package com.firmadanteklif.application.entity;

import com.firmadanteklif.application.entity.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
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

    @Size.List({
            @Size(min = 5, message = "{email.size.min.message}"),
            @Size(max = 50, message = "{email.size.max.message}")
    })
    @Email(message = "{email.format.message}")
    @Column(length = 50)
    private String email;

    @Size.List({
            @Size(min = 6, message = "{password.size.min.message}"),
            @Size(max = 100, message = "{password.size.max.message}")
    })
    @Column(length = 100, nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    private boolean active;

    @Column(name = "role", length = 12)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String address;

    private String phoneNumber;

    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
    }
}
