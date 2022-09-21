package com.zerogift.backend.step;

import com.zerogift.backend.product.dto.NewProductRequest;
import com.zerogift.backend.product.type.Category;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

public class ProductStep {

    private static final String multipartUrl = "/images/ice.jpeg";

    public static ExtractableResponse<Response> 상품_생성_요청(String token, String name, Integer price, Long productImageId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("description", "test 설명");
        params.put("price", price);
        params.put("category", Category.BIRTHDAY);
        params.put("productImageIds", List.of(productImageId));
        params.put("count", 100);

        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
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
