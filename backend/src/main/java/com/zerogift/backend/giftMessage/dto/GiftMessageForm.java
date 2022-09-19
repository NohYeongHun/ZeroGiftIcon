package com.zerogift.backend.giftMessage.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class GiftMessageForm {
    private String sendMemberName;
    private String productImage;

    public GiftMessageForm(String sendMemberName, String productImage) {
        this.sendMemberName = sendMemberName;
        this.productImage = productImage;
    }
}
