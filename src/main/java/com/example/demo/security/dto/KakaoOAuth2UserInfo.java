package com.example.demo.security.dto;


import com.example.demo.common.type.AuthType;
import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

	private final String name;
	private final String email;
	private final AuthType authType;


	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

		this.email = (String)kakaoAccount.get("email");
		this.authType = AuthType.KAKAO;
		this.name = (String) kakaoProfile.get("nickname");
	}


}
