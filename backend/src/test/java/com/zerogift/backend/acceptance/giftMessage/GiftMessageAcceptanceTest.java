package com.zerogift.backend.acceptance.giftMessage;

import static com.zerogift.backend.step.GiftMessageStep.감사메시지_보내기;
import static com.zerogift.backend.step.GiftMessageStep.감사메시지_상세내용_조회;
import static com.zerogift.backend.step.GiftMessageStep.감사메시지_폼_조회;
import static com.zerogift.backend.step.PayHistoryStep.상품_결제_요청;
import static com.zerogift.backend.step.ProductStep.상품_생성_요청;
import static com.zerogift.backend.step.ProductStep.상품_이미지_생성_요청;
import static com.zerogift.backend.utils.DataMakeUtils.회원_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.zerogift.backend.acceptance.AcceptanceTest;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.giftMessage.entity.GiftMessage;
import com.zerogift.backend.giftMessage.repository.GiftMessageRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.pay.repository.PayHistoryRepository;
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
class GiftMessageAcceptanceTest extends AcceptanceTest {

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

    @Autowired
    private GiftMessageRepository giftMessageRepository;

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

    @DisplayName("감사 메시지 보내기전 폼 조회")
    @Test
    void getGiftMessageFormTest() {
        ExtractableResponse<Response> response = 감사메시지_폼_조회(토큰, 상품_아이디);

        assertThat(response.jsonPath().getString("sendMemberName")).isEqualTo("test");
        assertThat(response.jsonPath().getString("productImage")).isEqualTo("https://test.com");
    }

    @DisplayName("감사 메시지 보내기 테스트")
    @Test
    void sendGiftMessageTest() {
        ExtractableResponse<Response> response = 감사메시지_보내기(토큰, 선물함_아이디,"생일 축하");

        GiftMessage giftMessage = giftMessageRepository.findAll().get(0);

        assertThat(giftMessage.getMessage()).isEqualTo("생일 축하");
    }

    @DisplayName("감사메시지 상세 보기 조회")
    @Test
    void getGiftMessage() {
        감사메시지_보내기(토큰, 선물함_아이디, "생일 축하");

        ExtractableResponse<Response> response = 감사메시지_상세내용_조회(토큰,  giftMessageRepository.findAll().get(0).getId());

        assertThat(response.jsonPath().getString("message")).isEqualTo("생일 축하");
    }

}