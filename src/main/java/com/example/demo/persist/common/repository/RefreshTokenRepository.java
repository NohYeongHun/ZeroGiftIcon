package com.example.demo.persist.common.repository;

import com.example.demo.persist.common.RefreshToken;
import com.example.demo.persist.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByMember(Member member);
}
