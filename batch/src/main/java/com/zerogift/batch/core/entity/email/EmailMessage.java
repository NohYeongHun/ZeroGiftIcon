package com.zerogift.batch.core.entity.email;

import com.zerogift.batch.core.entity.base.BaseTimeEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public EmailMessage(String email, String message, MessageStatus status) {
        this.email = email;
        this.message = message;
        this.status = status;
        send = false;
    }

    public void successMailSend() {
        this.send = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailMessage that = (EmailMessage) o;
        return send == that.send && Objects.equals(id, that.id) && Objects.equals(
            email, that.email) && Objects.equals(message, that.message)
            && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, message, status, send);
    }
}
