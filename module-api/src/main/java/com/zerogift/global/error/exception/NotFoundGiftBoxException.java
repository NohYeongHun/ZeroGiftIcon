package com.zerogift.global.error.exception;

import com.zerogift.global.error.code.GiftBoxErrorCode;
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
