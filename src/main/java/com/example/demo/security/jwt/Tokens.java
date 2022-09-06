package com.example.demo.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tokens{
    private String accessToken;
    private String refreshToken;
    private int refreshTokenExpiredMin;
}
