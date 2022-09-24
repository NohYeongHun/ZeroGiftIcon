package com.zerogift.backend.member.model;

import com.zerogift.backend.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberReviewResponse {

    private long id;
    private String nickname;
    private String profileImageUrl;

    public static MemberReviewResponse of(Member member) {
        return MemberReviewResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
