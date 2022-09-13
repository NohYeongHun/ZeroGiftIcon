package com.zerogift.backend.security.dto;

import com.zerogift.backend.common.type.AuthType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {
	protected Map<String, Object> attributes;

	public OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public abstract String getName();

	public abstract String getEmail();

	public abstract AuthType getAuthType();

	public abstract String getAuthId();

	public abstract String getProfileImageUrl();

}
