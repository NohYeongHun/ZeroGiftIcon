package com.zerogift.backend.security.service;

import com.zerogift.backend.security.dto.AdminInfo;
import com.zerogift.backend.common.exception.member.JwtInvalidException;
import com.zerogift.backend.security.dto.LoginInfo;
import com.zerogift.backend.security.dto.MemberInfo;
import com.zerogift.backend.common.exception.code.JwtErrorCode;
import com.zerogift.backend.security.dto.TokenDto;
import com.zerogift.backend.security.repository.RefreshTokenRepository;
import com.zerogift.backend.security.utils.JwtInfo;
import com.zerogift.backend.security.type.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

import static javax.management.timer.Timer.ONE_MINUTE;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtInfo jwtInfo;

    public TokenDto issueAllToken(LoginInfo loginInfo) {
        return TokenDto.builder()
                .accessToken(issueAccessToken(loginInfo))
                .refreshToken(issueRefreshToken(loginInfo))
                .refreshTokenExpiredMin(jwtInfo.getRefreshTokenExpiredMin())
                .build();
    }

    private String issueAccessToken(LoginInfo loginInfo) {
        return createToken(loginInfo, jwtInfo.getEncodedAccessKey(),
                jwtInfo.getAccessTokenExpiredMin());
    }

    private String issueRefreshToken(LoginInfo loginInfo) {
        String refreshToken = createToken(loginInfo, jwtInfo.getEncodedRefreshKey(),
                jwtInfo.getRefreshTokenExpiredMin());

        refreshTokenRepository.save(loginInfo.getEmail(), refreshToken, Duration.ofMinutes(
                jwtInfo.getRefreshTokenExpiredMin()));

        return refreshToken;
    }

    private String createToken(LoginInfo loginInfo, byte[] encodedSecretKey,int expiredMin) {
        Date now = new Date();

        Claims claims = loginInfo.toClaims();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ONE_MINUTE * expiredMin))
                .signWith(Keys.hmacShaKeyFor(encodedSecretKey))
                .compact();
    }

    public boolean existsRefreshToken(String username) {
        return refreshTokenRepository.existsByUsername(username);
    }
    public void deleteRefreshToken(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }



    public Claims parseAccessToken(String token) {
        return parseToken(token, jwtInfo.getEncodedAccessKey());
    }
    public Claims parseRefreshToken(String token) {
        return parseToken(token, jwtInfo.getEncodedRefreshKey());
    }

    private Claims parseToken(String token,byte[] encodedKey) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(encodedKey)
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            throw new JwtInvalidException(JwtErrorCode.INVALID_JWT_SIGNATURE, e);
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new JwtInvalidException(JwtErrorCode.INVALID_JWT_SIGNATURE, e);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            throw new JwtInvalidException(JwtErrorCode.EXPIRED_JWT, e);
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
            throw new JwtInvalidException(JwtErrorCode.INVALID_JWT_SIGNATURE, e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty or null: {}", e.getMessage());
            throw new JwtInvalidException(JwtErrorCode.INVALID_JWT_SIGNATURE, e);
        }
        return claims;
    }

    public TokenDto refresh(String refreshToken) {
        Claims claims = parseRefreshToken(refreshToken);

        String email = claims.get(JwtInfo.KEY_EMAIL, String.class);

        if (!refreshTokenRepository.existsByUsername(email)) {
            throw new JwtInvalidException(JwtErrorCode.EXPIRED_JWT);
        }

        String roles = claims.get(JwtInfo.KEY_ROLES, String.class);

        LoginInfo loginInfo;

        if (Role.ROLE_ADMIN.name().equals(roles)) {
            loginInfo = AdminInfo.of(claims);
        } else {
            loginInfo = MemberInfo.of(claims);
        }

        return issueAllToken(loginInfo);
    }
}
