package com.zerogift.support.auth.oauth;

import com.zerogift.member.domain.AuthType;
import com.zerogift.global.error.OAuthException;
import com.zerogift.global.error.dto.OAuthErrorCode;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthType authType,
        Map<String, Object> attributes) {
        switch (authType) {
            case KAKAO:
                return new KakaoOAuth2UserInfo(attributes);
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            default:
                throw new OAuthException(OAuthErrorCode.INVALID_PROVIDER);
        }
    }

}
