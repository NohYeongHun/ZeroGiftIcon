package com.zerogift.backend.pay.dto;

import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PayHisoryRequest {

    @ApiModelProperty(example = "아임포트 아이디값")
    @NotBlank(message = "impUid는 필수값입니다.")
    private String impUid;

    @ApiModelProperty(example = "상점 아이디 값")
    @NotBlank(message = "merchantUid는 필수값입니다.")
    private String merchantUid;

    @ApiModelProperty(example = "PG 아이디 값")
    @NotBlank(message = "pgProvider는 필수값입니다.")
    private String pgProvider;

    @ApiModelProperty(example = "PG 거래번호")
    @NotBlank(message = "pgTid는 필수값입니다.")
    private String pgTid;

    @ApiModelProperty(example = "사용 포인트")
    private Integer usePoint;

    @ApiModelProperty(example = "상품 아이디")
    @NotNull(message = "productId는 필수값입니다.")
    private Long productId;

    @ApiModelProperty(example = "선물 보낼 아이디")
    @NotNull(message = "sendId는 필수값입니다.")
    private Long sendId;

    @ApiModelProperty(example = "감사 메시지")
    @NotBlank(message = "message는 필수 값입니다.")
    private String message;

    @Builder
    public PayHisoryRequest(String impUid, String merchantUid, String pgProvider,
        String pgTid, Integer usePoint, Long productId, Long sendId, String message) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.pgProvider = pgProvider;
        this.pgTid = pgTid;
        this.usePoint = Objects.isNull(usePoint) ? 0 : usePoint;
        this.productId = productId;
        this.sendId = sendId;
        this.message = message;
    }

}
