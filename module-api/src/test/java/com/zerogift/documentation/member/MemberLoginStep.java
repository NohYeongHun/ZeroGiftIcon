package com.zerogift.documentation.member;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.zerogift.member.domain.AuthType;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.MemberStatus;
import com.zerogift.member.domain.Role;
import com.zerogift.support.auth.token.TokenDto;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class MemberLoginStep {

    public static Member member_register_response_create(){
        return Member.builder()
            .email("example@naver.com")
            .nickname("nickname")
            .status(MemberStatus.PERMITTED)
            .authType(AuthType.GENERAL)
            .authId("null")
            .profileImageUrl("profileImageUrl")
            .role(Role.ROLE_MEMBER)
            .build();
    }

    public static RestDocumentationFilter member_register_documentation(){

        return document(
            "memberRegister",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("zerogift.p-e.kr")
                    .removePort(),
                prettyPrint()),
            preprocessResponse(prettyPrint()),
            member_register_request_fields(),
            member_register_response()
        );
    }


    public static RequestFieldsSnippet member_register_request_fields() {
        return requestFields(
            fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("password"),
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("nickname")
        );
    }

    public static ResponseFieldsSnippet member_register_response() {
        return responseFields(
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("data").type(JsonFieldType.STRING).description("멤버 이메일")
        );
    }

    public static RequestFieldsSnippet member_login_request_fields() {
        return requestFields(
            fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("password")
        );
    }

    public static ResponseFieldsSnippet member_login_response() {
        return responseFields(
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("accessToken"),
            fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("refreshToken"),
            fieldWithPath("data.refreshTokenExpiredMin").type(JsonFieldType.NUMBER).description("refreshTokenExpired")
        );
    }


    public static TokenDto member_login_response_create(){
        return TokenDto.builder()
            .accessToken("accessToken")
            .refreshToken("refreshToken")
            .refreshTokenExpiredMin(1440)
            .build();
    }

    public static RestDocumentationFilter member_login_documentation(){

        return document(
            "memberLogin",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("zerogift.p-e.kr")
                    .removePort(),
                prettyPrint()),
            preprocessResponse(prettyPrint()),
            member_login_request_fields(),
            member_login_response()
        );
    }

}
