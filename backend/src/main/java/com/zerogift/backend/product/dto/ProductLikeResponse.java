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
public class ProductLikeResponse {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Category category;
    private Long viewCount;
    private Long likeCount;
    private String mainImageUrl;

    public static ProductLikeResponse of(Product product) {
        return ProductLikeResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .viewCount(product.getViewCount())
                .likeCount(product.getLikeCount())
                .mainImageUrl(product.getMainImageUrl())
                .build();
    }
}
