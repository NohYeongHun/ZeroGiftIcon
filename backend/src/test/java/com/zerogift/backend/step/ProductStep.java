package com.zerogift.backend.step;

import com.zerogift.backend.product.dto.NewProductRequest;
import com.zerogift.backend.product.type.Category;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

public class ProductStep {

    private static final String multipartUrl = "/images/ice.jpeg";

    public static ExtractableResponse<Response> 상품_생성_요청(String token, String name, Integer price, Long productImageId) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(NewProductRequest.builder()
                .name(name)
                .description("test 설명")
                .price(price)
                .category(Category.BIRTHDAY)
                .productImageIds(List.of(productImageId))
                .build())
            .when().post("/admin/product")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 상품_이미지_생성_요청(String token) throws IOException {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when()
            .multiPart("files", new ClassPathResource(multipartUrl).getFile(), MediaType.MULTIPART_FORM_DATA_VALUE)
            .post("/admin/upload")
            .then().log().all().extract();
    }

}
