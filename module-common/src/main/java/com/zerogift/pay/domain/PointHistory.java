package com.zerogift.pay.domain;

import com.zerogift.member.domain.Member;
import com.zerogift.support.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private Integer point;

    @Column
    private PointType pointType;

    @Column
    private Long pointTypeId; // 포인트 타입의 Id값 (review.getGiftBox().getId(), payHistoryId 등등)

    @Builder
    public PointHistory(Member member, Integer point, PointType pointType, Long pointTypeId) {
        this.member = member;
        this.point = point;
        this.pointType = pointType;
        this.pointTypeId = pointTypeId;
    }

}
