package com.firmadanteklif.application.entity;

import com.firmadanteklif.application.entity.enums.VerificationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "verification_codes")
@EntityListeners(AuditingEntityListener.class)
public class VerificationCode {

    @Id
    private UUID uuid;

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
    }

}
