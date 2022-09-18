package com.company.spacetrans.screen.waybill;

import com.company.spacetrans.app.Calculators;
import com.company.spacetrans.entity.*;
import com.company.spacetrans.screen.waybillitem.WaybillItemEdit;
import io.jmix.core.DataManager;
import io.jmix.core.common.util.ParamsMap;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.Notifications;
import io.jmix.ui.RemoveOperation;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.entitypicker.EntityLookupAction;
import io.jmix.ui.component.*;
import io.jmix.ui.component.data.value.ContainerValueSource;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UiController("st_Waybill.edit")
@UiDescriptor("waybill-edit.xml")
@EditedEntityContainer("waybillDc")
public class WaybillEdit extends StandardEditor<Waybill> {

    static final  Integer companyType = 1;
    static final  Integer individualType = 2;


    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Autowired
    private InstanceContainer<Waybill> waybillDc;

    @Autowired
    private ComboBox consigneeTypeCombo;

    @Autowired
    private ComboBox shipperTypeCombo;
    @Autowired
    private MessageBundle messageBundle;

    @Autowired
    private EntityComboBox shipperField;
    @Autowired
    private EntityPicker consigneeField;
    @Autowired
    private CollectionContainer<Individual> individualDc;
    @Autowired
    private CollectionContainer<Company> companyDc;

    @Autowired
    private ComboBox<AstronomicalBody> departureABCombo;
    @Autowired
    private ComboBox destinationABCombo;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private EntityPicker destinationPortField;
    @Autowired
    private EntityPicker departurePortField;
    @Autowired
    private Table<WaybillItem> itemsTable;
    @Autowired
    private Notifications notifications;

    @Autowired
    private Calculators calculators;

    @Subscribe
    public void onInit(InitEvent event) {

        Map<String, Integer> map = new LinkedHashMap<>();
        map.put(messageBundle.getMessage("companyComboEntry"), 1);
        map.put(messageBundle.getMessage("individualComboEntry"), 2);

        consigneeTypeCombo.setOptionsMap(map);
        shipperTypeCombo.setOptionsMap(map);

        Map<String, AstronomicalBody> abMap = new LinkedHashMap<>();

        List<Planet> planets = dataManager.load(Planet.class)
                .query("select p from st_Planet p")
                .list();

        for (Planet planet : planets ) {
            abMap.put(messageBundle.getMessage("planetComboPrefix")+": "+
                    planet.getName(), (AstronomicalBody) planet);
        }

        List<Moon> moons = dataManager.load(Moon.class)
                .query("select m from st_Moon m")
                .list();

        for (Moon moon : moons ) {
            abMap.put(messageBundle.getMessage("moonComboPrefix")+": "+
                    moon.getName(), (AstronomicalBody) moon);
        }

        departureABCombo.setOptionsMap(abMap);
        destinationABCombo.setOptionsMap(abMap);
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<Waybill> event) {
        Waybill waybill = event.getEntity();
        waybill.setCreator((User)currentAuthentication.getUser());
        waybill.setTotalCharge(BigDecimal.valueOf(0));
        waybill.setTotalWeight((double) 0);

        consigneeTypeCombo.setValue(companyType);
        shipperTypeCombo.setValue(companyType);
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {

        Waybill openedwaybill = getEditedEntity();

        if (openedwaybill.getConsignee() != null) {
            if (openedwaybill.getConsignee() instanceof Company) {
                consigneeField.setMetaClass(companyDc.getEntityMetaClass());
                consigneeTypeCombo.setValue(companyType);
            } else {
                consigneeField.setMetaClass(individualDc.getEntityMetaClass());
                consigneeTypeCombo.setValue(individualType);
            }

            consigneeField.setValue(openedwaybill.getConsignee());
        } else {
            consigneeTypeCombo.setValue(companyType);
        }

        if (openedwaybill.getShipper() != null) {
            if (openedwaybill.getShipper() instanceof Company) {
                shipperTypeCombo.setValue(companyType);
            } else {
                shipperTypeCombo.setValue(individualType);
            }
        } else {
            shipperTypeCombo.setValue(companyType);
        }

        departureABCombo.setValue(findAstronomicalBody(openedwaybill.getDeparturePort()));
        destinationABCombo.setValue(findAstronomicalBody(openedwaybill.getDestinationPort()));

    }

    @Install(to = "itemsTable.create", subject = "afterCloseHandler")
    private void itemsTableCreateAfterCloseHandler(AfterCloseEvent afterCloseEvent) {
        calculators.CalculateWaybillSums(getEditedEntity());
    }

    @Install(to = "itemsTable.edit", subject = "afterCloseHandler")
    private void itemsTableEditAfterCloseHandler(AfterCloseEvent afterCloseEvent) {
        calculators.CalculateWaybillSums(getEditedEntity());
    }

    @Subscribe("itemsTable.positionDown")
    public void onItemsTablePositionDown(Action.ActionPerformedEvent event) {
        getEditedEntity().ItemDown(itemsTable.getSelected().stream().findFirst().orElse(null));
        itemsTable.sort("number", Table.SortDirection.ASCENDING);
    }

    @Subscribe("itemsTable.positionUp")
    public void onItemsTablePositionUp(Action.ActionPerformedEvent event) {
        getEditedEntity().ItemUp(itemsTable.getSelected().stream().findFirst().orElse(null));
        itemsTable.sort("number", Table.SortDirection.ASCENDING);
    }

    @Install(to = "itemsTable.create", subject = "screenConfigurer")
    private void itemsTableCreateScreenConfigurer(Screen screen) {
        Integer nextItemNumber = getEditedEntity().getNextItemNumber();
        ((WaybillItemEdit)screen).getEditedEntity().setNumber(nextItemNumber);
    }

    @Subscribe("carrierField.entityLookup")
    public void onCarrierFieldEntityLookup(Action.ActionPerformedEvent event) {

        if (getEditedEntity().getDeparturePort() == null ||
                getEditedEntity().getDestinationPort() == null ) {

            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption(messageBundle.getMessage("errorRoadmapCaption"))
                    .withDescription(messageBundle.getMessage("errorRoadmapMessage"))
                    .show();

        } else {
            // попытка запустить прерванное lookup событие
            if (event.getSource() instanceof EntityLookupAction ) {
                ((EntityLookupAction)event.getSource()).execute();
            }
        }

    }

    @Subscribe("shipperField")
    public void onShipperFieldValueChange(HasValue.ValueChangeEvent<Customer> event) {

        if (!event.isUserOriginated()) return;

        calculators.CalculateWaybillSums(getEditedEntity());
    }

    @Install(to = "itemsTable.remove", subject = "afterActionPerformedHandler")
    private void itemsTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent<WaybillItem> afterActionPerformedEvent) {
        getEditedEntity().RenumberItems();
        calculators.CalculateWaybillSums(getEditedEntity());
    }


    @Install(to = "carrierField.entityLookup", subject = "screenOptionsSupplier")
    private ScreenOptions carrierFieldEntityLookupScreenOptionsSupplier() {
        return new MapScreenOptions(ParamsMap.of("ports", true,
                "port1", getEditedEntity().getDeparturePort(),
                "port2", getEditedEntity().getDestinationPort()));
    }

    @Subscribe("consigneeTypeCombo")
    public void onConsigneeTypeComboValueChange(HasValue.ValueChangeEvent event) {

        if (event.getValue().equals(1)){
            consigneeField.setMetaClass(companyDc.getEntityMetaClass());
        } else {
            consigneeField.setMetaClass(individualDc.getEntityMetaClass());
        }

        if (!event.isUserOriginated()) return;

        consigneeField.setValue(null);
    }

    @Subscribe("consigneeField")
    public void onConsigneeFieldValueChange(HasValue.ValueChangeEvent event) {
        getEditedEntity().setConsignee((Customer) event.getSource().getValue());
    }

    @Subscribe("shipperTypeCombo")
    public void onShipperTypeComboValueChange(HasValue.ValueChangeEvent event) {

        if ( event.getValue().equals(1)){
            shipperField.setOptionsContainer(companyDc);
        } else {
            shipperField.setOptionsContainer(individualDc);
        }

        if (!event.isUserOriginated()) return;

        shipperField.setValue(null);
    }

    @Subscribe("destinationABCombo")
    public void onDestinationABComboValueChange(HasValue.ValueChangeEvent event) {

        if (!event.isUserOriginated()) return;

        Object ab = event.getValue();

        Spaceport spaceport = findSpaceport( ab );

        if (spaceport != null && spaceport.equals( getEditedEntity().getDestinationPort() )) return;

        destinationPortField.setValue(spaceport);
        getEditedEntity().setCarrier(null);

    }

    @Subscribe("departureABCombo")
    public void onDepartureABComboValueChange(HasValue.ValueChangeEvent event) {

        if (!event.isUserOriginated()) return;

        Object ab = event.getValue();

        Spaceport spaceport = findSpaceport( ab );

        if ( spaceport != null && spaceport.equals( getEditedEntity().getDeparturePort() )) return;

        departurePortField.setValue(spaceport);
        getEditedEntity().setCarrier(null);
    }

    private Spaceport findSpaceport( Object ab) {

        Spaceport spaceport = null;

        if (ab instanceof Planet) {

            spaceport = dataManager.load(Spaceport.class)
                    .query("select s from st_Spaceport s where s.planet.name = :abname and s.isDefault = true")
                    .parameter("abname", ((Planet) ab).getName())
                    .optional().orElse(null);

        } else if (ab instanceof Moon) {

            spaceport = dataManager.load(Spaceport.class)
                    .query("select s from st_Spaceport s where s.moon.name = :abname and s.isDefault = true")
                    .parameter("abname", ((Moon) ab).getName())
                    .optional().orElse(null);
        }

        return spaceport;
    }

    @Subscribe("departurePortField")
    public void onDeparturePortFieldValueChange(HasValue.ValueChangeEvent<Spaceport> event) {

        if (!event.isUserOriginated()) return;

        getEditedEntity().setCarrier(null);

        Spaceport spaceport = event.getSource().getValue();

        if ( spaceport == null) {
            departureABCombo.setValue(null);
            return;
        }

        departureABCombo.setValue(findAstronomicalBody(spaceport));

    }

    @Subscribe("destinationPortField")
    public void onDestinationPortFieldValueChange(HasValue.ValueChangeEvent<Spaceport> event) {

        if (!event.isUserOriginated()) return;

        getEditedEntity().setCarrier(null);

        Spaceport spaceport = event.getSource().getValue();

        if ( spaceport == null) {
            destinationABCombo.setValue(null);
            return;
        }

        destinationABCombo.setValue(findAstronomicalBody(spaceport));
    }

    private AstronomicalBody findAstronomicalBody(Spaceport spaceport) {

        if (spaceport != null)  {
            if (spaceport.getMoon() != null) {
                return spaceport.getMoon();
            } else if (spaceport.getPlanet() != null ) {
                return spaceport.getPlanet();
            }
        }

        return null;
    }

}