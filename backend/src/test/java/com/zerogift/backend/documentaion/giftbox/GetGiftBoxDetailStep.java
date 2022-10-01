package com.zerogift.backend.documentaion.giftbox;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.zerogift.backend.giftBox.dto.GiftBoxDetail;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class GetGiftBoxDetailStep {

    public static GiftBoxDetail 선물함_상세보기_응답_생성() {
        return new GiftBoxDetail("아이스 아메리카노",
            "https://img.danawa.com/prod_img/500000/609/014/img/3014609_1.jpg?shrink=330:330&_v=20220524144914"
            , "https://test-barcode.com", true, 1L);
    }

    public static RestDocumentationFilter 선물함_상세보기_조회_문서화() {
        return document("giftBoxDetail",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("zerogift.p-e.kr")
                    .removePort(),
                prettyPrint()),
            preprocessResponse(prettyPrint()),
            선물함_상세보기_조회_요청_정의(),
            선물함_상세보기_조회_응답_정의()
        );
    }

    public static PathParametersSnippet 선물함_상세보기_조회_요청_정의() {
        return pathParameters(
            parameterWithName("giftBoxId").description("선물함 아이디")
        );
    }

    public static ResponseFieldsSnippet 선물함_상세보기_조회_응답_정의() {
        return responseFields(
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
            fieldWithPath("data.name").type(JsonFieldType.STRING).description("선물함 아이디"),
            fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("상품 이름"),
            fieldWithPath("data.barcodUrl").type(JsonFieldType.STRING).description("상품 설명"),
            fieldWithPath("data.answer").type(JsonFieldType.BOOLEAN).description("사용 여부"),
            fieldWithPath("data.giftMessageId").type(JsonFieldType.NUMBER).description("감사 메시지 아이디")
        );
    }

}
