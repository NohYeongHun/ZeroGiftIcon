package com.zerogift.backend.review.controller;

import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.review.model.ReviewInput;
import com.zerogift.backend.review.service.ReviewService;
import com.zerogift.backend.security.dto.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/user/{productId}/review")
    public ResponseEntity<?> addReview(@AuthenticationPrincipal LoginInfo loginInfo,
                                        @PathVariable Long productId,
                                        @RequestBody ReviewInput reviewInput) {
        return ResponseEntity.ok(reviewService.addReview(loginInfo, productId, reviewInput));
    }

    @PatchMapping("/user/{reviewId}/review")
    public ResponseEntity<?> modifyReview(@AuthenticationPrincipal LoginInfo loginInfo,
                                          @PathVariable Long reviewId,
                                          @RequestBody ReviewInput reviewInput) {
        return ResponseEntity.ok(reviewService.modifyReview(loginInfo, reviewId, reviewInput));
    }

    private static final String REMOVE_REVIEW_SUCCESS = "리뷰 삭제를 성공하였습니다.";

    @DeleteMapping("/user/{reviewId}/review")
    public ResponseEntity<?> removeReview(@AuthenticationPrincipal LoginInfo loginInfo,
                                          @PathVariable Long reviewId) {
        reviewService.removeReview(loginInfo, reviewId);
        return ResponseEntity.ok(REMOVE_REVIEW_SUCCESS);
    }

    @GetMapping("/review/user")
    public ResponseEntity<?> userReviewList(@AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok().body(reviewService.userReviewList(loginInfo));
    }

    @GetMapping("/review/product/{productId}")
    public ResponseEntity<?> productReviewList(@PathVariable Long productId) {
        return ResponseEntity.ok().body(reviewService.productReviewList(productId));
    }
}