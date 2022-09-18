package com.company.spacetrans.screen.waybillitem;

import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.TextInputField;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.screen.*;
import com.company.spacetrans.entity.WaybillItem;

@UiController("st_WaybillItem.edit")
@UiDescriptor("waybill-item-edit.xml")
@EditedEntityContainer("waybillItemDc")
public class WaybillItemEdit extends StandardEditor<WaybillItem> {

    @Subscribe("dimHeightField")
    public void onDimHeightFieldValueChange(HasValue.ValueChangeEvent<Double> event) {

    }

    @Subscribe("dimLengthField")
    public void onDimLengthFieldValueChange(HasValue.ValueChangeEvent<Double> event) {

    }

    @Subscribe("dimWidthField")
    public void onDimWidthFieldValueChange(HasValue.ValueChangeEvent<Double> event) {

    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onChange(DataContext.ChangeEvent event) {
        getEditedEntity().CalculateCharge();
    }



}