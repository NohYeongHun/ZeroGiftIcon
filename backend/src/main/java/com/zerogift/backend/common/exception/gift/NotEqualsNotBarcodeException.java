package com.zerogift.backend.common.exception.gift;

import com.zerogift.backend.common.exception.code.GiftBoxErrorCode;
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
