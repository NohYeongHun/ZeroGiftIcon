package com.zerogift.backend.member.web;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.member.dto.SearchMember;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.service.MemberSearchService;
import com.zerogift.backend.security.dto.LoginInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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


}
