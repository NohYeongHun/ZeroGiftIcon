package com.zerogift.backend.member.dto;


import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchDetail {
    private Long id;
    private String nickname;
    private String email;
    private Integer point;

    @Builder
    public MemberSearchDetail(
        Long id,
        String nickname,
        String email,
        Integer point
    )
    {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.point = point;
    }
}
