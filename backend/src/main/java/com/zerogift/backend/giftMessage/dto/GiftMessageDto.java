package com.zerogift.backend.giftMessage.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class GiftMessageDto {
    private String productImage;
    private String sendName;
    private String message;

    public GiftMessageDto(String productImage, String sendName, String message) {
        this.productImage = productImage;
        this.sendName = sendName;
        this.message = message;
    }
}
