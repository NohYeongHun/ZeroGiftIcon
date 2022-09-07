package com.example.demo.security.repository;

import com.example.demo.common.entity.RefreshToken;
import com.example.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByMember(Member member);
}
