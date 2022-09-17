package com.zerogift.backend.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class GiftMessageStep {

    public static ExtractableResponse<Response> 감사메시지_폼_조회(String token, Long giftBoxId) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().get("/giftMessage/form/{giftBoxId}", giftBoxId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 감사메시지_보내기(String token, Long giftBoxId, Long sendMemberId, Long productId, String message) {
        Map<String, Object> params = new HashMap<>();
        params.put("giftBoxId", giftBoxId);
        params.put("sendMemberId", sendMemberId);
        params.put("productId", productId);
        params.put("message", message);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .auth().oauth2(token)
            .when().post("/giftMessage")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 감사메시지_상세내용_조회(String token, Long giftMesageId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .when().get("/giftMessage/{giftMessageId}", giftMesageId)
            .then().log().all()
            .extract();
    }

}
