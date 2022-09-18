package com.company.spacetrans.app;

import com.company.spacetrans.entity.*;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component("st_Calculators")
public class Calculators {

    @Autowired
    private DataManager dataManager;

    public void CalculateWaybillSums(Waybill waybill) {

        List<WaybillItem> items = waybill.getItems();

        if (items == null) {
            waybill.setTotalWeight((double) 0);
            waybill.setTotalCharge(BigDecimal.valueOf(0));
            return;
        }

        BigDecimal totalCharge = BigDecimal.valueOf(0);
        Double totalWeight = Double.valueOf(0);

        for (WaybillItem item: items ) {
            totalWeight += item.getWeigth();
            totalCharge = totalCharge.add(item.getCharge());
        }

        // расчет скидки
        Customer shipper = waybill.getShipper();
        if (shipper != null) {
            CustomerGrade grade = shipper.getGrade();
            if (grade != null) {

                Discounts discount = dataManager.load(Discounts.class)
                        .query("select d from st_Discounts d where d.grade = :grade")
                        .parameter("grade", grade.getId())
                        .list().stream().findFirst().orElse(null);
                if (discount != null) {
                    BigDecimal value = discount.getValue();
                    if (value != null && value.compareTo(BigDecimal.valueOf(0)) > 0) {
                        totalCharge = totalCharge
                                .subtract(totalCharge
                                        .multiply(value));
//                                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                    }
                }
            }
        }

        waybill.setTotalWeight(totalWeight);
        waybill.setTotalCharge(totalCharge);
    }

}