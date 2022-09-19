package com.zerogift.backend.security.dto;


import com.zerogift.backend.common.exception.member.OAuthException;
import com.zerogift.backend.common.exception.code.OAuthErrorCode;
import com.zerogift.backend.common.type.AuthType;

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
