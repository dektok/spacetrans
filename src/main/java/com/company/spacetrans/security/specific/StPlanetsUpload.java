package com.company.spacetrans.security.specific;

import io.jmix.core.accesscontext.SpecificOperationAccessContext;

public class StPlanetsUpload extends SpecificOperationAccessContext {

    public static final String NAME = "st.planets.upload";

    public StPlanetsUpload() {
        super(NAME);
    }
}
