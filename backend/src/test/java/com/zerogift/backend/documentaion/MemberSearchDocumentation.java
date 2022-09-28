package com.zerogift.backend.documentaion;

import com.zerogift.backend.member.service.MemberSearchService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static com.zerogift.backend.documentaion.member.MemberSearchStep.member_list_search_documentation;
import static com.zerogift.backend.documentaion.member.MemberSearchStep.member_list_search_response_create;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MemberSearchDocumentation extends Documentation{

    @MockBean
    private MemberSearchService memberSearchService;

    @Test
    void memberListSearch(){
        when(memberSearchService.searchMemberList(any(), any())).thenReturn(member_list_search_response_create());

        RestAssured
                .given(specification).log().all()
                .filter(member_list_search_documentation())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("nickname", "nickname")
                .queryParam("email","email")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/member-search/member-list")
                .then().log().all().extract();
    }
}
