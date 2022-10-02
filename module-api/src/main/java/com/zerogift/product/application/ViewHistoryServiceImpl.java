package com.zerogift.product.application;

import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.ProductErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.ProductException;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ViewHistory;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.product.repository.ViewHistoryRepository;
import com.zerogift.support.auth.userdetails.LoginInfo;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewHistoryServiceImpl implements ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional
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
            }
        }
    }


}
