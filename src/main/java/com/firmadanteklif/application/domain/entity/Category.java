package com.firmadanteklif.application.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @Column(name = "category_id")
    private int id;
    @Column(name = "name")
    private String name;
}
