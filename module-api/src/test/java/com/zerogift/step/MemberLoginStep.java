package com.zerogift.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class MemberLoginStep {

    public static ExtractableResponse<Response> 일반_로그인_요청(String email, String password) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/member-auth/login")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 일반_회원_가입_요청(String email, String password, String nickname) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("nickname", nickname);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/member-auth/register")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 일반_회원_이메일_인증_요청(String email, String authToken) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("email", email);
        params.put("authToken", authToken);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().get("/member-auth/confirm-email")
            .then().log().all()
            .extract();
    }

}
