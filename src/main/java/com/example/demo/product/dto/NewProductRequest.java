package com.example.demo.product.dto;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotEmpty
    private String name;
    private String description;
    @NotNull
    private Integer price;
    @Enumerated(EnumType.STRING)
    private Category category;
    private List<Long> productImageIds;
}
