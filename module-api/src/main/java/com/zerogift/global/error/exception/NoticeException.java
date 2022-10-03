package com.zerogift.global.error.exception;

import com.zerogift.global.error.code.NoticeErrorCode;
import lombok.Getter;

@Getter
public class NoticeException extends RuntimeException {
    private final NoticeErrorCode errorCode;
    private final String errorMessage;

    public NoticeException(NoticeErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
