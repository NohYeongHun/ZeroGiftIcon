package com.zerogift.backend.likes.service;

import com.zerogift.backend.acceptance.AcceptanceTest;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.common.exception.product.ProductException;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.product.service.ProductService;
import com.zerogift.backend.security.dto.AdminInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static com.zerogift.backend.utils.DataMakeUtils.상품_생성;
import static com.zerogift.backend.utils.DataMakeUtils.회원_생성;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LikesServiceImplTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private LikesService likesService;


    Member member;
    Member member1;
    Member member2;

    Product product;
    Product product1;
    Product product2;


    AdminInfo adminInfo;
    AdminInfo adminInfo1;
    AdminInfo adminInfo2;

    @BeforeEach
    void setup() throws IOException {
        super.setUp();
        member = memberRepository.save(회원_생성("test@naver.com", "테스트", "test_id"));
        member1 = memberRepository.save(회원_생성("hong@naver.com", "홍길동", "test1_id"));
        member2 = memberRepository.save(회원_생성("kim@naver.com", "김철수", "test2_id"));

        product = productRepository.save(상품_생성("테스트 상품", "테스트 상품 설명", member));
        product1 = productRepository.save(상품_생성("테스트 상품1", "테스트 상품 설명1", member));
        product2 = productRepository.save(상품_생성("테스트 상품2", "테스트 상품 설명2", member));


        adminInfo = AdminInfo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole().name())
                .build();
        adminInfo1 = AdminInfo.builder()
                .id(member1.getId())
                .email(member1.getEmail())
                .role(member1.getRole().name())
                .build();
        adminInfo2 = AdminInfo.builder()
                .id(member2.getId())
                .email(member2.getEmail())
                .role(member2.getRole().name())
                .build();

    }

    @DisplayName(" 좋아요 누르기 ")
    @Test
    void likesTest() {
        likesService.pressLike(adminInfo, product.getId());

        product = productRepository.findById(product.getId())
                        .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        assertThat(product.getLikeCount()).isEqualTo(1);
    }

    @DisplayName(" 이미 좋아요 한 상품 좋아요 눌러보기 ")
    @Test
    void alreadyLikesTest() {
        // 처음 누를떄
        likesService.pressLike(adminInfo, product.getId());

        product = productRepository.findById(product.getId())
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        assertThat(product.getLikeCount()).isEqualTo(1);

        // 두번째 누를떄
        likesService.pressLike(adminInfo, product.getId());

        product = productRepository.findById(product.getId())
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        assertThat(product.getLikeCount()).isEqualTo(0);
    }
}
