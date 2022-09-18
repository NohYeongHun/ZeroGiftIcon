package com.zerogift.backend.likes.controller;

import com.zerogift.backend.likes.service.LikesService;
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
    public ResponseEntity<?> pressLike(@PathVariable Long productId) {
        String email = TokenUtil.getAdminEmail() != null ? TokenUtil.getAdminEmail() : TokenUtil.getMemberEmail();
        return likesService.pressLike(email, productId);
    }

    // 상품에 좋아요 지우기
    @DeleteMapping("/user/{productId}/likes")
    public ResponseEntity<?> likeCancel(@PathVariable Long productId) {
        String email = TokenUtil.getAdminEmail() != null ? TokenUtil.getAdminEmail() : TokenUtil.getMemberEmail();
        return likesService.likeCancel(email, productId);
    }

    // 사용자가 좋아요한 리스트 호출
    @GetMapping("/user/likes/list")
    public ResponseEntity<?> likeList() {
        String email = TokenUtil.getAdminEmail() != null ? TokenUtil.getAdminEmail() : TokenUtil.getMemberEmail();
        return likesService.likeList(email);
    }

}
