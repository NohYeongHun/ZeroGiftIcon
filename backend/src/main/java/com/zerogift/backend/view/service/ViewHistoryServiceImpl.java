package com.zerogift.backend.view.service;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.view.entity.ViewHistory;
import com.zerogift.backend.view.model.ViewModel;
import com.zerogift.backend.view.repository.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ViewHistoryServiceImpl implements ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<Result<?>> addViewCount(Long productId) {
        // 상품 정보 가져오기
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.PRODUCT_NOT_FOUND).build()
            );
        }
        Product product = optionalProduct.get();

        // 'Product Entity' 에 조회수 +1 해서 저장
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);

        return ResponseEntity.ok().body(Result.builder().data(ViewModel.of(product)).build());
    }

    @Override
    public ResponseEntity<Result<?>> addViewHistory(String email, Long productId) {
        // 회원 정보 가져오기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(403).success(false).data(MemberErrorCode.MEMBER_NOT_FOUND).build()
            );
        }
        Member member = optionalMember.get();

        // 상품 정보 가져오기
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.PRODUCT_NOT_FOUND).build()
            );
        }
        Product product = optionalProduct.get();

        // 자기자신의 물건 조회해도 조회수는 오르지 않음
        if (member.getId() == product.getMember().getId()) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.SELF_VIEW_NOT_COUNT).build()
            );
        }

        // 계정 하나당 상품 조회수 증가 중복 안됨
        if (viewHistoryRepository.countByMemberAndProduct(member, product) > 0) {
            return ResponseEntity.badRequest().body(
                    Result.builder().status(404).success(false).data(ProductErrorCode.ALREADY_VIEW_PRODUCT).build()
            );
        }

        // 조회 기록 저장
        ViewHistory viewHistory = ViewHistory.builder()
                .product(product)
                .member(member)
                .build();
        viewHistoryRepository.save(viewHistory);

        // 상품의 총 조회수를 'Product Entity' 에 저장
        long viewCount = viewHistoryRepository.countByProduct(product);
        product.setViewCount(viewCount);
        productRepository.save(product);

        return ResponseEntity.ok().body(Result.builder().data(ViewModel.of(product)).build());
    }


}
