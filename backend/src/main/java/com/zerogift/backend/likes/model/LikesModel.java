package com.zerogift.backend.likes.model;

import com.zerogift.backend.likes.entity.Likes;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.model.MemberLikeResponse;
import com.zerogift.backend.product.dto.ProductLikeResponse;
import com.zerogift.backend.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
                .memberLikeResponse(MemberLikeResponse.of(likes.getMember()))
                .productLikeResponse(ProductLikeResponse.of(likes.getProduct()))
                .build();
    }
}
