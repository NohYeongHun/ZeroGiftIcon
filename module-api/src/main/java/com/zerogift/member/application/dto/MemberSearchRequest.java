package com.zerogift.member.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchRequest {
    private String email;
    private String nickname;

    @Builder
    public MemberSearchRequest(
        String email,
        String nickname
    ){
        this.email = email;
        this.nickname = nickname;
    }

    public MemberSearchCondition toCondition(){
        return MemberSearchCondition.builder()
            .email(getEmail())
            .nickname(getNickname())
            .build();
    }

}
