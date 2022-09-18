package com.company.spacetrans.screen.spaceport;

import com.company.spacetrans.entity.Moon;
import com.company.spacetrans.entity.Planet;
import io.jmix.ui.Notifications;
import io.jmix.ui.Screens;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.spacetrans.entity.Spaceport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@UiController("st_Spaceport.edit")
@UiDescriptor("spaceport-edit.xml")
@EditedEntityContainer("spaceportDc")
public class SpaceportEdit extends StandardEditor<Spaceport> {

    @Autowired
    private Notifications notifications;
    @Autowired
    private MessageBundle messageBundle;

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {

        Moon moon = getEditedEntity().getMoon();
        Planet planet = getEditedEntity().getPlanet();
        if (moon != null && planet != null) {

            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption(messageBundle.getMessage("errorPlanetAndMoonCaption"))
                    .withDescription(messageBundle.getMessage("errorPlanetAndMoonMessage"))
                    .show();
            event.preventCommit();
        } else {
            event.resume();
        }
    }
}