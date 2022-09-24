package com.zerogift.backend.documentaion;

import static com.zerogift.backend.documentaion.giftMessage.getGiftMessageStep.감사_메시지_조회_문서화;
import static com.zerogift.backend.documentaion.giftMessage.getGiftMessageStep.감사_메시지_조회_응답_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerogift.backend.config.authorization.AuthenticationPrincipalArgumentResolver;
import com.zerogift.backend.giftMessage.service.GiftMessageService;
import com.zerogift.backend.security.dto.AdminInfo;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class GiftMessageDocumentation extends Documentation{

    @MockBean
    private GiftMessageService giftMessageService;

    @MockBean
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Test
    void getGiftMessage() {
        when(authenticationPrincipalArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
            AdminInfo.builder().build());
        when(giftMessageService.getGiftMessage(any(), any())).thenReturn(감사_메시지_조회_응답_생성());

        RestAssured
            .given(specification).log().all()
            .filter(감사_메시지_조회_문서화())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/giftMessage/{giftMessageId}", 1L)
            .then().log().all().extract();
    }

}
