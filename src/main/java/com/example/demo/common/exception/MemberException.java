package com.example.demo.common.exception;

import com.example.demo.common.exception.code.MemberErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final MemberErrorCode errorCode;
    private final String errorMessage;

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
