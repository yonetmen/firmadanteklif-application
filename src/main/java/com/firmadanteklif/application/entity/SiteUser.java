package com.firmadanteklif.application.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "site_users")
@EqualsAndHashCode(callSuper = true)
public class SiteUser extends BaseEntity {

    @Size(min = 8, max = 50)
    @NotEmpty(message = "Email .")
    @Column(nullable = false, unique = true)
    @Email(message = "Not valid email")
    private String email;


    private String password;
    @Transient
    private String confirmPassword;
}
