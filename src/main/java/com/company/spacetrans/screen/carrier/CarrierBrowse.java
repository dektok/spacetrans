package com.company.spacetrans.screen.carrier;

import com.company.spacetrans.entity.Spaceport;
import io.jmix.ui.component.ButtonsPanel;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.spacetrans.entity.Carrier;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("st_Carrier.browse")
@UiDescriptor("carrier-browse.xml")
@LookupComponent("carriersTable")
public class CarrierBrowse extends StandardLookup<Carrier> {

    @Autowired
    private CollectionLoader<Carrier> carriersDl;

    @Autowired
    private ButtonsPanel buttonsPanel;

    @Subscribe
    public void onInit(InitEvent event) {
        MapScreenOptions options = (MapScreenOptions) event.getOptions();

        if (options.getParams().containsKey("ports")) {

            Spaceport port1 = (Spaceport) options.getParams().get("port1");
            Spaceport port2 = (Spaceport) options.getParams().get("port2");

            if (port1 != null && port2 != null) {
                carriersDl.setParameter("port1", port1);
                carriersDl.setParameter("port2", port2);
            }

            buttonsPanel.setVisible(false);
        }



    }

}