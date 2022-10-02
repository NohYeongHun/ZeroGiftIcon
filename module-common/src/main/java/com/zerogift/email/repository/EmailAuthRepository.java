package com.zerogift.email.repository;

import com.zerogift.email.domain.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long>,
    EmailAuthCustomRepository {
}
