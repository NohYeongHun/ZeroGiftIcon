package com.zerogift.backend.likes.controller;

import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.likes.service.LikesService;
import com.zerogift.backend.security.dto.LoginInfo;
import com.zerogift.backend.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LikesController {

    private final LikesService likesService;

    // 상품에 좋아요 누르기
    @PutMapping("/user/{productId}/likes")
    public ResponseEntity<?> pressLike(@PathVariable Long productId, @AuthenticationPrincipal LoginInfo loginInfo) {
        return likesService.pressLike(loginInfo, productId);
    }

    // 상품에 좋아요 지우기
    @DeleteMapping("/user/{productId}/likes")
    public ResponseEntity<?> likeCancel(@PathVariable Long productId, @AuthenticationPrincipal LoginInfo loginInfo) {
        return likesService.likeCancel(loginInfo, productId);
    }

    // 사용자가 좋아요한 리스트 호출
    @GetMapping("/user/likes/list")
    public ResponseEntity<?> likeList(@AuthenticationPrincipal LoginInfo loginInfo) {
        return likesService.likeList(loginInfo);
    }
}
