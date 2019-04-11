package com.firmadanteklif.application.entity;

import com.firmadanteklif.application.entity.enums.Role;
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

    @Size(min = 8, max = 50, message = "email.length")
    @NotEmpty(message = "email.not.empty")
    @Column(nullable = false, unique = true)
    @Email(message = "email.format.error")
    private String email;

    @NotEmpty(message = "password.not.empty")
    @Column(length = 100, nullable = false)
    @Size(min = 6, max = 100, message = "password.length")
    private String password;

    @Transient
    @NotEmpty(message = "password.not.empty")
    @Column(length = 100, nullable = false)
    @Size(min = 6, max = 100, message = "password.length")
    private String confirmPassword;

    private boolean active;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
        active = false;
        role = Role.ROLE_USER;
    }
}
