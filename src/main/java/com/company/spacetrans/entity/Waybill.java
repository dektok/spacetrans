package com.company.spacetrans.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "ST_WAYBILL", indexes = {
        @Index(name = "IDX_ST_WAYBILL_CREATOR", columnList = "CREATOR_ID"),
        @Index(name = "IDX_ST_WAYBILL_SHIPPER", columnList = "SHIPPER_ID"),
        @Index(name = "IDX_ST_WAYBILL_CONSIGNEE", columnList = "CONSIGNEE_ID"),
        @Index(name = "IDX_ST_WAYBILL_DEPARTURE_PORT", columnList = "DEPARTURE_PORT_ID"),
        @Index(name = "IDX_STWAYBILL_DESTINATIONPORT", columnList = "DESTINATION_PORT_ID"),
        @Index(name = "IDX_ST_WAYBILL_CARRIER", columnList = "CARRIER_ID")
})
@Entity(name = "st_Waybill")
public class Waybill {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "REFERENCE")
    private String reference;

    @JoinColumn(name = "CREATOR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @JoinColumn(name = "SHIPPER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer shipper;

    @JoinColumn(name = "CONSIGNEE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer consignee;

    @JoinColumn(name = "DEPARTURE_PORT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Spaceport departurePort;

    @JoinColumn(name = "DESTINATION_PORT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Spaceport destinationPort;

    @JoinColumn(name = "CARRIER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Carrier carrier;

    @Composition
    @OneToMany(mappedBy = "waybill")
    private List<WaybillItem> items;

    @Column(name = "TOTAL_WEIGHT")
    private Double totalWeight;

    @Column(name = "TOTAL_CHARGE", precision = 19, scale = 2)
    private BigDecimal totalCharge;

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<WaybillItem> getItems() {
        return items;
    }

    public void setItems(List<WaybillItem> items) {
        this.items = items;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public Spaceport getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(Spaceport destinationPort) {
        this.destinationPort = destinationPort;
    }

    public Spaceport getDeparturePort() {
        return departurePort;
    }

    public void setDeparturePort(Spaceport departurePort) {
        this.departurePort = departurePort;
    }

    public Customer getConsignee() {
        return consignee;
    }

    public void setConsignee(Customer consignee) {
        this.consignee = consignee;
    }

    public Customer getShipper() {
        return shipper;
    }

    public void setShipper(Customer shipper) {
        this.shipper = shipper;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public Integer getNextItemNumber() {

        if (getItems() == null) return 1;

        return getItems().size()+1;
    }

    public void RenumberItems() {

        Integer nextNumber = 1;

        items.sort((i1,i2)-> i1.getNumber().compareTo(i2.getNumber()));
        for (WaybillItem item : items) {
            item.setNumber(nextNumber++);
        }
    }

    public void ItemUp (WaybillItem item) {
        if (item == null) return;

        int previousNumber = item.getNumber() - 1;

        if (previousNumber <= 0) return;

        items.sort((i1,i2)-> i1.getNumber().compareTo(i2.getNumber()));
        for (WaybillItem changeitem : items) {
            if (changeitem.getNumber() < previousNumber) continue;
            if (changeitem.getNumber().equals(previousNumber)) {
                item.setNumber(previousNumber);
                changeitem.setNumber(previousNumber+1);
                break;
            }
        }

    }

    public void ItemDown (WaybillItem item) {
        if (item == null) return;
        int nextNumber = item.getNumber() + 1;

        if (nextNumber > items.size()) return;

        items.sort((i1,i2)-> i1.getNumber().compareTo(i2.getNumber()));
        for (WaybillItem changeitem : items) {
            if (changeitem.getNumber() <= item.getNumber()) continue;
            if (changeitem.getNumber().equals(nextNumber)) {
                item.setNumber(nextNumber);
                changeitem.setNumber(nextNumber-1);
                break;
            }
        }
    }
}