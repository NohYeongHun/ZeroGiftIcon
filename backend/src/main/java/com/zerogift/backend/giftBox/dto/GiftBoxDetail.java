package com.zerogift.backend.giftBox.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GiftBoxDetail {
    private String name;
    private String imageUrl;
    private String barcodUrl;
    private boolean answer;

    public GiftBoxDetail(String name, String imageUrl, String barcodUrl, boolean answer) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.barcodUrl = barcodUrl;
        this.answer = answer;
    }
}
