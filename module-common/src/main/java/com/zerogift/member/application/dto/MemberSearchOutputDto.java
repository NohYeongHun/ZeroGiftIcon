package com.zerogift.member.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchOutputDto {

    private Long id;
    private String profileImageUrl;
    private String nickname;

    @Builder
    public MemberSearchOutputDto(Long id, String profileImageUrl, String nickname) {
        this.id = id;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
    }

}
