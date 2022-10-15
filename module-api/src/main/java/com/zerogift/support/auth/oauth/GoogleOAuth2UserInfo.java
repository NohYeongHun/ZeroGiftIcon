package com.zerogift.support.auth.oauth;

import com.zerogift.member.domain.AuthType;
import java.util.Map;
import lombok.Getter;

@Getter
public class GoogleOAuth2UserInfo extends OAuth2UserInfo{

    private final String name;
    private final String email;
    private final AuthType authType;
    private String authId;
    private String profileImageUrl;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.email = (String)attributes.get("email");
        this.authType = AuthType.GOOGLE;
        this.name = (String) attributes.get("name");
    }

}
