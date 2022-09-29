package com.zerogift.backend.member.repository.condition;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSearchCondition {
    String email;
    String nickname;


    @Builder
    public MemberSearchCondition(
            String email,
            String nickname) {

        this.email = email;
        this.nickname = nickname;
    }

}
