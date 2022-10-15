package com.zerogift.global.error.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResultDto {
    private final String errorCode;
    private final String errorDescription;

    @Builder
    public ErrorResultDto(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
