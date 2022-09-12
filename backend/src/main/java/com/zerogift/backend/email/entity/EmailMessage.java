package com.zerogift.backend.email.entity;

import com.zerogift.backend.common.entity.BaseTimeEntity;
import com.zerogift.backend.email.type.MessageStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Lob
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(nullable = false)
    private boolean send;

}