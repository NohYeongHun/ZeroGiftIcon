package com.zerogift.backend.member.dto;

import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.member.repository.condition.MemberSearchCondition;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchMember {
    private String email;
    private String nickname;


    @Builder
    public SearchMember(
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
