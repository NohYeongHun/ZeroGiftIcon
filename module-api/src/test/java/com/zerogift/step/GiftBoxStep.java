package com.zerogift.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.LinkedHashMap;
import java.util.Map;

public class GiftBoxStep {

    public static ExtractableResponse<Response> 선물함_조회_요청(String token) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("page", 0);
        params.put("size", 10);

        return RestAssured
            .given().log().all()
            .params(params)
            .auth().oauth2(token)
            .when().get("/giftbox")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 선물한_상세페이지_조회(String token, Long giftBoxId) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().get("/giftbox/{giftboxId}", giftBoxId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 선물함_기프트콘_사용(Long giftBoxId, String code) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("code", code);
        params.put("giftBoxId", giftBoxId);

        return RestAssured
            .given().log().all()
            .params(params)
            .when().get("/barcode")
            .then().log().all()
            .extract();
    }

}
