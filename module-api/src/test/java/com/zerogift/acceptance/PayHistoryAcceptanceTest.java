package com.zerogift.acceptance;

import static com.zerogift.step.PayHistoryStep.상품_결제_요청;
import static com.zerogift.step.ProductStep.상품_생성_요청;
import static com.zerogift.step.ProductStep.상품_이미지_생성_요청;
import static com.zerogift.utils.DataMakeUtils.회원_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.pay.repository.PayHistoryRepository;
import com.zerogift.product.infrastructure.FileUtil;
import com.zerogift.support.auth.oauth.application.TokenService;
import com.zerogift.support.auth.oauth.infrastructure.RefreshTokenRepository;
import com.zerogift.support.auth.userdetails.MemberInfo;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class PayHistoryAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private GiftBoxRepository giftBoxRepository;

    @MockBean
    private FileUtil fileUtil;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    String token;
    Member 회원;
    Long 상품이미지_아이디;
    Long 상품_아이디;

    @BeforeEach
    public void setUp() throws IOException {
        super.setUp();
        when(fileUtil.update((MultipartFile) any())).thenReturn("https://test.com");
        doNothing().when(refreshTokenRepository).save(anyString(), anyString(), any());

        회원 = memberRepository.save(회원_생성("test@naver.com", "test"));
        MemberInfo memberInfo = MemberInfo.of(회원);

        token = tokenService.issueAllToken(memberInfo).getAccessToken();

        상품이미지_아이디 = 상품_이미지_생성_요청(token).jsonPath().getLong("data[0].productImageId");
        상품_아이디 = 상품_생성_요청(token, "test", 1000, 상품이미지_아이디).jsonPath().getLong("data.productId");
    }

    @DisplayName("결제 성공 테스트")
    @Test
    void paySuccessTest() throws IOException {
        when(fileUtil.update((BufferedImage) any())).thenReturn("https://test.com");

        상품_결제_요청(token, 0, 상품_아이디, 회원.getId(), "test_메시지");

        히스토리와_선물함에_값이_들어갔는지_체크();
    }

    private void 히스토리와_선물함에_값이_들어갔는지_체크() {
        assertThat(payHistoryRepository.findAll()).hasSize(1);
        assertThat(giftBoxRepository.findAll()).hasSize(1);
    }

    @DisplayName("포인트가 부족하여 에러가 발생")
    @Test
    void notEnoughPointExceptionTest() {
        ExtractableResponse<Response> response = 상품_결제_요청(token, 100, 상품_아이디, 회원.getId(),
            "test_메시지");

        assertThat(response.jsonPath().getString("errorDescription")).isEqualTo(
            "사용 포인트가 보유 포인트보다 많습니다.");
    }


}
