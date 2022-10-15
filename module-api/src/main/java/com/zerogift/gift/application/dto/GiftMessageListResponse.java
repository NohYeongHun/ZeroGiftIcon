package com.zerogift.gift.application.dto;

import com.zerogift.gift.domain.GiftMessage;
import lombok.*;

@Getter
public class GiftMessageListResponse {

    private final Long giftMessageId;

    private final String message;

    private final Long toMemberId;

    private final String toMemberName;

    private final Long fromMemberId;

    private final String fromMemberName;

    private final String fromMemberImageUrl;

    private final Long productId;

    private final String productName;

    private final Long giftBoxId;


    @Builder
    public GiftMessageListResponse(Long giftMessageId, String message, Long toMemberId, String toMemberName, Long fromMemberId, String fromMemberName, String fromMemberImageUrl, Long productId, String productName, Long giftBoxId) {
        this.giftMessageId = giftMessageId;
        this.message = message;
        this.toMemberId = toMemberId;
        this.toMemberName = toMemberName;
        this.fromMemberId = fromMemberId;
        this.fromMemberName = fromMemberName;
        this.fromMemberImageUrl = fromMemberImageUrl;
        this.productId = productId;
        this.productName = productName;
        this.giftBoxId = giftBoxId;
    }

    public static GiftMessageListResponse from (GiftMessage giftMessage) {
        return GiftMessageListResponse.builder()
                .giftMessageId(giftMessage.getId())
                .message(giftMessage.getMessage())
                .toMemberId(giftMessage.getToMember().getId())
                .toMemberName(giftMessage.getToMember().getNickname())
                .fromMemberId(giftMessage.getFromMember().getId())
                .fromMemberName(giftMessage.getFromMember().getNickname())
                .fromMemberImageUrl(giftMessage.getFromMember().getProfileImageUrl())
                .productId(giftMessage.getProduct().getId())
                .productName(giftMessage.getProduct().getName())
                .giftBoxId(giftMessage.getGiftBox().getId())
                .build();
    }
}
