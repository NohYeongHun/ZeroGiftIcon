package com.zerogift.backend.common.exception.member;

import com.zerogift.backend.common.exception.code.JwtErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtInvalidException extends AuthenticationException {
    private final JwtErrorCode errorCode;
    private final String errorMessage;

    public JwtInvalidException(JwtErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    public JwtInvalidException(JwtErrorCode errorCode,Throwable cause) {
        super(errorCode.getDescription(),cause);
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
