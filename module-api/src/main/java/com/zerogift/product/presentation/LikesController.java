package com.zerogift.product.presentation;

import com.zerogift.product.application.LikesService;
import com.zerogift.support.auth.authorization.AuthenticationPrincipal;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Likes",description = "좋아요 관련 API")
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
        return ResponseEntity.ok(Result.builder()
            .data(likesService.likeList(loginInfo))
            .build()
        );
    }
}
