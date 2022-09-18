package com.company.spacetrans.entity;

import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JmixEntity
@Table(name = "ST_INDIVIDUAL")
@Entity(name = "st_Individual")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class Individual extends Customer {

    @NotEmpty(message = "{msg://com.company.spacetrans.entity/Individual.firstName.validation.NotEmpty}")
    @NotNull
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}