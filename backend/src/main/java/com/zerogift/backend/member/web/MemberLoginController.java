package com.zerogift.backend.member.web;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.email.dto.EmailAuthRequest;
import com.zerogift.backend.member.dto.MemberLoginRequest;
import com.zerogift.backend.member.dto.MemberRegisterRequest;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.service.MemberLoginService;
import com.zerogift.backend.security.dto.MemberInfo;
import com.zerogift.backend.security.dto.TokenDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "MemberLogin", description = "멤버 로그인 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-auth")
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    @Operation(
            summary = "로그인", description = "일반 회원 로그인입니다.",
            tags = {"MemberLogin"}
    )
    @PostMapping("/login")
    public ResponseEntity<Result<?>> login(
            @RequestBody @Valid MemberLoginRequest request) {

        TokenDto login = memberLoginService.login(request);

        return ResponseEntity.ok().body(Result.builder()
                .status(200)
                .success(true)
                .data(login)
                .build());
    }

    @Operation(
            summary = "회원 등록", description = "일반 회원 등록입니다.",
            tags = {"MemberLogin"}
    )
    @PostMapping("/register")
    public ResponseEntity<Result<?>> register(
            @RequestBody @Valid MemberRegisterRequest request
    ){

        Member member = memberLoginService.registerNewMember(request);

        return ResponseEntity.ok().body(Result.builder()
                .status(200)
                .success(true)
                .data(member.getEmail())
                .build());
    }

    @Operation(
            summary = "회원 로그아웃입니다.", description = "일반 회원 로그아웃 입니다.",
            tags = {"MemberLogin"}
    )
    @GetMapping("/logout")
    public ResponseEntity<Result<?>> logout(@AuthenticationPrincipal MemberInfo memberInfo){

        memberLoginService.logoutMember(memberInfo);

        return ResponseEntity.ok().body(
                Result.builder()
                        .status(200)
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Operation(
            summary = "일반 회원 이메일 인증입니다.", description = "일반 회원 이메일 인증입니다.",
            tags = {"MemberLogin"}
    )
    @GetMapping("/confirm-email")
    public ResponseEntity<Result<?>> confirmEmail(
            @RequestBody @Valid EmailAuthRequest request
    )
    {
        memberLoginService.confirmEmail(request);
        return ResponseEntity.ok().body(
                Result.builder()
                        .status(200)
                        .success(true)
                        .data("인증이 완료되었습니다.")
                        .build()
        );
    }
}
