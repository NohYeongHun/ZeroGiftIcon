package com.example.demo.common.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import com.example.demo.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "refresh")
public class RefreshToken extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Duration expiryDate;


}
