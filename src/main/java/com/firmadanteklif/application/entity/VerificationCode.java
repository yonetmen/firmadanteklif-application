package com.firmadanteklif.application.entity;

import com.firmadanteklif.application.entity.enums.VerificationEntity;
import com.firmadanteklif.application.entity.enums.VerificationType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_codes")
public class VerificationCode extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    @Enumerated(EnumType.STRING)
    private VerificationEntity verificationEntity;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

}
