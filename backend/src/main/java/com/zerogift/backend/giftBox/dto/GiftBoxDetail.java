package com.zerogift.backend.giftBox.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GiftBoxDetail {
    private String name;
    private String imageUrl;
    private String barcodUrl;
    private boolean isAnswer;

    public GiftBoxDetail(String name, String imageUrl, String barcodUrl, boolean isAnswer) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.barcodUrl = barcodUrl;
        this.isAnswer = isAnswer;
    }
}
