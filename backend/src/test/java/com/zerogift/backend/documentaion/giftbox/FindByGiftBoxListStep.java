package com.zerogift.backend.documentaion.giftbox;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.zerogift.backend.giftBox.dto.GiftBoxDto;
import java.util.List;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class FindByGiftBoxListStep {

    public static List<GiftBoxDto> 선물함_리스트_응답_생성() {
        return List.of(
            new GiftBoxDto(1L, "아이스 아메리카노", "https://img.danawa.com/prod_img/500000/609/014/img/3014609_1.jpg?shrink=330:330&_v=20220524144914",
                "더운날에는 아이스 아메리카노 어떠세요?", false, 1L, "제로베이스", 1L, false, false),
            new GiftBoxDto(2L, "두번쨰 아이스 아메리카노", "https://img.danawa.com/prod_img/500000/609/014/img/3014609_1.jpg?shrink=330:330&_v=20220524144914",
                "조금 싼 아이스 아메리카노", false, 2L, "패스트캠퍼스", 2L, false, false)
        );
    }

    public static RestDocumentationFilter 선물함_리스트_조회_문서화() {
        return document("giftBox",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("zerogift.p-e.kr")
                        .removePort(),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                선물함_리스트_조회_요청_정의(),
                선물함_리스트_조회_응답_정의()
            );
    }

    public static RequestParametersSnippet 선물함_리스트_조회_요청_정의() {
        return requestParameters(
            parameterWithName("page").description("페이지 번호"),
            parameterWithName("size").description("페이지 사이즈")
        );
    }

    public static ResponseFieldsSnippet 선물함_리스트_조회_응답_정의() {
        return responseFields(
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("data").type(JsonFieldType.ARRAY).description("데이터"),
            fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("선물함 아이디"),
            fieldWithPath("data[].name").type(JsonFieldType.STRING).description("상품 이름"),
            fieldWithPath("data[].imageUrl").type(JsonFieldType.STRING).description("상품 대표 이미지"),
            fieldWithPath("data[].description").type(JsonFieldType.STRING).description("상품 설명"),
            fieldWithPath("data[].use").type(JsonFieldType.BOOLEAN).description("사용 여부"),
            fieldWithPath("data[].sendId").type(JsonFieldType.NUMBER).description("선물한 사람 아이디"),
            fieldWithPath("data[].sendNickname").type(JsonFieldType.STRING).description("선물한 사람 닉네임"),
            fieldWithPath("data[].productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
            fieldWithPath("data[].answer").type(JsonFieldType.BOOLEAN).description("감사 메시지 여부"),
            fieldWithPath("data[].review").type(JsonFieldType.BOOLEAN).description("리뷰 여부")
        );
    }

}
