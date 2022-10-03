package com.zerogift.product.application.dto;

import com.zerogift.product.domain.Category;
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
    private Long likeCount;
    List<ProductImageDto> images;
}
