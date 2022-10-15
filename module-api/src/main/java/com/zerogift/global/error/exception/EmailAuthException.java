package com.zerogift.global.error.exception;

import com.zerogift.global.error.code.EmailAuthErrorCode;
import lombok.Getter;

@Getter
public class EmailAuthException extends RuntimeException {
    private final EmailAuthErrorCode errorCode;
    private final String errorMessage;

    public EmailAuthException(EmailAuthErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
