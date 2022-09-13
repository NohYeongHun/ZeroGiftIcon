package com.zerogift.backend.security.dto;

import static com.zerogift.backend.security.utils.JwtInfo.KEY_EMAIL;
import static com.zerogift.backend.security.utils.JwtInfo.KEY_ID;
import static com.zerogift.backend.security.utils.JwtInfo.KEY_NICKNAME;
import static com.zerogift.backend.security.utils.JwtInfo.KEY_ROLES;
import static com.zerogift.backend.security.utils.JwtInfo.KEY_STATUS;

import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.security.utils.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInfo implements LoginInfo {

    private Long id;
    private String role;

    private String nickname;
    private String status;
    private String email;

    @Builder
    public MemberInfo(
            Long id,
            String role,
            String nickname,
            String status,
            String email
    ){
        this.id = id;
        this.role = role;
        this.nickname = nickname;
        this.status = status;
        this.email = email;
    }

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
