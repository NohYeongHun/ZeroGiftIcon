package com.zerogift.backend.giftMessage.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GiftMessageRequest {

    @ApiModelProperty(example = "선물함 아이디")
    @NotNull(message = "giftBoxId는 필수값입니다.")
    private Long giftBoxId;

    @ApiModelProperty(example = "메시지 받을 사람 아이디")
    @NotNull(message = "sendMemberId는 필수 값입니다.")
    private Long sendMemberId;

    @ApiModelProperty(example = "상품 아이디")
    @NotNull(message = "productId는 필수 값입니다.")
    private Long productId;

    @ApiModelProperty(example = "감사 메시지 내용")
    @NotNull(message = "message는 필수 값입니다.")
    private String message;

    @Builder
    public GiftMessageRequest(Long giftBoxId, Long sendMemberId, Long productId,
        String message) {
        this.giftBoxId = giftBoxId;
        this.sendMemberId = sendMemberId;
        this.productId = productId;
        this.message = message;
    }
}
