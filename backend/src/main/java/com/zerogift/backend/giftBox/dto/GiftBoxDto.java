package com.zerogift.backend.giftBox.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class GiftBoxDto {

    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private boolean use;
    private Long sendId;
    private String sendNickname;
    private Long productId;
    private boolean answer;
    private boolean review;

    public GiftBoxDto(Long id, String name, String imageUrl, String description, boolean use,
        Long sendId, String sendNickname, Long productId, boolean answer, boolean review) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.use = use;
        this.sendId = sendId;
        this.sendNickname = sendNickname;
        this.productId = productId;
        this.answer = answer;
        this.review = review;
    }
}
