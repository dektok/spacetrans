package com.company.spacetrans.security;

import com.company.spacetrans.entity.*;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityui.role.annotation.MenuPolicy;
import io.jmix.securityui.role.annotation.ScreenPolicy;

@ResourceRole(name = "OperatorRole", code = "operator-role", scope = "UI")
public interface OperatorRole {
    @MenuPolicy(menuIds = {"st_Atmosphere.browse", "st_Gas.browse", "st_Moon.browse", "st_Planet.browse", "st_Waybill.browse", "st_Company.browse", "st_Individual.browse", "st_Carrier.browse", "st_Spaceport.browse"})
    @ScreenPolicy(screenIds = {"st_Atmosphere.browse", "st_Gas.browse", "st_Moon.browse", "st_Planet.browse", "st_Waybill.browse", "st_Company.browse", "st_Individual.browse", "st_Atmosphere.edit", "st_Carrier.browse", "st_Carrier.edit", "st_Company.edit", "st_Gas.edit", "st_Individual.edit", "st_LoginScreen", "st_MainScreen", "st_Moon.edit", "st_Planet.edit", "st_Spaceport.browse", "st_Spaceport.edit", "st_Waybill.edit", "st_WaybillItem.edit", "st_AtmosphericGas.browse", "st_AtmosphericGas.edit"})
    void screens();

    @EntityAttributePolicy(entityClass = AstronomicalBody.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = AstronomicalBody.class, actions = EntityPolicyAction.READ)
    void astronomicalBody();

    @EntityAttributePolicy(entityClass = Atmosphere.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Atmosphere.class, actions = EntityPolicyAction.READ)
    void atmosphere();

    @EntityAttributePolicy(entityClass = AtmosphericGas.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = AtmosphericGas.class, actions = EntityPolicyAction.READ)
    void atmosphericGas();

    @EntityAttributePolicy(entityClass = Carrier.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Carrier.class, actions = EntityPolicyAction.READ)
    void carrier();

    @EntityAttributePolicy(entityClass = Company.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Company.class, actions = EntityPolicyAction.ALL)
    void company();

    @EntityAttributePolicy(entityClass = Gas.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Gas.class, actions = EntityPolicyAction.READ)
    void gas();

    @EntityAttributePolicy(entityClass = Individual.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Individual.class, actions = EntityPolicyAction.ALL)
    void individual();

    @EntityAttributePolicy(entityClass = Moon.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Moon.class, actions = EntityPolicyAction.READ)
    void moon();

    @EntityAttributePolicy(entityClass = Planet.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Planet.class, actions = EntityPolicyAction.READ)
    void planet();

    @EntityAttributePolicy(entityClass = Spaceport.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Spaceport.class, actions = EntityPolicyAction.READ)
    void spaceport();

    @EntityAttributePolicy(entityClass = Waybill.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Waybill.class, actions = EntityPolicyAction.ALL)
    void waybill();

    @EntityAttributePolicy(entityClass = WaybillItem.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = WaybillItem.class, actions = EntityPolicyAction.ALL)
    void waybillItem();

    @EntityAttributePolicy(entityClass = Dimensions.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Dimensions.class, actions = EntityPolicyAction.ALL)
    void dimensions();

    @EntityAttributePolicy(entityClass = Customer.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Customer.class, actions = EntityPolicyAction.ALL)
    void customer();
}