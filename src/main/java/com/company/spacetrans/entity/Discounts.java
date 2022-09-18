package com.company.spacetrans.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@JmixEntity
@Table(name = "ST_DISCOUNTS", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_ST_DISCOUNTS_UNQ_GRADE", columnNames = {"GRADE"})
})
@Entity(name = "st_Discounts")
public class Discounts {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @Column(name = "GRADE")
    private Integer grade;

    @NumberFormat(pattern = "0.00%")
    @Positive(message = "{msg://com.company.spacetrans.entity/Discounts.value.validation.Positive}")
    @Column(name = "VALUE_", precision = 19, scale = 2)
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public CustomerGrade getGrade() {
        return grade == null ? null : CustomerGrade.fromId(grade);
    }

    public void setGrade(CustomerGrade grade) {
        this.grade = grade == null ? null : grade.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}