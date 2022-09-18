package com.company.spacetrans.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "ST_CUSTOMER", indexes = {
        @Index(name = "IDX_ST_CUSTOMER_UNQ_NAME", columnList = "NAME", unique = true)
})
@Entity(name = "st_Customer")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotEmpty(message = "{msg://com.company.spacetrans.entity/Customer.name.validation.NotEmpty}")
    @NotNull
    @InstanceName
    @Column(name = "NAME", nullable = false)
    private String name;

    @Email(message = "{msg://com.company.spacetrans.entity/Customer.email.validation.Email}")
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GRADE")
    private Integer grade;

    public CustomerGrade getGrade() {
        return grade == null ? null : CustomerGrade.fromId(grade);
    }

    public void setGrade(CustomerGrade grade) {
        this.grade = grade == null ? null : grade.getId();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}