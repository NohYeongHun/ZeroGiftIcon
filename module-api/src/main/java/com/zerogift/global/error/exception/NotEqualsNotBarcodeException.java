package com.zerogift.global.error.exception;

import com.zerogift.global.error.code.GiftBoxErrorCode;
import lombok.Getter;

@Getter
public class NotEqualsNotBarcodeException extends RuntimeException{

    private final GiftBoxErrorCode errorCode;
    private final String errorMessage;

    public NotEqualsNotBarcodeException(GiftBoxErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
