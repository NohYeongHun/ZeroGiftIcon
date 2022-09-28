package com.zerogift.backend.documentaion;

import com.zerogift.backend.config.authorization.AuthenticationPrincipalArgumentResolver;
import com.zerogift.backend.member.service.MemberLoginService;
import com.zerogift.backend.security.dto.AdminInfo;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static com.zerogift.backend.documentaion.giftMessage.GetGiftMessageStep.감사_메시지_조회_문서화;
import static com.zerogift.backend.documentaion.giftMessage.GetGiftMessageStep.감사_메시지_조회_응답_생성;
import static com.zerogift.backend.documentaion.member.MemberLoginStep.member_register_documentation;
import static com.zerogift.backend.documentaion.member.MemberLoginStep.member_register_response_create;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MemberLoginDocumentation extends Documentation{

    @MockBean
    private MemberLoginService memberLoginService;

    @MockBean
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Test
    void getMemberRegister() {

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
}
