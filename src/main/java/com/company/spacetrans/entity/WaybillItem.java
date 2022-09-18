package com.company.spacetrans.entity;

import io.jmix.core.entity.annotation.EmbeddedParameters;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

@JmixEntity
@Table(name = "ST_WAYBILL_ITEM", indexes = {
        @Index(name = "IDX_ST_WAYBILL_ITEM_WAYBILL", columnList = "WAYBILL_ID"),
        @Index(name = "IDX_ST_WAYBILL_ITEM_UNQ_NAME", columnList = "NAME, WAYBILL_ID", unique = true)
})
@Entity(name = "st_WaybillItem")
public class WaybillItem {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @Column(name = "NUMBER_", nullable = false)
    private Integer number;

    @NotEmpty(message = "{msg://com.company.spacetrans.entity/WaybillItem.name.validation.NotEmpty}")
    @NotNull
    @InstanceName
    @Column(name = "NAME", nullable = false)
    private String name;

    @PositiveOrZero(message = "{msg://com.company.spacetrans.entity/WaybillItem.weigth.validation.PositiveOrZero}")
    @Column(name = "WEIGTH")
    private Double weigth;

    @EmbeddedParameters(nullAllowed = false)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "length", column = @Column(name = "DIM_LENGTH")),
            @AttributeOverride(name = "width", column = @Column(name = "DIM_WIDTH")),
            @AttributeOverride(name = "height", column = @Column(name = "DIM_HEIGHT"))
    })
    private Dimensions dim;

    @Column(name = "CHARGE", precision = 19, scale = 2)
    private BigDecimal charge;

    @JoinColumn(name = "WAYBILL_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Waybill waybill;

    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public Dimensions getDim() {
        return dim;
    }

    public void setDim(Dimensions dim) {
        this.dim = dim;
    }

    public Double getWeigth() {

        if (weigth == null) return Double.valueOf(0);

        return weigth;
    }

    public void setWeigth(Double weigth) {
        this.weigth = weigth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void CalculateCharge() {

        BigDecimal charge = BigDecimal.valueOf(0);

        if (dim == null || weigth == null){
            setCharge(charge);
            return;
        }

        if ( getWeigth().equals(0) ||
             dim.getLength().equals(0) ||
             dim.getHeight().equals(0) ||
             dim.getWidth().equals(0)) {

            setCharge(charge);
            return;
        }

        charge = BigDecimal.valueOf(dim.getLength())
                .multiply(BigDecimal.valueOf(dim.getHeight()))
                .multiply(BigDecimal.valueOf(dim.getWidth()))
                .multiply(BigDecimal.valueOf(weigth));

        setCharge(charge);
    }
}