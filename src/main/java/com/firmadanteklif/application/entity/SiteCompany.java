package com.firmadanteklif.application.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "site_companies")
public class SiteCompany extends BaseEntity {

    @Column(name = "email")
    private String email;
    private String password;
    @Transient
    private String confirmPassword;
    private String address;
    private String phoneNumber;
}
