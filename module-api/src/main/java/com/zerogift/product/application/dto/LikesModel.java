package com.zerogift.product.application.dto;

import com.zerogift.member.application.dto.MemberLikeResponse;
import com.zerogift.product.domain.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LikesModel {

    private Long id;
    private MemberLikeResponse memberLikeResponse;
    private ProductLikeResponse productLikeResponse;

    public static LikesModel of(Likes likes) {
        return LikesModel.builder()
            .id(likes.getId())
            .memberLikeResponse(MemberLikeResponse.of(likes.getMember()))
            .productLikeResponse(ProductLikeResponse.of(likes.getProduct()))
            .build();
    }
}
