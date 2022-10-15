package com.zerogift.product.application.dto;

import com.zerogift.member.application.dto.MemberReviewResponse;
import com.zerogift.product.domain.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReviewResponse {

    private Long reviewId;
    private Integer rank;
    private String description;
    private MemberReviewResponse member;
    private ProductReviewResponse product;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
            .reviewId(review.getId())
            .rank(review.getRank())
            .description(review.getDescription())
            .member(MemberReviewResponse.of(review.getMember()))
            .product(ProductReviewResponse.of(review.getProduct()))
            .createDate(review.getCreatedDate())
            .updateDate(review.getLastModifiedDate())
            .build();
    }

    public static ReviewResponse listFrom(ReviewQueryModel review) {
        return ReviewResponse.builder()
            .reviewId(review.getReviewId())
            .rank(review.getRank())
            .description(review.getDescription())
            .member(MemberReviewResponse.of(review.getMember()))
            .product(ProductReviewResponse.of(review.getProduct()))
            .createDate(review.getCreatedDate())
            .updateDate(review.getLastModifiedDate())
            .build();
    }
}
