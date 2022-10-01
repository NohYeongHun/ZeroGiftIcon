package com.zerogift.backend.likes.controller;

import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.likes.service.LikesService;
import com.zerogift.backend.security.dto.LoginInfo;
import com.zerogift.backend.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LikesController {

    private final LikesService likesService;

    @Operation(
            summary = "좋아요", description = "상품에 좋아요 누르기,이미 좋아요 한 상품은 취소하기",
            tags = {"Likes"}
    )
    @PostMapping("/user/{productId}/likes")
    public ResponseEntity<?> pressLike(@PathVariable Long productId, @AuthenticationPrincipal LoginInfo loginInfo) {
        return likesService.pressLike(loginInfo, productId);
    }


    @Operation(
            summary = "회원의 좋아요 리스트", description = "회원이 좋아요 눌렀던 상품들 리스트 호출",
            tags = {"Likes"}
    )
    @GetMapping("/user/likes/list")
    public ResponseEntity<?> likeList(@AuthenticationPrincipal LoginInfo loginInfo) {
        return likesService.likeList(loginInfo);
    }
}
