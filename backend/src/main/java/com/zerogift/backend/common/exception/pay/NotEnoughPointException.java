package com.zerogift.backend.common.exception.pay;

import com.zerogift.backend.common.exception.code.PayErrorCode;

public class NotEnoughPointException extends RuntimeException {
    private final PayErrorCode errorCode;
    private final String errorMessage;

    public NotEnoughPointException(PayErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
