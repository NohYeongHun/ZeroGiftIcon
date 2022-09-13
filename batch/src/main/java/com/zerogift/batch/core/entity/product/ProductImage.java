package com.zerogift.batch.core.entity.product;

import com.zerogift.batch.core.entity.base.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private Boolean thumbnail;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ProductImage(String url, boolean thumbnail,
        Product product) {
        this.url = url;
        this.thumbnail = thumbnail;
        this.product = product;
    }
}
