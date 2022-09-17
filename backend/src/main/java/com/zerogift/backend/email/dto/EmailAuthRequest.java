package com.zerogift.backend.email.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuthRequest {
    private String email;
    private String authToken;

    @Builder
    public EmailAuthRequest(
            String email,
            String authToken
    ){
        this.email = email;
        this.authToken = authToken;
    }
}
