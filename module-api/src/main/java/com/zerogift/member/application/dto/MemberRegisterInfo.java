package com.zerogift.member.application.dto;

import com.zerogift.member.domain.AuthType;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.MemberStatus;
import com.zerogift.member.domain.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegisterInfo {


    private String email;
    private String nickname;
    private String password;
    private MemberStatus status;
    private AuthType authType;
    private Role role;

    @Builder
    public MemberRegisterInfo(
            String email,
            String password,
            String nickname,
            MemberStatus status,
            AuthType authType,
            Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.authType = authType;
        this.role = role;
    }

    public static MemberRegisterInfo from(Member member){

        return MemberRegisterInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .status(member.getStatus())
                .authType(member.getAuthType())
                .role(member.getRole())
                .build();
    }
}
