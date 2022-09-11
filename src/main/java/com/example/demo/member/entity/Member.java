package com.example.demo.member.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import com.example.demo.common.type.AuthType;
import com.example.demo.member.type.MemberStatus;
import com.example.demo.security.type.Role;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String authId;

    private String nickname;

    private String profileImageUrl;

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
    ){
        this.email = email;
        this.nickname = nickname;
        this.authId = authId;
        this.profileImageUrl = profileImageUrl;
        this.authType = authType;
        this.status = status;
        this.role = role;
    }
}
