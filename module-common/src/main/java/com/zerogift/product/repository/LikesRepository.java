package com.zerogift.product.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.product.application.dto.LikesModel;
import com.zerogift.product.domain.Likes;
import com.zerogift.product.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    long countByProduct(Product product);

    boolean existsByMemberAndProduct(Member member, Product product);

    List<LikesModel> findByMember(Member member);

    Optional<Likes> findByMemberAndProduct(Member member, Product product);
}
