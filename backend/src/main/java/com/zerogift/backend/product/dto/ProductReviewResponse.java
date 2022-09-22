package com.zerogift.backend.product.dto;

import com.zerogift.backend.product.entity.Product;
import com.zerogift.backend.product.type.Category;
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
