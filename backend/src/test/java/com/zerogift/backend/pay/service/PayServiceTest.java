package com.zerogift.backend.pay.service;

import static com.zerogift.backend.utils.DataMakeUtils.상품_생성;
import static com.zerogift.backend.utils.DataMakeUtils.상품_이미지_생성;
import static com.zerogift.backend.utils.DataMakeUtils.회원_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerogift.backend.acceptance.AcceptanceTest;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.pay.dto.PayHisoryRequest;
import com.zerogift.backend.pay.repository.PayHistoryRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.entity.ProductImage;
import com.zerogift.backend.product.repository.ProductImageRepository;
import com.zerogift.backend.product.repository.ProductRepository;
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
import org.springframework.web.multipart.MultipartFile;
import com.zerogift.backend.util.FileUtil;

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
        assertThat(giftBoxRepository.findAll()).hasSize(1);
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