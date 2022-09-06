package com.example.demo.dto.common;

import io.jsonwebtoken.Claims;

public interface LoginInfo {
    Long getId();
    String getRole();

    String getEmail();

    Claims toClaims();
}
