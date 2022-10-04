package com.zerogift.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static com.zerogift.utils.DataMakeUtils.상품_생성;
import static com.zerogift.utils.DataMakeUtils.상품_이미지_생성;
import static com.zerogift.utils.DataMakeUtils.회원_생성;

import com.zerogift.acceptance.AcceptanceTest;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.pay.application.PayService;
import com.zerogift.pay.application.dto.PayHisoryRequest;
import com.zerogift.pay.repository.PayHistoryRepository;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.ProductImage;
import com.zerogift.product.infrastructure.FileUtil;
import com.zerogift.product.repository.ProductImageRepository;
import com.zerogift.product.repository.ProductRepository;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class PayServiceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private GiftBoxRepository giftBoxRepository;

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private PayService payService;

    @MockBean
    private FileUtil fileUtil;

    Member member;
    Product product;
    ProductImage productImage;

    @BeforeEach
    void setup() throws IOException {
        super.setUp();
        member = memberRepository.save(회원_생성("test@naver.com", "테스트"));
        product = productRepository.save(상품_생성("테스트 상품", "테스트 상품 설명", member));
        productImage = productImageRepository.save(상품_이미지_생성("https://test.com"));
    }

    @DisplayName("결제 테스트")
    @Test
    void payTest() throws IOException {
        when(fileUtil.update((BufferedImage) any())).thenReturn("https://test.com");

        payService.pay(createPayHisotryRequest(), member.getEmail());

        assertThat(giftBoxRepository.findAll()).hasSize(1);
        assertThat(payHistoryRepository.findAll()).hasSize(1);
    }

    private PayHisoryRequest createPayHisotryRequest() {
        return PayHisoryRequest.builder()
            .impUid("test_uid")
            .merchantUid("test_merchanteUid")
            .pgProvider("test_provider")
            .pgTid("test_pgTid")
            .productId(product.getId())
            .sendId(member.getId())
            .message("생일 축하해요")
            .build();
    }

}