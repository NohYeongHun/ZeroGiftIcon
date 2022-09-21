package com.zerogift.backend.step;

import com.zerogift.backend.pay.dto.PayHisoryRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PayHistoryStep {

    public static ExtractableResponse<Response> 상품_결제_요청(String token, Integer usePoint, Long productId, Long sendId, String message) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("impUid", "test_impUid");
        params.put("merchantUid", "test_merchatUid");
        params.put("pgProvider", "test_provider");
        params.put("pgTid", "test_pgTid");
        params.put("usePoint", usePoint);
        params.put("productId", productId);
        params.put("sendId", sendId);
        params.put("message", message);


        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/pay")
            .then().log().all()
            .extract();
    }

}
