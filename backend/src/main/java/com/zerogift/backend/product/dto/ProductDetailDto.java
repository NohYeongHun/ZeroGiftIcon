package com.zerogift.backend.product.dto;

import com.zerogift.backend.product.type.Category;
import java.util.List;

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
    List<ProductImageDto> images;
}
