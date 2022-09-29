package com.zerogift.backend.product.dto;

import com.zerogift.backend.product.type.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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