package com.zerogift.product.application.dto;

import com.zerogift.member.application.dto.MemberLikeResponse;
import com.zerogift.product.domain.ViewHistory;
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
