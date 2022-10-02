package com.zerogift.documentation;

import static com.zerogift.documentation.member.MemberLoginStep.member_login_documentation;
import static com.zerogift.documentation.member.MemberLoginStep.member_login_response_create;
import static com.zerogift.documentation.member.MemberLoginStep.member_register_documentation;
import static com.zerogift.documentation.member.MemberLoginStep.member_register_response_create;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerogift.member.application.MemberLoginService;
import com.zerogift.support.auth.authorization.AuthenticationPrincipalArgumentResolver;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class MemberLoginDocumentation extends Documentation{

    @MockBean
    private MemberLoginService memberLoginService;

    @MockBean
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Test
    void memberRegister() {

        Map<String, Object> params = new HashMap<>();
        params.put("email", "example@naver.com");
        params.put("password", "password");
        params.put("nickname", "nickname");

        when(memberLoginService.registerNewMember(any())).thenReturn(member_register_response_create());

        RestAssured
            .given(specification).log().all()
            .filter(member_register_documentation())
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/member-auth/register")
            .then().log().all().extract();
    }

    @Test
    void memberLogin(){
        when(memberLoginService.login(any())).thenReturn(member_login_response_create());

        Map<String, Object> params = new HashMap<>();
        params.put("email", "example@naver.com");
        params.put("password", "password");

        RestAssured
            .given(specification).log().all()
            .filter(member_login_documentation())
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/member-auth/login")
            .then().log().all().extract();

    }
}
