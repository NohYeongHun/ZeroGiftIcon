package com.zerogift.support.auth.userdetails;

import io.jsonwebtoken.Claims;

public interface LoginInfo {
    Long getId();

    String getRole();

    String getEmail();

    Claims toClaims();
}
