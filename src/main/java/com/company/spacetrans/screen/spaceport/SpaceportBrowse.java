package com.company.spacetrans.screen.spaceport;

import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.spacetrans.entity.Spaceport;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("st_Spaceport.browse")
@UiDescriptor("spaceport-browse.xml")
@LookupComponent("spaceportsTable")
public class SpaceportBrowse extends StandardLookup<Spaceport> {

    @Autowired
    private CollectionLoader<Spaceport> spaceportsDl;

    @Install(to = "spaceportsTable.edit", subject = "afterCommitHandler")
    private void spaceportsTableEditAfterCommitHandler(Spaceport spaceport) {
        // в случае изменении дефолтного порта меняются объекты через DataManager
        // изменения этих объектов не отражаются на экране
        // требуется перезагрузить измененные данные
        spaceportsDl.load();
    }

    @Install(to = "spaceportsTable.create", subject = "afterCommitHandler")
    private void spaceportsTableCreateAfterCommitHandler(Spaceport spaceport) {
        // в случае изменении дефолтного порта меняются объекты через DataManager
        // изменения этих объектов не отражаются на экране
        // требуется перезагрузить измененные данные
        spaceportsDl.load();
    }

}