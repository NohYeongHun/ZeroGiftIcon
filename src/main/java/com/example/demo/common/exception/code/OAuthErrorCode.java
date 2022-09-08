package com.example.demo.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OAuthErrorCode {
    EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다."),
    EMAIL_ALREADY_SIGNED_UP("이미 가입되어있는 이메일입니다."),
    INVALID_PROVIDER("지원하지 않는 OAuth Provider 입니다.");
    private String description;

    public OAuthErrorCode setDescription(String message) {
        this.description = message;
        return this;
    }
}
