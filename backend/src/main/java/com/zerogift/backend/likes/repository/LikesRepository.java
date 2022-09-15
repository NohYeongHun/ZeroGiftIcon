package com.zerogift.backend.likes.repository;

import com.zerogift.backend.likes.entity.Likes;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    long countByProduct(Product product);

    long countByMemberAndProduct(Member member, Product product);

    boolean existsByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);

    List<Likes> findByMember(Member member);

    Optional<Likes> findByMemberAndProduct(Member member, Product product);
}
