package com.company.spacetrans.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "ST_CARRIER", indexes = {
        @Index(name = "IDX_ST_CARRIER_UNQ_NAME", columnList = "NAME", unique = true)
})
@Entity(name = "st_Carrier")
public class Carrier {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotEmpty(message = "{msg://com.company.spacetrans.entity/Carrier.name.validation.NotEmpty}")
    @NotNull
    @InstanceName
    @Column(name = "NAME", nullable = false)
    private String name;

    @JoinTable(name = "ST_CARRIER_SPACEPORT_LINK",
            joinColumns = @JoinColumn(name = "CARRIER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "SPACEPORT_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<Spaceport> ports;

    public List<Spaceport> getPorts() {
        return ports;
    }

    public void setPorts(List<Spaceport> ports) {
        this.ports = ports;
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