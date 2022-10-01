package com.zerogift.backend.giftBox.dto;

import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GiftBoxDetail {
    private String name;
    private String imageUrl;
    private String barcodUrl;
    private boolean answer;
    private Long giftMessageId;

    public GiftBoxDetail(String name, String imageUrl, String barcodUrl, boolean answer, Long giftMessageId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.barcodUrl = barcodUrl;
        this.answer = answer;
        this.giftMessageId = Objects.isNull(giftMessageId) ? null : giftMessageId;
    }
}
