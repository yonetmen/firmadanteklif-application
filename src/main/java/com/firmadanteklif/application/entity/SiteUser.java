package com.firmadanteklif.application.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@Entity
@Table(name = "site_users")
@EqualsAndHashCode(callSuper = true)
public class SiteUser extends BaseEntity {

    private String email;
    private String password;
    @Transient
    private String confirmPassword;
}
