package com.zerogift.backend.documentaion.member;

import com.zerogift.backend.member.dto.MemberSearchDetail;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class MemberSearchDetailStep {

    public static MemberSearchDetail member_detail_search_response_create(){

        return MemberSearchDetail.builder()
                .id(1L)
                .nickname("nickname")
                .email("email")
                .point(0)
                .build();
    }

    public static RestDocumentationFilter member_detail_search_documentation() {
        return document("memberDetailSearch",
                preprocessRequest(modifyUris()
                                .scheme("https")
                                .host("zerogift.p-e.kr")
                                .removePort(),
                        prettyPrint()),
                preprocessResponse(prettyPrint()),
                member_detail_search_response_fields()
        );
    }

//    public static PathParametersSnippet member_detail_search_request_fields() {
//        return pathParameters();
//    }


    public static ResponseFieldsSnippet member_detail_search_response_fields() {
        return responseFields(
                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 고유 번호"),
                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                fieldWithPath("data.point").type(JsonFieldType.NUMBER).description("회원 포인트")
        );
    }
}
