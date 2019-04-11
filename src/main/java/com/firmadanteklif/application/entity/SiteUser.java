package com.firmadanteklif.application.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "site_users")
@EntityListeners(AuditingEntityListener.class)
public class SiteUser {

    @Id
    private UUID uuid;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Size(min = 8, max = 50)
    @NotEmpty(message = "Email .")
    @Column(nullable = false, unique = true)
    @Email(message = "Not valid email")
    private String email;

    private String password;

    @Transient
    private String confirmPassword;

    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
    }
}
