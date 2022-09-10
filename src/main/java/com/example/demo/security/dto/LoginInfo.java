package com.example.demo.security.dto;

import io.jsonwebtoken.Claims;

public interface LoginInfo {
    Long getId();
    String getRole();

    String getEmail();

    Claims toClaims();
}
