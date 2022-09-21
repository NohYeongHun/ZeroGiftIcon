package com.zerogift.backend.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginRequest {

    private String email;
    private String password;

    @Builder
    public MemberLoginRequest(
            String email,
            String password) {
        this.email = email;
        this.password =password;
    }


}
