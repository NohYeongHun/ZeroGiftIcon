package com.zerogift.backend.documentaion;

import static com.zerogift.backend.documentaion.giftMessage.GetGiftMessageStep.감사_메시지_조회_문서화;
import static com.zerogift.backend.documentaion.pay.PayStep.결졔_요청_문서화;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.zerogift.backend.config.authorization.AuthenticationPrincipalArgumentResolver;
import com.zerogift.backend.pay.service.PayService;
import com.zerogift.backend.security.dto.AdminInfo;
import io.restassured.RestAssured;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class PayDocumentation extends Documentation {

    @MockBean
    private PayService payService;

    @MockBean
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Test
    void pay() {
        when(authenticationPrincipalArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
            AdminInfo.builder().build());
        doNothing().when(payService).pay(any(), any());

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("impUid", "test_impUid");
        params.put("merchantUid", "test_merchatUid");
        params.put("pgProvider", "test_provider");
        params.put("pgTid", "test_pgTid");
        params.put("usePoint", 1000);
        params.put("productId", 1L);
        params.put("sendId", 2L);
        params.put("message", "생일 축하");

        RestAssured
                .given(specification).log().all()
                .filter(결졔_요청_문서화())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/pay")
                .then().log().all().extract();
    }

}
