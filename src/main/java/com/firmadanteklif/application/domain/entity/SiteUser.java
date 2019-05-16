package com.firmadanteklif.application.domain.entity;

import com.firmadanteklif.application.validator.ValidEmail;
import com.firmadanteklif.application.domain.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "site_users", indexes = {
        @Index(columnList = "email", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class SiteUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Type(type = "uuid-char")
    @Column(name = "user_id", length = 36)
    private UUID uuid;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ValidEmail(message = "{email.not.unique}")
    @Column(name = "email", length = 50)
    private String email;

    @Size.List({
            @Size(min = 6, message = "{password.size.min.message}"),
            @Size(max = 100, message = "{password.size.max.message}")
    })
    @Column(length = 50, nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "active")
    private boolean active;

    @Column(name = "role", length = 12)
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
        active = false;
        role = Role.ROLE_USER;
    }
}
