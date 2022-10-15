package com.zerogift.member.domain;

import com.zerogift.global.error.OAuthException;
import com.zerogift.global.error.dto.OAuthErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthType {
    KAKAO("카카오톡"),
    GOOGLE("구글"),
    GENERAL("일반");

    private String korName;


    public static AuthType of(String authType) {
        try {
            return AuthType.valueOf(authType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OAuthException(OAuthErrorCode.INVALID_PROVIDER);
        }
    }

}
