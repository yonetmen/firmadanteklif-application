package com.firmadanteklif.application.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "site_companies")
@EntityListeners(AuditingEntityListener.class)
public class SiteCompany {

    @Id
    private UUID uuid;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Column(name = "email")
    private String email;

    private String password;

    @Transient
    private String confirmPassword;

    private String address;

    private String phoneNumber;

    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
    }
}
