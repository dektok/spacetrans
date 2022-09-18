package com.company.spacetrans;

import com.company.spacetrans.entity.Waybill;
import io.jmix.core.SaveContext;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.security.SystemAuthenticator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SpacetransApplicationTests {

	@Autowired
	SystemAuthenticator systemAuthenticator;

	@Autowired
	protected UnconstrainedDataManager dataManager;

	protected List<Waybill> entitiesToRemove = new ArrayList<>();

	@BeforeEach
	void setUp() {
		systemAuthenticator.begin("admin");
	}

	@AfterEach
	void tearDown() {

		dataManager.remove(entitiesToRemove);

		systemAuthenticator.end();
	}

}
