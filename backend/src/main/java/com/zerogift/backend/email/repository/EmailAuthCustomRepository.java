package com.zerogift.backend.email.repository;

import com.zerogift.backend.email.entity.EmailAuth;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthCustomRepository {
    Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime);
}
