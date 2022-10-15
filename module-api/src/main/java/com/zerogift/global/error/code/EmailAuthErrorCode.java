package com.zerogift.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum EmailAuthErrorCode {
    AUTH_TOKEN_NOT_FOUND("해당하는 토큰을 찾을 수 없습니다.");

    private String description;
}
