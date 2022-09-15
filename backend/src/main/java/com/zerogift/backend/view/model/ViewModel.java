package com.zerogift.backend.view.model;


import com.zerogift.backend.product.entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ViewModel {
    private long id;
    private String name;
    private long viewCount;

    public static ViewModel of(Product product) {
        return ViewModel.builder()
                .id(product.getId())
                .name(product.getName())
                .viewCount(product.getViewCount())
                .build();
    }
}
