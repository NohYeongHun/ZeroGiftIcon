package com.zerogift.member.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSearchCondition {
    private String email;
    private String nickname;

    @Builder
    public MemberSearchCondition(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
