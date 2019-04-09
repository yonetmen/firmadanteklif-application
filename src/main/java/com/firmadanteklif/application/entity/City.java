package com.firmadanteklif.application.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cities")
@Data
public class City {

    @Id
    @Column(name = "city_id")
    private int id;
    @Column(name = "name")
    private String name;

}
