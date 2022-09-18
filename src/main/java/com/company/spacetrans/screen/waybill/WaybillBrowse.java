package com.company.spacetrans.screen.waybill;

import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.spacetrans.entity.Waybill;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("st_Waybill.browse")
@UiDescriptor("waybill-browse.xml")
@LookupComponent("waybillsTable")
public class WaybillBrowse extends StandardLookup<Waybill> {
    @Autowired
    private CollectionLoader<Waybill> waybillsDl;

    @Install(to = "waybillsTable.create", subject = "afterCommitHandler")
    private void waybillsTableCreateAfterCommitHandler(Waybill waybill) {
        waybillsDl.load();
    }

    @Install(to = "waybillsTable.edit", subject = "afterCommitHandler")
    private void waybillsTableEditAfterCommitHandler(Waybill waybill) {
        waybillsDl.load();
    }

}