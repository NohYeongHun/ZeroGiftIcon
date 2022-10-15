package com.zerogift.support.auth.userdetails;

import static com.zerogift.support.auth.token.JwtInfo.KEY_EMAIL;
import static com.zerogift.support.auth.token.JwtInfo.KEY_ID;
import static com.zerogift.support.auth.token.JwtInfo.KEY_ROLES;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminInfo implements LoginInfo {

    private Long id;
    private String email;
    private String role;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Builder
    public AdminInfo(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    @Override
    public Claims toClaims() {
        Claims claims = Jwts.claims();

        claims.put(KEY_ID, this.id);
        claims.put(KEY_EMAIL, this.email);
        claims.put(KEY_ROLES, this.role);

        return claims;
    }

    public static AdminInfo of(Claims claims) {
        return AdminInfo.builder()
            .id(claims.get(KEY_ID, Long.class))
            .email(claims.get(KEY_EMAIL, String.class))
            .role(claims.get(KEY_ROLES,String.class))
            .build();
    }
}
