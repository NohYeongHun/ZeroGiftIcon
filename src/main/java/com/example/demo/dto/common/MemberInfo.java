package com.example.demo.dto.common;

import static com.example.demo.security.jwt.JwtInfo.KEY_EMAIL;
import static com.example.demo.security.jwt.JwtInfo.KEY_ID;
import static com.example.demo.security.jwt.JwtInfo.KEY_NICKNAME;
import static com.example.demo.security.jwt.JwtInfo.KEY_ROLES;
import static com.example.demo.security.jwt.JwtInfo.KEY_STATUS;

import com.example.demo.persist.member.Member;
import com.example.demo.security.jwt.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberInfo implements LoginInfo{

    private Long id;
    private String role;

    private String nickname;
    private String status;
    private String email;

    @Override
    public Claims toClaims() {
        Claims claims = Jwts.claims();

        claims.put(KEY_ID, this.id);
        claims.put(KEY_EMAIL, this.email);
        claims.put(KEY_NICKNAME, this.nickname);
        claims.put(KEY_STATUS, this.status);
        claims.put(KEY_ROLES, this.role);

        return claims;
    }

    public static MemberInfo of(Claims claims) {
        return MemberInfo.builder()
                .id(claims.get(JwtInfo.KEY_ID, Long.class))
                .email(claims.get(JwtInfo.KEY_EMAIL, String.class))
                .role(claims.get(JwtInfo.KEY_ROLES,String.class))
                .status(claims.get(JwtInfo.KEY_STATUS,String.class))
                .nickname(claims.get(JwtInfo.KEY_NICKNAME,String.class))
                .build();
    }

    public static MemberInfo of(Member member) {
        return MemberInfo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole().name())
                .status(member.getStatus().name())
                .nickname(member.getNickname())
                .build();
    }
}
