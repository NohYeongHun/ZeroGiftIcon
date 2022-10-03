package com.zerogift.email.repository;

import com.zerogift.email.domain.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {
}
