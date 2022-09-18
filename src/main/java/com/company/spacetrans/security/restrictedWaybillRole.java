package com.company.spacetrans.security;

import com.company.spacetrans.entity.Waybill;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;

@RowLevelRole(name = "Owner waybill access", code = "owner-waybill-access")
public interface restrictedWaybillRole {

    @JpqlRowLevelPolicy(entityClass = Waybill.class, where = "{E}.creator.id = :current_user_id")
    void restrictWaybillOwnerVisible();
}
