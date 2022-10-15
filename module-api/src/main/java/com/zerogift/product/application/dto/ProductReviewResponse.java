package com.zerogift.product.application.dto;

import com.zerogift.product.domain.Category;
import com.zerogift.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductReviewResponse {
    private Long id;
    private String name;
    private Category category;
    private String mainImageUrl;

    public static ProductReviewResponse of(Product product) {
        return ProductReviewResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .category(product.getCategory())
            .mainImageUrl(product.getMainImageUrl())
            .build();
    }
}
