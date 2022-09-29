package com.zerogift.backend.review.controller;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.review.model.ReviewInput;
import com.zerogift.backend.review.service.ReviewService;
import com.zerogift.backend.security.dto.LoginInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    private static final String REMOVE_REVIEW_SUCCESS = "리뷰 삭제를 성공하였습니다.";

    @Operation(
            summary = "리뷰 등록", description = "사용한 상품 리뷰를 등록합니다.",
            tags = {"Review"}
    )
    @PostMapping("/user/{productId}/review")
    public ResponseEntity<?> addReview(@AuthenticationPrincipal LoginInfo loginInfo,
                                        @PathVariable Long productId,
                                        @RequestBody ReviewInput reviewInput) {
        return ResponseEntity.ok(Result.builder()
                .data(reviewService.addReview(loginInfo, productId, reviewInput))
                .build()
        );
    }

    @Operation(
            summary = "리뷰 수정", description = "등록된 상품 리뷰를 수정합니다.",
            tags = {"Review"}
    )
    @PatchMapping("/user/{reviewId}/review")
    public ResponseEntity<?> modifyReview(@AuthenticationPrincipal LoginInfo loginInfo,
                                          @PathVariable Long reviewId,
                                          @RequestBody ReviewInput reviewInput) {
        return ResponseEntity.ok(Result.builder()
                .data(reviewService.modifyReview(loginInfo, reviewId, reviewInput))
                .build()
        );
    }

    @Operation(
            summary = "리뷰 삭제", description = "등록된 상품 리뷰를 삭제합니다.",
            tags = {"Review"}
    )
    @DeleteMapping("/user/{reviewId}/review")
    public ResponseEntity<?> removeReview(@AuthenticationPrincipal LoginInfo loginInfo,
                                          @PathVariable Long reviewId) {
        reviewService.removeReview(loginInfo, reviewId);
        return ResponseEntity.ok(Result.builder()
                .data(REMOVE_REVIEW_SUCCESS)
                .build()
        );
    }

    @Operation(
            summary = "회원 리뷰 리스트", description = "회원이 작성한 리뷰들을 불러옵니다.",
            tags = {"Review"}
    )
    @GetMapping("/review/user")
    public ResponseEntity<?> userReviewList(@AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(Result.builder()
                .data(reviewService.userReviewList(loginInfo))
                .build()
        );
    }

    @Operation(
            summary = "상품 리뷰 리스트", description = "상품에 등록된 리뷰를 불러옵니다.",
            tags = {"Review"}
    )
    @GetMapping("/review/product/{productId}")
    public ResponseEntity<?> productReviewList(@PathVariable Long productId) {
        return ResponseEntity.ok(Result.builder()
                .data(reviewService.productReviewList(productId))
                .build()
        );
    }
}