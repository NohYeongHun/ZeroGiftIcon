package com.zerogift.backend.security.dto;

import static com.zerogift.backend.security.utils.JwtInfo.KEY_ID;
import static com.zerogift.backend.security.utils.JwtInfo.KEY_EMAIL;
import static com.zerogift.backend.security.utils.JwtInfo.KEY_ROLES;

import com.zerogift.backend.security.utils.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminInfo implements LoginInfo{

    private Long id;
    private String email;
    private String role;

    @Builder
    public AdminInfo(Long id, String email, String role)
    {
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
                .id(claims.get(JwtInfo.KEY_ID, Long.class))
                .email(claims.get(JwtInfo.KEY_EMAIL, String.class))
                .role(claims.get(JwtInfo.KEY_ROLES,String.class))
                .build();
    }
}
