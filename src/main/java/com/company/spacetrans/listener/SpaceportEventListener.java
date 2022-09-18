package com.company.spacetrans.listener;

import com.company.spacetrans.entity.Spaceport;
import io.jmix.core.DataManager;
import io.jmix.core.FluentLoader;
import io.jmix.core.event.EntityChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component("st_SpaceportEventListener")
public class SpaceportEventListener {

    @Autowired
    private DataManager dataManager;

    @EventListener
    public void onSpaceportChangedBeforeCommit(EntityChangedEvent<Spaceport> event) {
        if (event.getType() != EntityChangedEvent.Type.DELETED
                && event.getChanges().isChanged("isDefault")) {

            Spaceport spaceport = dataManager.load(event.getEntityId()).one();

            if(spaceport.getIsDefault().equals(false)) {
                return;
            }

            String abName = "";
            String query = "";

            if ( spaceport.getMoon() != null ) {
                abName = spaceport.getMoon().getName();
                query = "select s from st_Spaceport s where s.moon.name = :abname";
            } else if (spaceport.getPlanet() != null ) {
                abName = spaceport.getPlanet().getName();
                query = "select s from st_Spaceport s where s.planet.name = :abname";
            }

            if (abName.isEmpty()) return;

            List<Spaceport> spaceports = dataManager.load(Spaceport.class)
                    .query(query)
                    .parameter("abname", abName )
                    .list();

            for (Spaceport sp: spaceports){
                if ( spaceport.getId().equals(sp.getId())) {
                    continue;
                }

                Boolean isDefault = sp.getIsDefault();

                if (isDefault != null && isDefault.equals(true)) {
                    sp.setIsDefault(false);
                    dataManager.save(sp);
                    break; // должен быть только один дефолтный и мы его нашли
                }
            }
        }
    }

}