package com.company.spacetrans.security;

import com.company.spacetrans.entity.Waybill;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;

@RowLevelRole(name = "Full waybill access", code = "full-waybill-access")
public interface  FullWaybillRole {

    // тут я что-то в задании не понял как именно это должно работать
    @JpqlRowLevelPolicy(entityClass = Waybill.class, where = "")
    void restrictWaybillAllVisible();
}
