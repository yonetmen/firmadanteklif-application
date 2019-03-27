package com.firmadanteklif.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "home_page")
@Data
@AllArgsConstructor
public class HomePage {

    @Id
    private int id;
    private String name;

}
