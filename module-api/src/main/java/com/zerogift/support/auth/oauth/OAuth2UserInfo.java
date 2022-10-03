package com.zerogift.support.auth.oauth;

import com.zerogift.member.domain.AuthType;
import java.util.Map;
import lombok.Getter;

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
