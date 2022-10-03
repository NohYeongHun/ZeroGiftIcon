package com.zerogift.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static com.zerogift.step.GiftBoxStep.선물한_상세페이지_조회;
import static com.zerogift.step.GiftBoxStep.선물함_기프트콘_사용;
import static com.zerogift.step.GiftBoxStep.선물함_조회_요청;
import static com.zerogift.step.PayHistoryStep.상품_결제_요청;
import static com.zerogift.step.ProductStep.상품_생성_요청;
import static com.zerogift.step.ProductStep.상품_이미지_생성_요청;
import static com.zerogift.utils.DataMakeUtils.회원_생성;

import com.zerogift.gift.application.dto.GiftBoxDetail;
import com.zerogift.gift.application.dto.GiftBoxDto;
import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.product.infrastructure.s3.FileUtil;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.support.auth.oauth.application.TokenService;
import com.zerogift.support.auth.oauth.infrastructure.RefreshTokenRepository;
import com.zerogift.support.auth.userdetails.MemberInfo;
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
class GiftBoxAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenService tokenService;

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

        GiftBoxDto extract = response.jsonPath().getObject("data[0]", GiftBoxDto.class);

        assertThat(extract).isEqualTo(
            new GiftBoxDto(1L, "test", "https://test.com", "test 설명", false, 회원.getId(), 회원.getNickname(), 상품_아이디, false, false, null));
    }

    @DisplayName("선물함 상세 페이지 조회 테스트")
    @Test
    void findByGiftBoxDetail() {
        ExtractableResponse<Response> response = 선물한_상세페이지_조회(토큰, 선물함_아이디);

        GiftBoxDetail giftBoxDetail = response.jsonPath().getObject("data", GiftBoxDetail.class);

        assertThat(giftBoxDetail).isEqualTo(
            new GiftBoxDetail("test", "https://test.com", null, false, null));
    }

    @DisplayName("기프트콘 사용을 테스트 합니다.")
    @Test
    void useGiftConTest() {
        GiftBox giftBox = giftBoxRepository.findById(선물함_아이디).get();

        ExtractableResponse<Response> response = 선물함_기프트콘_사용(giftBox.getId(), giftBox.getCode());

        assertThat(giftBoxRepository.findById(선물함_아이디).get().getIsUse()).isTrue();
    }

}