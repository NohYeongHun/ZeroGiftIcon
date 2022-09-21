package com.zerogift.backend.view.model;

import com.zerogift.backend.member.model.MemberLikeResponse;
import com.zerogift.backend.product.dto.ProductLikeResponse;
import com.zerogift.backend.view.entity.ViewHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ViewModel {

    private long id;
    private MemberLikeResponse memberLikeResponse;
    private ProductLikeResponse productLikeResponse;


    public static ViewModel of(ViewHistory viewHistory) {
        return ViewModel.builder()
                .id(viewHistory.getId())
                .memberLikeResponse(MemberLikeResponse.of(viewHistory.getMember()))
                .productLikeResponse(ProductLikeResponse.of(viewHistory.getProduct()))
                .build();
    }
}
