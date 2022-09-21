package com.zerogift.backend.product.entity;

import java.time.LocalDateTime;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.type.Category;
import com.zerogift.backend.product.type.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Builder.Default
    private Long viewCount = 0L;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String mainImageUrl;
    @Lob
    @Builder.Default
    private HashSet<Long> liked = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Builder.Default
    private Long likeCount = 0L;

    public void plusViewCount() {
        this.viewCount += 1;
    }
}
