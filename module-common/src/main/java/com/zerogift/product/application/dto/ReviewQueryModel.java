package com.zerogift.product.application.dto;

import com.zerogift.member.domain.Member;
import com.zerogift.product.domain.Product;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewQueryModel {
    private Long reviewId;
    private Integer rank;
    private String description;
    private Member member;
    private Product product;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    public ReviewQueryModel(Long reviewId, Integer rank, String description, Member member, Product product, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.reviewId = reviewId;
        this.rank = rank;
        this.description = description;
        this.member = member;
        this.product = product;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

}
