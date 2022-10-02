package com.zerogift.product.application.dto;


import com.zerogift.product.domain.Category;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private Integer count;
    @Enumerated(EnumType.STRING)
    private Category category;
    private List<Long> productImageIds;
}
