package com.zerogift.member.application.dto;

import com.zerogift.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberLikeResponse {

    private long id;
    private String email;
    private String nickname;
    private String profileImageUrl;

    public static MemberLikeResponse of(Member member) {
        return MemberLikeResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImageUrl(member.getProfileImageUrl())
            .build();
    }
}
