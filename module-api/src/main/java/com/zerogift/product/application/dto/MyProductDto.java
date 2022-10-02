package com.zerogift.product.application.dto;

import com.zerogift.product.domain.Category;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class MyProductDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer inventory;
    private Category category;
    private LocalDateTime createdAt;
    private String mainImageUrl;
}