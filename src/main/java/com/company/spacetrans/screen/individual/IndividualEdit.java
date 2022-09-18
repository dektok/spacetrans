package com.company.spacetrans.screen.individual;

import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.screen.*;
import com.company.spacetrans.entity.Individual;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("st_Individual.edit")
@UiDescriptor("individual-edit.xml")
@EditedEntityContainer("individualDc")
public class IndividualEdit extends StandardEditor<Individual> {

}