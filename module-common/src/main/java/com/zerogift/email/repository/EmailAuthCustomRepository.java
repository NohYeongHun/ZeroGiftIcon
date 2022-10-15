package com.zerogift.email.repository;

import com.zerogift.email.domain.EmailAuth;
import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthCustomRepository {
    Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime);
}
