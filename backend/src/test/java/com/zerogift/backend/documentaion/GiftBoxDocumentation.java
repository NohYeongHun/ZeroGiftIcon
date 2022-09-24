package com.zerogift.backend.documentaion;


import static com.zerogift.backend.documentaion.giftbox.FindByGiftBoxListStep.선물함_리스트_응답_생성;
import static com.zerogift.backend.documentaion.giftbox.FindByGiftBoxListStep.선물함_리스트_조회_문서화;
import static com.zerogift.backend.documentaion.giftbox.GetGiftBoxDetailStep.선물함_상세보기_응답_생성;
import static com.zerogift.backend.documentaion.giftbox.GetGiftBoxDetailStep.선물함_상세보기_조회_문서화;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerogift.backend.config.authorization.AuthenticationPrincipalArgumentResolver;
import com.zerogift.backend.giftBox.service.GiftBoxService;
import com.zerogift.backend.security.dto.AdminInfo;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class GiftBoxDocumentation extends Documentation{

    @MockBean
    private GiftBoxService giftBoxService;

    @MockBean
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Test
    void findGiftBoxList() {
        when(authenticationPrincipalArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
            AdminInfo.builder().build());
        when(giftBoxService.findByGiftBoxList(any(), any())).thenReturn(선물함_리스트_응답_생성());

        RestAssured
            .given(specification).log().all()
            .filter(선물함_리스트_조회_문서화())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", 1)
            .queryParam("size", 10)
            .when().get("/giftbox")
            .then().log().all().extract();
    }


    @Test
    void giftBoxDetail() {
        when(authenticationPrincipalArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(
            AdminInfo.builder().build());
        when(giftBoxService.getGiftBoxDetail(any(), any())).thenReturn(선물함_상세보기_응답_생성());

        RestAssured
            .given(specification).log().all()
            .filter(선물함_상세보기_조회_문서화())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/giftbox/{giftBoxId}", 1L)
            .then().log().all().extract();
    }


}
