package com.zerogift.documentation.member;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.zerogift.member.application.dto.MemberSearchOutputDto;
import com.zerogift.member.application.dto.MemberSearchInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class MemberSearchListStep {
    public static MemberSearchInfo member_list_search_response_create(){

        List<MemberSearchOutputDto> memberSearchOutputDtoList = new ArrayList<>();
        memberSearchOutputDtoList.add(
            MemberSearchOutputDto.builder()
                .id(1L)
                .profileImageUrl("profileImageUrl")
                .nickname("nickname")
                .build()
        );

        return MemberSearchInfo.builder()
            .memberSearchOutputDtoList(
                memberSearchOutputDtoList
            )
            .totalPage(1)
            .page(0)
            .size(10)
            .build();
    }

    public static RestDocumentationFilter member_list_search_documentation() {
        return document("memberListSearch",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("zerogift.p-e.kr")
                    .removePort(),
                prettyPrint()),
            preprocessResponse(prettyPrint()),
            member_list_search_request_fields(),
            member_list_search_response_fields()
        );
    }

    public static RequestParametersSnippet member_list_search_request_fields() {
        return requestParameters(
            parameterWithName("email").description("email"),
            parameterWithName("nickname").description("nickname")
        );
    }

    public static ResponseFieldsSnippet member_list_search_response_fields() {
        return responseFields(
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("data.memberSearchOutputDtoList[0].id").type(JsonFieldType.NUMBER).description("회원 고유 번호"),
            fieldWithPath("data.memberSearchOutputDtoList[0].profileImageUrl").type(JsonFieldType.STRING).description("회원 이미지 URL"),
            fieldWithPath("data.memberSearchOutputDtoList[0].nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
            fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("총 페이지"),
            fieldWithPath("data.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
            fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기")
        );
    }
}
