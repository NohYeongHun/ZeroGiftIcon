package com.zerogift.backend.likes.model;

import com.zerogift.backend.likes.entity.Likes;
import com.zerogift.backend.member.model.MemberLikeResponse;
import com.zerogift.backend.product.dto.ProductLikeResponse;
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
