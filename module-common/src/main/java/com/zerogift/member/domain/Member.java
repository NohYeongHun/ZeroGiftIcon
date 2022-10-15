package com.zerogift.member.domain;

import com.zerogift.support.domain.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String authId;

    private String nickname;

    private String profileImageUrl;

    // 포인트 추가
    private Integer point;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(
        String email,
        String nickname,
        MemberStatus status,
        AuthType authType,
        String authId,
        String profileImageUrl,
        Role role
    ) {
        this.email = email;
        this.nickname = nickname;
        this.authId = authId;
        this.profileImageUrl = profileImageUrl;
        this.authType = authType;
        this.status = status;
        this.role = role;
        this.point = 0;
    }

    public void usePoint(Integer point) {
        this.point -= point;
    }

    public void addPassword(String password) {
        this.password = password;
    }

    public void mileagePoints(Integer point) {
        this.point += point;
    }

    public void emailVerifiedSuccess() {
        this.status = MemberStatus.PERMITTED;
    }

}
