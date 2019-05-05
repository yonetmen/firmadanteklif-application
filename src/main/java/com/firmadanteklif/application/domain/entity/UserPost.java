package com.firmadanteklif.application.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_post")
@EntityListeners(AuditingEntityListener.class)
public class UserPost {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "post_id", length = 36)
    private UUID uuid;

    @CreatedBy
    private String ownerEmail;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Size.List({
            @Size(min = 5, message = "{post.header.size.min.message}"),
            @Size(max = 50, message = "{post.header.size.max.message}")
    })
    private String header;

    @Size.List({
            @Size(min = 5, message = "{post.body.size.min.message}"),
            @Size(max = 999, message = "{post.body.size.max.message}")
    })
    private String postBody;

    private boolean active;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "selected_city")
    private City city;

    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
        active = true;
    }
}
