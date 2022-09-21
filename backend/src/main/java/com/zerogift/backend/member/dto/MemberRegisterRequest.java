package com.zerogift.backend.member.dto;

import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.member.type.MemberStatus;
import com.zerogift.backend.security.type.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegisterRequest {

    private String email;
    private String password;
    private String nickname;

    @Builder
    public MemberRegisterRequest(
            String email,
            String password,
            String nickname
    ){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }


}
