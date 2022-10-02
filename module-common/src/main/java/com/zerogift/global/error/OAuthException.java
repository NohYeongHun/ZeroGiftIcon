package com.zerogift.global.error;

import com.zerogift.global.error.dto.OAuthErrorCode;

public class OAuthException extends RuntimeException {

    private final OAuthErrorCode errorCode;
    private final String errorMessage;

    public OAuthException(OAuthErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

}
