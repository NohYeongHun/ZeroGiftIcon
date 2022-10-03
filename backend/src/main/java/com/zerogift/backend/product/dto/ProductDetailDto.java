package com.zerogift.backend.product.dto;

import java.util.List;

import com.zerogift.backend.product.type.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ProductDetailDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Category category;
    private Long viewCount;
    private Long likeCount;
    List<ProductImageDto> images;
}
