package com.zerogift.documentation.giftMessage;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.zerogift.gift.application.dto.GiftMessageDto;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class GetGiftMessageStep {

    public static GiftMessageDto 감사_메시지_조회_응답_생성() {
        return new GiftMessageDto(
            "https://img.danawa.com/prod_img/500000/609/014/img/3014609_1.jpg?shrink=330:330&_v=20220524144914",
            "아이스 아메리카노", "생일 축하합니다"
        );
    }

    public static RestDocumentationFilter 감사_메시지_조회_문서화() {
        return document("giftMessage",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("zerogift.p-e.kr")
                    .removePort(),
                prettyPrint()),
            preprocessResponse(prettyPrint()),
            감사_메시지_조회_요청_정의(),
            감사_메시지_조회_응답_정의()
        );
    }

    public static PathParametersSnippet 감사_메시지_조회_요청_정의() {
        return pathParameters(
            parameterWithName("giftMessageId").description("감사 메시지 아이디")
        );
    }

    public static ResponseFieldsSnippet 감사_메시지_조회_응답_정의() {
        return responseFields(
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
            fieldWithPath("data.productImage").type(JsonFieldType.STRING).description("감사 메시지 이미지"),
            fieldWithPath("data.sendName").type(JsonFieldType.STRING).description("보낸 사람 닉네임"),
            fieldWithPath("data.message").type(JsonFieldType.STRING).description("감사 메시지")
        );
    }

}
