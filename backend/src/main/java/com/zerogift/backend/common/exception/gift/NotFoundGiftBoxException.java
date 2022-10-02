package com.zerogift.backend.common.exception.gift;

import com.zerogift.backend.common.exception.code.GiftBoxErrorCode;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import lombok.Getter;

@Getter
public class NotFoundGiftBoxException extends RuntimeException {

    private final GiftBoxErrorCode errorCode;
    private final String errorMessage;

    public NotFoundGiftBoxException(GiftBoxErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
