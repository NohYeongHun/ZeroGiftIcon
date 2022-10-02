package com.zerogift.support.auth.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenDto{
    private String accessToken;
    private String refreshToken;
    private int refreshTokenExpiredMin;

    @Builder
    public TokenDto(String accessToken, String refreshToken, int refreshTokenExpiredMin){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredMin = refreshTokenExpiredMin;
    }
}
