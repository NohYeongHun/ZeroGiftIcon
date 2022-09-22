package com.zerogift.backend.review.model;

import com.zerogift.backend.member.model.MemberReviewResponse;
import com.zerogift.backend.product.dto.ProductReviewResponse;
import com.zerogift.backend.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReviewResponse {

    private Integer rank;
    private String description;
    private MemberReviewResponse member;
    private ProductReviewResponse product;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder()
                .rank(review.getRank())
                .description(review.getDescription())
                .member(MemberReviewResponse.of(review.getMember()))
                .product(ProductReviewResponse.of(review.getProduct()))
                .createDate(review.getCreateDate())
                .updateDate(review.getUpdateDate())
                .build();
    }
}
