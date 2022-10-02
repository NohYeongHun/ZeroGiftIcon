package com.zerogift.support.auth.authentication.provider;

import static com.zerogift.global.error.code.JwtErrorCode.MEMBER_STATUS_BANNED;
import static com.zerogift.global.error.code.JwtErrorCode.MEMBER_STATUS_WAIT;
import static com.zerogift.global.error.code.JwtErrorCode.MEMBER_STATUS_WITHDRAWAL;
import static com.zerogift.support.auth.token.JwtInfo.KEY_EMAIL;
import static com.zerogift.support.auth.token.JwtInfo.KEY_ROLES;

import com.zerogift.global.error.exception.JwtInvalidException;
import com.zerogift.member.application.GeneralService;
import com.zerogift.member.domain.Member;
import com.zerogift.support.auth.authentication.JwtAuthenticationToken;
import com.zerogift.support.auth.oauth.application.TokenService;
import com.zerogift.support.auth.userdetails.AdminInfo;
import com.zerogift.support.auth.userdetails.MemberInfo;
import io.jsonwebtoken.Claims;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;
    private final GeneralService generalService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        Claims claims =
            tokenService.parseAccessToken(((JwtAuthenticationToken) authentication).getJwt());

        if (claims.get(KEY_ROLES,String.class).equals("ROLE_ADMIN")) {

            AdminInfo info = AdminInfo.of(claims);

            return new JwtAuthenticationToken(AdminInfo.of(claims), "",
                Arrays.asList(new SimpleGrantedAuthority(info.getRole())));
        }

        Member member = generalService.findByEmail(claims.get(KEY_EMAIL, String.class));
        MemberInfo info = MemberInfo.of(member);

        switch (info.getStatus()) {
            case "BANNED":
                throw new JwtInvalidException(MEMBER_STATUS_BANNED);
            case "WITHDRAWAL":
                throw new JwtInvalidException(MEMBER_STATUS_WITHDRAWAL);
            case "WAIT":
                throw new JwtInvalidException(MEMBER_STATUS_WAIT);
            default:
                return new JwtAuthenticationToken(info, "",
                    Arrays.asList(new SimpleGrantedAuthority(info.getRole())));
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
