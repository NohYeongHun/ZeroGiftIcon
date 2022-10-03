package com.zerogift.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerogift.documentation.giftMessage.GetGiftMessageStep;
import com.zerogift.gift.application.GiftMessageService;
import com.zerogift.support.auth.authorization.AuthenticationPrincipalArgumentResolver;
import com.zerogift.support.auth.userdetails.AdminInfo;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class GiftMessageDocumentation extends Documentation {

    @MockBean
    private GiftMessageService giftMessageService;

    @MockBean
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Test
    void getGiftMessage() {
        when(authenticationPrincipalArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
            AdminInfo.builder().build());
        when(giftMessageService.getGiftMessage(any(), any())).thenReturn(
            GetGiftMessageStep.감사_메시지_조회_응답_생성());

        RestAssured
            .given(specification).log().all()
            .filter(GetGiftMessageStep.감사_메시지_조회_문서화())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/giftMessage/{giftMessageId}", 1L)
            .then().log().all().extract();
    }

}
