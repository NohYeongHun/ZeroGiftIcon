package com.zerogift.backend.giftBox.controller;

import static com.zerogift.backend.step.GiftBoxStep.선물한_상세페이지_조회;
import static com.zerogift.backend.step.GiftBoxStep.선물함_기프트콘_사용;
import static com.zerogift.backend.step.GiftBoxStep.선물함_조회_요청;
import static com.zerogift.backend.step.PayHistoryStep.상품_결제_요청;
import static com.zerogift.backend.step.ProductStep.상품_생성_요청;
import static com.zerogift.backend.step.ProductStep.상품_이미지_생성_요청;
import static com.zerogift.backend.utils.DataMakeUtils.회원_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.zerogift.backend.acceptance.AcceptanceTest;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.pay.repository.PayHistoryRepository;
import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.repository.ProductRepository;
import com.zerogift.backend.security.dto.MemberInfo;
import com.zerogift.backend.security.repository.RefreshTokenRepository;
import com.zerogift.backend.security.service.TokenService;
import com.zerogift.backend.util.FileUtil;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class GiftBoxControllerTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private GiftBoxRepository giftBoxRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @MockBean
    private FileUtil fileUtil;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    String 토큰;
    Member 회원;
    Long 상품이미지_아이디;
    Long 상품_아이디;
    Long 선물함_아이디;

    @BeforeEach
    public void setUp() throws IOException {
        super.setUp();
        when(fileUtil.update((MultipartFile) any())).thenReturn("https://test.com");
        doNothing().when(refreshTokenRepository).save(anyString(), anyString(), any());

        회원 = memberRepository.save(회원_생성("test@naver.com", "test"));
        MemberInfo memberInfo = MemberInfo.of(회원);
        토큰 = tokenService.issueAllToken(memberInfo).getAccessToken();

        상품이미지_아이디 = 상품_이미지_생성_요청(토큰).jsonPath().getLong("data[0].productImageId");
        상품_아이디 = 상품_생성_요청(토큰, "test", 1000, 상품이미지_아이디).jsonPath().getLong("data.productId");

        상품_결제_요청(토큰, 0, 상품_아이디, 회원.getId(), "생일 축하");
        선물함_아이디 = giftBoxRepository.findAll().get(0).getId();
    }

    @DisplayName("선물함 조회 리스트 조회 테스트")
    @Test
    void findByGiftBoxList() {
        ExtractableResponse<Response> response = 선물함_조회_요청(토큰);

        assertThat(response.jsonPath().getList("content")).hasSize(1);
    }

    @DisplayName("선물함 상세 페이지 조회 테스트")
    @Test
    void findByGiftBoxDetail() {
        ExtractableResponse<Response> response = 선물한_상세페이지_조회(토큰, 선물함_아이디);

        Product product = productRepository.findById(상품_아이디).get();
        
        assertThat(response.jsonPath().getString("name")).isEqualTo(product.getName());
    }

    @DisplayName("기프트콘 사용을 테스트 합니다.")
    @Test
    void useGiftConTest() {
        GiftBox giftBox = giftBoxRepository.findById(선물함_아이디).get();

        ExtractableResponse<Response> response = 선물함_기프트콘_사용(giftBox.getId(), giftBox.getCode());

        assertThat(giftBoxRepository.findById(선물함_아이디).get().getIsUse()).isTrue();
    }

}