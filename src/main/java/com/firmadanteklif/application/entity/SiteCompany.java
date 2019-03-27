package com.firmadanteklif.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
}
