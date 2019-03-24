package com.firmadanteklif.application.entity;

import com.firmadanteklif.application.entity.enums.VerificationType;

import javax.persistence.*;

@Entity
@Table(name = "site_companies")
public class SiteCompany extends BaseEntity {

    @Column(name = "email")
    private String email;
    private String password;
    @Transient
    private String confirmPassword;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;
}
