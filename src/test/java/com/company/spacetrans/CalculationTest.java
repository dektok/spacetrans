package com.company.spacetrans;

import com.company.spacetrans.entity.Dimensions;
import com.company.spacetrans.entity.Waybill;
import com.company.spacetrans.entity.WaybillItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculationTest extends SpacetransApplicationTests{

    Waybill CreateWayBill() {

        Waybill waybill = dataManager.create(Waybill.class);
        waybill.setReference("Test waybill");

        waybill.setItems(new ArrayList<>());

        WaybillItem newitem1 = dataManager.create(WaybillItem.class);
        newitem1.setNumber( waybill.getNextItemNumber());
        newitem1.setName("Item 1");

        newitem1.setWeigth(Double.valueOf(10));

        Dimensions dim = new Dimensions();
        newitem1.setDim( dim);
        dim.setHeight(Double.valueOf(1));
        dim.setWidth(Double.valueOf(2));
        dim.setLength(Double.valueOf(3));
        newitem1.CalculateCharge();
        newitem1.setWaybill(waybill);

        waybill.getItems().add(newitem1);

        WaybillItem newitem2 =  dataManager.create(WaybillItem.class);

        newitem2.setNumber( waybill.getNextItemNumber());
        newitem2.setName("Item 2");

        newitem2.setWeigth(Double.valueOf(20));

        dim = new Dimensions();
        newitem2.setDim( dim);
        dim.setHeight(Double.valueOf(5));
        dim.setWidth(Double.valueOf(6));
        dim.setLength(Double.valueOf(7));
        newitem2.CalculateCharge();
        newitem2.setWaybill(waybill);

        waybill.getItems().add(newitem2);

        dataManager.save(waybill, newitem1, newitem2);

        return waybill;
    }

    @Test
    void totalsTest(){

        // содание тестовой накладной и сохранение ее в базе
        Waybill waybill = CreateWayBill();
        entitiesToRemove.add(waybill);

        // получение сохраненной накладной из базы
        Waybill checkedWaybill = dataManager.load(Waybill.class)
                .query("select w from st_Waybill w where w.id = :id")
                .parameter("id", waybill.getId())
                .optional().orElse(null);

        // проверки
        Assertions.assertNotNull(checkedWaybill);

        // суммарный вес
        Assertions.assertEquals(checkedWaybill.getTotalWeight(), Double.valueOf(30));

        // суммарный сбор
        Assertions.assertEquals(checkedWaybill.getTotalCharge(), BigDecimal.valueOf(426000, 2));
    }

    @Test
    void addPositionTest() {

        // содание тестовой накладной и сохранение ее в базе
        Waybill waybill = CreateWayBill();

        // получение сохраненной накладной из базы, добавление новой позиции и сохранении накладной
        Waybill loadedWaybill = dataManager.load(Waybill.class)
                .query("select w from st_Waybill w join fetch w.items where w.id = :id")
                .parameter("id", waybill.getId())
                .optional().orElse(null);

        Assertions.assertNotNull(loadedWaybill);

        WaybillItem newitem =  dataManager.create(WaybillItem.class);

        newitem.setNumber( waybill.getNextItemNumber());
        newitem.setName("Item 3");

        newitem.setWeigth(Double.valueOf(30));

        Dimensions dim = new Dimensions();
        newitem.setDim( dim);
        dim.setHeight(Double.valueOf(5));
        dim.setWidth(Double.valueOf(6));
        dim.setLength(Double.valueOf(7));
        newitem.CalculateCharge();
        newitem.setWaybill(waybill);

        loadedWaybill.getItems().add(newitem);

        dataManager.save(loadedWaybill, newitem);
        entitiesToRemove.add(loadedWaybill);

        // получение сохраненной накладной из базы
        Waybill checkedWaybill = dataManager.load(Waybill.class)
                .query("select w from st_Waybill w where w.id = :id")
                .parameter("id", loadedWaybill.getId())
                .optional().orElse(null);

        // проверки
        Assertions.assertNotNull(checkedWaybill);

        // суммарный вес
        Assertions.assertEquals(checkedWaybill.getTotalWeight(), Double.valueOf(60));

        // суммарный сбор
        Assertions.assertEquals(checkedWaybill.getTotalCharge(), BigDecimal.valueOf(1056000, 2));

    }

    @Test
    void waybillItemChange() {

        // содание тестовой накладной и сохранение ее в базе
        Waybill waybill = CreateWayBill();

        // получение сохраненной накладной из базы, добавление новой позиции и сохранении накладной
        Waybill loadedWaybill = dataManager.load(Waybill.class)
                .query("select w from st_Waybill w join fetch w.items where w.id = :id")
                .parameter("id", waybill.getId())
                .optional().orElse(null);

        Assertions.assertNotNull(loadedWaybill);
        entitiesToRemove.add(loadedWaybill);

        WaybillItem item = loadedWaybill.getItems().stream()
                .filter(i -> i.getName().equals("Item 1"))
                .findFirst().orElse(null);

        Assertions.assertNotNull(item);

        item.setWeigth(Double.valueOf(5));
        item.CalculateCharge();

        dataManager.save(item);

        // получение сохраненной накладной из базы
        Waybill checkedWaybill = dataManager.load(Waybill.class)
                .query("select w from st_Waybill w where w.id = :id")
                .parameter("id", loadedWaybill.getId())
                .optional().orElse(null);

        // проверки
        Assertions.assertNotNull(checkedWaybill);

        // суммарный вес
        Assertions.assertEquals(checkedWaybill.getTotalWeight(), Double.valueOf(25));

        // суммарный сбор
        Assertions.assertEquals(checkedWaybill.getTotalCharge(), BigDecimal.valueOf(423000, 2));

    }

}
