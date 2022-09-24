package com.zerogift.backend.common.exception.review;

import com.zerogift.backend.common.exception.code.ReviewErrorCode;
import lombok.Getter;

@Getter
public class ReviewException extends RuntimeException {
    private final ReviewErrorCode errorCode;
    private final String errorMessage;

    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
