package com.zerogift.backend.common.exception.pay;

import com.zerogift.backend.common.exception.code.PayErrorCode;

public class NotEnoughProductCountException extends RuntimeException{
    private final PayErrorCode errorCode;
    private final String errorMessage;

    public NotEnoughProductCountException(PayErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
