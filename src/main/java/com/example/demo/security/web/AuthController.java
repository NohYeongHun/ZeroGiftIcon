package com.example.demo.security.web;

import com.example.demo.common.dto.Result;
import com.example.demo.security.dto.MemberInfo;
import com.example.demo.security.dto.TokenDto;
import com.example.demo.security.repository.RefreshTokenRepository;
import com.example.demo.security.service.OAuthService;
import com.example.demo.security.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name = "Auth",description = "본인 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final OAuthService oAuthService;

    @Operation(
            summary = "로그아웃", description = "로그아웃 합니다.",
            security = {@SecurityRequirement(name = "Authorization")},
            tags = {"Auth"}
    )
    @GetMapping("/logout")
    public ResponseEntity<Result<?>> logout(@ApiIgnore @AuthenticationPrincipal MemberInfo memberInfo) {
        refreshTokenRepository.deleteByUsername(memberInfo.getEmail());
        return ResponseEntity.ok().body(
                Result.builder()
                        .status(200)
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Operation(
            summary = "토큰 재발급", description = "토큰을 재발급합니다. refresh Token값을 보내주세요",
            tags = {"Auth"}
    )
    @PostMapping("/refresh")
    public ResponseEntity<Result<?>> reissueToken(@RequestBody String refreshToken) {

        TokenDto token = tokenService.refresh(refreshToken);

        return ResponseEntity.ok().body(
                Result.builder()
                        .status(200)
                        .success(true)
                        .data(token)
                        .build()
        );
    }

    @Operation(
            summary = "로그인", description = "OAuth2를 이용한 로그인을 합니다.",
            tags = {"Auth"}
    )
    @GetMapping("/login/{provider}")
    public ResponseEntity<Result<?>> login(@Parameter(description = "인증 제공자") @PathVariable String provider,
                                           @Parameter(description = "인증 코드") @RequestParam String code) {

        TokenDto login = oAuthService.login(provider, code);

        return ResponseEntity.ok().body(Result.builder()
                .status(200)
                .success(true)
                .data(login)
                .build());
    }

}