package com.zerogift.documentation.pay;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PayStep {

    public static RestDocumentationFilter 결졔_요청_문서화() {
        return document("pay",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("zerogift.p-e.kr")
                    .removePort(),
                prettyPrint()),
            preprocessResponse(prettyPrint()),
            결제_요청_요청_정의(),
            결제_요청_응답_정의()
        );
    }


    public static RequestFieldsSnippet 결제_요청_요청_정의() {
        return requestFields(
            fieldWithPath("impUid").type(JsonFieldType.STRING).description("아임포트 아이디값"),
            fieldWithPath("merchantUid").type(JsonFieldType.STRING).description("상점 아이디 값"),
            fieldWithPath("pgProvider").type(JsonFieldType.STRING).description("PG 아이디 값"),
            fieldWithPath("pgTid").type(JsonFieldType.STRING).description("PG 거래번호"),
            fieldWithPath("usePoint").type(JsonFieldType.NUMBER).description("사용 포인트"),
            fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
            fieldWithPath("sendId").type(JsonFieldType.NUMBER).description("선물 보낼 아이디"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("감사 메시지")
        );
    }

    public static ResponseFieldsSnippet 결제_요청_응답_정의() {
        return responseFields(
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지")
        );
    }

}
