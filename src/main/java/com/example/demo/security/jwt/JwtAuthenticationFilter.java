package com.example.demo.security.jwt;

import com.example.demo.common.dto.ErrorResultDto;
import com.example.demo.common.exception.JwtInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.security.jwt.JwtInfo.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		authenticate(request, getJwtFrom(request));

		filterChain.doFilter(request, response);
	}

	private void authenticate(HttpServletRequest request, String token) {
		if (StringUtils.hasText(token)) {
			try {
				JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
				Authentication authentication = authenticationManager.authenticate(
					jwtAuthenticationToken);

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (AuthenticationException e) {
				SecurityContextHolder.clearContext();

				JwtInvalidException jwtInvalidException = (JwtInvalidException) e;

				ErrorResultDto error = ErrorResultDto.builder()
					.errorCode(jwtInvalidException.getErrorCode().name())
					.errorDescription(jwtInvalidException.getErrorMessage())
					.build();

				request.setAttribute("errorResult", error);
			}
		}
	}

	private String getJwtFrom(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}

		return null;
	}


}
