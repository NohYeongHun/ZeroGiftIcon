package com.zerogift.backend.view.service;

import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.common.exception.product.ProductException;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.security.dto.LoginInfo;
import com.zerogift.backend.view.entity.ViewHistory;
import com.zerogift.backend.view.repository.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ViewHistoryServiceImpl implements ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    public void addViewHistory(LoginInfo loginInfo, Long productId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 상품 정보 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 상품을 조회한 기록이 없으면 조회수 증가
        if (!(member.getId() == product.getMember().getId())) {
            if (!viewHistoryRepository.existsByMemberAndProduct(member, product)) {
                // 조회 기록 저장
                ViewHistory viewHistory = ViewHistory.builder()
                        .product(product)
                        .member(member)
                        .build();
                viewHistoryRepository.save(viewHistory);

                // 'Product Entity' viewCount 에 +1
                product.plusViewCount();
                productRepository.save(product);
            }
        }
    }


}
