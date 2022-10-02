package com.zerogift.member.presentation;

import com.zerogift.member.application.MemberLoginService;
import com.zerogift.member.application.dto.MemberLoginRequest;
import com.zerogift.member.application.dto.MemberRegisterRequest;
import com.zerogift.member.domain.Member;
import com.zerogift.support.auth.token.TokenDto;
import com.zerogift.support.auth.userdetails.MemberInfo;
import com.zerogift.support.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        @RequestParam @Valid String email,
        @RequestParam @Valid String authToken
    ) {
        memberLoginService.confirmEmail(email, authToken);
        return ResponseEntity.ok().body(
            Result.builder()
                .status(200)
                .success(true)
                .data("인증이 완료되었습니다.")
                .build()
        );
    }
}
