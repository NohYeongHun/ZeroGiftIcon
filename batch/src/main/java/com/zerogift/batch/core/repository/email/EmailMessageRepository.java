package com.zerogift.batch.core.repository.email;

import com.zerogift.batch.core.entity.email.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {
}
