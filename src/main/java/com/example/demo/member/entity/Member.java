package com.example.demo.member.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import com.example.demo.member.type.MemberStatus;
import com.example.demo.security.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;
}
