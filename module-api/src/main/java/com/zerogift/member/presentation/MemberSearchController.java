package com.zerogift.member.presentation;

import com.zerogift.member.application.MemberSearchService;
import com.zerogift.member.application.dto.SearchMember;
import com.zerogift.support.auth.authorization.AuthenticationPrincipal;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.MyPageableDto;
import com.zerogift.support.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MemberSearch", description = "회원 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-search")
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    @Operation(
        summary = "회원 리스트 조회", description = "회원 리스트 조회입니다.",
        tags = {"MemberSearch"}
    )
    @GetMapping("/member-list")
    public ResponseEntity<Result<?>> getMemberList(
        SearchMember searchMember,
        MyPageableDto myPageableDto ) {

        return ResponseEntity.ok().body(
            Result.builder()
                .status(200)
                .success(true)
                .data(memberSearchService.searchMemberList(searchMember, myPageableDto))
                .build()
        );
    }

    @Operation(
        summary = "로그인 회원 상세 조회", description = "회원 리스트 조회입니다.",
        tags = {"MemberSearch"}
    )
    @GetMapping("/member")
    public ResponseEntity<Result<?>> getMemberDetail(
        @AuthenticationPrincipal LoginInfo loginInfo) {

        return ResponseEntity.ok().body(
            Result.builder()
                .status(200)
                .success(true)
                .data(
                    memberSearchService
                        .searchMemberDetail(loginInfo.getId()))
                .build()
        );
    }
}
