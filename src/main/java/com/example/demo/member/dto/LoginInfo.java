package com.example.demo.member.dto;

import io.jsonwebtoken.Claims;

public interface LoginInfo {
    Long getId();
    String getRole();

    String getEmail();

    Claims toClaims();
}
