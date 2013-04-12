package com.dd.test.claws.reports.runtime;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.config.ClawsConfig;
import com.dd.test.claws.platform.config.ClawsConfig.ClawsConfigProperty;
import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * Test class for WebLog.java
 */
public class WebReporterTest {
	String calLogSet;
	
	@BeforeClass (alwaysRun = true)
	public void setup(){
		calLogSet = ClawsConfig.getConfigProperty(ClawsConfigProperty.CAL_LOG);
		ClawsConfig.setConfigProperty(ClawsConfigProperty.CAL_LOG, "true");
	}
	
	@Test (testName = "SBTU1159104F", groups={"functional"})
	@WebTest 
	public void logTest(){
		Grid.driver().get(ClawsConfig.getConfigProperty(ClawsConfigProperty.PAYPAL_URL));
		WebReporter.log("Test rlogId", true,true);
	}
	
	@AfterClass (alwaysRun = true)
	public void tearDown(){
		ClawsConfig.setConfigProperty(ClawsConfigProperty.CAL_LOG, calLogSet);
		calLogSet = ClawsConfig.getConfigProperty(ClawsConfigProperty.CAL_LOG);
	}

}
