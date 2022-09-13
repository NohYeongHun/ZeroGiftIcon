package com.zerogift.batch.core.entity.product;

import com.zerogift.batch.core.entity.base.BaseTimeEntity;
import com.zerogift.batch.core.entity.member.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer count;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Integer viewCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String mainImageUrl;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Product(String name, String description, Integer price, Integer count,
        Category category, ProductStatus status, Member member) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.count = count;
        this.category = category;
        this.status = status;
        this.member = member;
        this.viewCount = 0;
    }
}
