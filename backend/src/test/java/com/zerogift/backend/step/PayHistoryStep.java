package com.zerogift.backend.step;

import com.zerogift.backend.pay.dto.PayHisoryRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PayHistoryStep {

    public static ExtractableResponse<Response> 상품_결제_요청(String token, Integer usePoint, Long productId, Long sendId, String message) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(PayHisoryRequest.builder()
                .impUid("test_impUid")
                .merchantUid("test_merchatUid")
                .pgProvider("test_provider")
                .usePoint(usePoint)
                .pgTid("test_pgTid")
                .productId(productId)
                .sendId(sendId)
                .message(message)
                .build())
            .when().post("/pay")
            .then().log().all()
            .extract();
    }

}
