package com.example.demo.product.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.demo.product.type.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class NewProductRequest {
    private String name;
    private String description;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private Category category;
}
