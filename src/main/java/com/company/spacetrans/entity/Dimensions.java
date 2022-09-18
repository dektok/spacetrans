package com.company.spacetrans.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.PositiveOrZero;

@JmixEntity(name = "st_Dimensions")
@Embeddable
public class Dimensions {
    @PositiveOrZero(message = "{msg://com.company.spacetrans.entity/Dimensions.length.validation.PositiveOrZero}")
    @Column(name = "LENGTH")
    private Double length;

    @PositiveOrZero(message = "{msg://com.company.spacetrans.entity/Dimensions.width.validation.PositiveOrZero}")
    @Column(name = "WIDTH")
    private Double width;

    @PositiveOrZero(message = "{msg://com.company.spacetrans.entity/Dimensions.height.validation.PositiveOrZero}")
    @Column(name = "HEIGHT")
    private Double height;

    public Double getHeight() {

        if (height == null) return Double.valueOf(0);

        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {

        if (width == null) return Double.valueOf(0);

        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {

        if (length == null) return Double.valueOf(0);

        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}