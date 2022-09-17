package com.zerogift.backend.email.repository;


import static com.zerogift.backend.email.entity.QEmailAuth.emailAuth;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.backend.email.entity.EmailAuth;
import com.zerogift.backend.email.entity.QEmailAuth;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;


public class EmailAuthCustomRepositoryImpl implements EmailAuthCustomRepository {

    JPAQueryFactory jpaQueryFactory;

    public EmailAuthCustomRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime) {
        EmailAuth auth = jpaQueryFactory
                .selectFrom(emailAuth)
                .where(emailAuth.email.eq(email),
                        QEmailAuth.emailAuth.authToken.eq(authToken),
                        QEmailAuth.emailAuth.expireDate.goe(currentTime),
                        QEmailAuth.emailAuth.expired.eq(false))
                .fetchFirst();

        return Optional.ofNullable(auth);
    }
}
