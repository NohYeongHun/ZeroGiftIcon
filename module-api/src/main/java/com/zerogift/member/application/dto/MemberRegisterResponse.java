package com.zerogift.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRegisterResponse {

    private String email;
    private String nickname;
    @Builder
    public MemberRegisterResponse(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
    }

    public static MemberRegisterResponse from(MemberRegisterInfo info){

        return MemberRegisterResponse.builder()
                .email(info.getEmail())
                .nickname(info.getNickname())
                .build();
    }
}
