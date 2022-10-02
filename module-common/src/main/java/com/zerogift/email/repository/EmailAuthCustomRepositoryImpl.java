package com.zerogift.email.repository;

import static com.zerogift.email.domain.QEmailAuth.emailAuth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.email.domain.EmailAuth;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;

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
                emailAuth.authToken.eq(authToken),
                emailAuth.expireDate.goe(currentTime),
                emailAuth.expired.eq(false))
            .fetchFirst();

        return Optional.ofNullable(auth);
    }

}
