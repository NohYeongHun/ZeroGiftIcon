package com.zerogift.support.auth.oauth;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OAuthTokenResponse {

    private String token_type;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
    private String id_token;

    @Builder
    public OAuthTokenResponse(
        String token_type,
        String access_token,
        int expires_in,
        String refresh_token,
        int refresh_token_expires_in,
        String id_token
    ){
        this.token_type = token_type;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.refresh_token_expires_in = refresh_token_expires_in;
        this.id_token = id_token;
    }

}
