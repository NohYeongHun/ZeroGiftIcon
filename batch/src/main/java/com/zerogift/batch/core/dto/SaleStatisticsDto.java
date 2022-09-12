package com.zerogift.batch.core.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class SaleStatisticsDto {

    private String thumbnail;
    private String productName;
    private Long count;
    private Long price;

    @Builder
    public SaleStatisticsDto(final String thumbnail,final String productName,final Long count,final Long price) {
        this.thumbnail = thumbnail;
        this.productName = productName;
        this.count = count;
        this.price = price;
    }

}
