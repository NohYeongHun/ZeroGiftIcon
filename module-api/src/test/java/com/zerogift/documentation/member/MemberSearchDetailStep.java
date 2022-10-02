package com.zerogift.documentation.member;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.zerogift.member.application.dto.MemberSearchDetail;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

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
