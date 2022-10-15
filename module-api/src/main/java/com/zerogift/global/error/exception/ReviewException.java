package com.zerogift.global.error.exception;

import com.zerogift.global.error.code.ReviewErrorCode;
import lombok.Getter;

@Getter
public class ReviewException extends RuntimeException{
    private final ReviewErrorCode errorCode;
    private final String errorMessage;

    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
