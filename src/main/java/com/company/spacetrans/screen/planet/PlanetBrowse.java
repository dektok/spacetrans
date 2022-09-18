package com.company.spacetrans.screen.planet;

import com.company.spacetrans.security.specific.StPlanetsUpload;
import io.jmix.core.AccessManager;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.FileStorageUploadField;
import io.jmix.ui.component.SingleFileUploadField;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.spacetrans.entity.Planet;
import io.jmix.ui.upload.TemporaryStorage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UiController("st_Planet.browse")
@UiDescriptor("planet-browse.xml")
@LookupComponent("planetsTable")
public class PlanetBrowse extends StandardLookup<Planet> {

    @Autowired
    private FileStorageUploadField uploadPlanets;

    @Autowired
    private DataManager dataManager;
    @Autowired
    private TemporaryStorage temporaryStorage;

    @Autowired
    private CollectionLoader<Planet> planetsDl;

    @Autowired
    private Notifications notifications;

    @Autowired
    private MessageBundle messageBundle;

    @Autowired
    private AccessManager accessManager;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        StPlanetsUpload context = new StPlanetsUpload();
        accessManager.applyRegisteredConstraints(context);
        if (!context.isPermitted()) {
            uploadPlanets.setEnabled(false);
        }
    }

    @Subscribe("uploadPlanets")
    public void onUploadPlanetsFileUploadSucceed(SingleFileUploadField.FileUploadSucceedEvent event) throws IOException {
        UUID fileId = uploadPlanets.getFileId();
        File file = temporaryStorage.getFile(fileId);
        List<String> rawPlanets = FileUtils.readLines(file, StandardCharsets.UTF_8);

        List<Planet> importedPlanets = new ArrayList<>();

        for ( String rawPlanet : rawPlanets ) {

            // name;mass;semi-major;orbital;rotation;rings

            String[] strings = rawPlanet.split(";");

            if (strings == null) continue;
            if (strings.length < 6) continue;

            String name = strings[0];

            if (name.isEmpty()) continue;

            Planet planet = dataManager.load(Planet.class)
                    .query("select p from st_Planet p where p.name = :name")
                    .parameter("name", name)
                    .list().stream().findFirst().orElse(null);

            if (planet == null) {
                planet = dataManager.create(Planet.class);
                planet.setName(name);
            }

            try {
                planet.setMass(Double.parseDouble(strings[1]));
                planet.setSemiMajorAxis(Double.parseDouble(strings[2]));
                planet.setOrbitalPeriod(Double.parseDouble(strings[3]));
                planet.setRotationPeriod(Double.parseDouble(strings[4]));
                planet.setRings(strings[5].trim().equals("yes"));
            } catch (Exception e) {
                notifications.create(Notifications.NotificationType.ERROR)
                        .withCaption(messageBundle.getMessage("errorWhileImportCaption"))
                        .withDescription(messageBundle.getMessage("errorWhileImportMessage"))
                        .show();
            }

            dataManager.save(planet);

        }

        planetsDl.load();
        temporaryStorage.deleteFile(fileId);
    }


}