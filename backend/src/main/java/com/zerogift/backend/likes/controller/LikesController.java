package com.zerogift.backend.likes.controller;

import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.likes.repository.LikesRepository;
import com.zerogift.backend.likes.service.LikesService;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LikesController {

    private final LikesService likesService;

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;

    // --- 테스트 용 ---
    // 상품에 좋아요 누르기 (임의의 이메일을 RequestBody 로 받아서 사용)
    @PutMapping("/user/{productId}/likes/temp")
    public ResponseEntity<?> pressLikeTemp(@PathVariable Long productId, @RequestHeader("EMAIL") String email) {
        return likesService.pressLike(email, productId);
    }

    // --- 테스트 용 ---
    // 상품에 좋아요 지우기 (임의의 이메일을 RequestBody 로 받아서 사용)
    @DeleteMapping("/user/{productId}/likes/temp")
    public ResponseEntity<?> likeCancelTemp(@PathVariable Long productId, @RequestHeader("EMAIL") String email) {
        return likesService.likeCancel(email, productId);
    }

    // --- 테스트 용 ---
    // 사용자가 좋아요한 리스트 호출 (임의의 이메일을 RequestBody 로 받아서 사용)
    @GetMapping("/user/likes/list/temp")
    public ResponseEntity<?> likeListTemp(@RequestHeader("EMAIL") String email) {
        return likesService.likeList(email);
    }

    // 상품에 좋아요 누르기
    @PutMapping("/user/{productId}/likes")
    public ResponseEntity<?> pressLike(@PathVariable Long productId) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) {
            return ResponseEntity.badRequest().body(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        return likesService.pressLike(email, productId);
    }

    // 상품에 좋아요 지우기
    @DeleteMapping("/user/{productId}/likes")
    public ResponseEntity<?> likeCancel(@PathVariable Long productId) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) {
            return ResponseEntity.badRequest().body(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        return likesService.likeCancel(email, productId);
    }

    // 사용자가 좋아요한 리스트 호출
    @GetMapping("/user/likes/list")
    public ResponseEntity<?> likeList() {
        String email = TokenUtil.getAdminEmail();
        if (email == null) {
            return ResponseEntity.badRequest().body(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        return likesService.likeList(email);
    }


}
