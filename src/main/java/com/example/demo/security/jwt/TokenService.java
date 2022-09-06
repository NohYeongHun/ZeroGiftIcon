package com.example.demo.security.jwt;

import com.example.demo.dto.common.LoginInfo;
import com.example.demo.dto.common.MemberInfo;
import com.example.demo.exception.common.JwtInvalidException;
import com.example.demo.exception.common.code.JwtErrorCode;
import com.example.demo.persist.common.RefreshToken;
import com.example.demo.persist.common.repository.RefreshTokenRepository;
import com.example.demo.persist.member.Member;
import com.example.demo.persist.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

import static javax.management.timer.Timer.ONE_MINUTE;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    private final JwtInfo jwtInfo;

    public Tokens issueAllToken(LoginInfo loginInfo) {
        return Tokens.builder()
                .accessToken(issueAccessToken(loginInfo))
                .refreshToken(issueRefreshToken(loginInfo))
                .refreshTokenExpiredMin(jwtInfo.getRefreshTokenExpiredMin())
                .build();
    }

    private String issueAccessToken(LoginInfo loginInfo) {
        return createToken(loginInfo, jwtInfo.getEncodedAccessKey(),
                jwtInfo.getAccessTokenExpiredMin());
    }

    @Transactional
    private String issueRefreshToken(LoginInfo loginInfo) {
        String refreshToken = createToken(loginInfo, jwtInfo.getEncodedRefreshKey(),
                jwtInfo.getRefreshTokenExpiredMin());

        Member member = memberRepository.findById(loginInfo.getId())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(refreshToken)
                        .expiryDate(Duration.ofMinutes(jwtInfo.getRefreshTokenExpiredMin()))
                        .member(member)
                        .build()
        );

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

    public boolean existsRefreshToken(String token) {
        return refreshTokenRepository.existsByToken(token);
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음."));

        refreshTokenRepository.deleteByMember(member);
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

    public Tokens refresh(String refreshToken) {
        Claims claims = parseRefreshToken(refreshToken);

        String email = claims.get(JwtInfo.KEY_EMAIL, String.class);

        if (!memberRepository.existsByEmail(email)) {
            throw new JwtInvalidException(JwtErrorCode.EXPIRED_JWT);
        }

        return issueAllToken(MemberInfo.of(claims));
    }
}
