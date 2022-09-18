package com.company.spacetrans.listener;

import com.company.spacetrans.app.Calculators;
import com.company.spacetrans.entity.Waybill;
import com.company.spacetrans.entity.WaybillItem;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.event.EntityChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("st_WaybillItemEventListener")
public class WaybillItemEventListener {

    @Autowired
    private Calculators calculators;

    @Autowired
    private DataManager dataManager;

    @EventListener
    public void onWaybillItemChangedBeforeCommit(EntityChangedEvent<WaybillItem> event) {

        Waybill waybill;

        if (event.getType() == EntityChangedEvent.Type.DELETED) {
            Id<Waybill> waybillId = event.getChanges().getOldReferenceId("waybill");
            try {
                waybill = dataManager.load(waybillId).one();
            } catch (Exception e) {
                waybill = null;
            }
        } else {
            try {
                WaybillItem item = dataManager.load(event.getEntityId()).one();
                waybill = item.getWaybill();
            } catch (Exception e) {
                waybill = null;
            }
        }

        if (waybill == null) return;

        calculators.CalculateWaybillSums(waybill);
        dataManager.save(waybill);
    }
}