package com.dd.test.claws.platform.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.config.ClawsConfig.ClawsConfigProperty;
public class ClawsConfigTest {
	final static Logger logger = Logger.getLogger(ClawsConfigTest.class);

	@Test(groups = { "unit" })
	public void testConfig(ITestContext context) {
		System.out.println("The test plan is " + context.getName());
		System.out.println("params " + context.getCurrentXmlTest().getAllParameters());
		String clawsName = null;
		String jawsName = null;
		String clawsConfigValue = null;
		String jawsConfigValue = null;
	}

	/**
	 * Test ClawsConfig here Every method has Claws check and Jaws check
	 */

	@Test(groups = { "unit", "P1" }, dependsOnMethods = { "testConfig" })
	public void testGetConfigProperty() {
		// Claws

		Assert.assertNotNull(ClawsConfig.getConfigProperty(ClawsConfigProperty.HOSTNAME),
				"Get config property should not be null");
	}

	@Test(groups = { "unit", "P1" }, dependsOnMethods = { "testConfig" })
	public void testGetConfigProperty_SSH_USER() {
		// Claws
		Assert.assertNotNull(ClawsConfig.getConfigProperty(ClawsConfigProperty.SSH_USER),
				"The SSH_USER value should not be null");
		Assert.assertNotSame(ClawsConfig.getConfigProperty(ClawsConfigProperty.SSH_USER), "",
				"The SSH_USER value should not be empty");
		// Jaws

	}

	@Test(groups = { "P4" }, dependsOnMethods = { "testConfig" })
	public void testGetConfigProperty_PayPalURL() {
		// Claws
		Assert.assertNotNull(ClawsConfig.getConfigProperty(ClawsConfigProperty.PAYPAL_URL),
				"PayPal URL should not be null");
		Assert.assertNotSame(ClawsConfig.getConfigProperty(ClawsConfigProperty.PAYPAL_URL), "",
				"PayPal URL should not be empty");
	}

	@Test(groups = { "P4" }, dependsOnMethods = { "testConfig" })
	public void testGetConfigProperty_PayPalAdminURL() {
		// Claws
		Assert.assertNotNull(ClawsConfig.getConfigProperty(ClawsConfigProperty.PAYPAL_ADMIN_URL), "PayPal Admin URL should not be null");
		Assert.assertNotSame(ClawsConfig.getConfigProperty(ClawsConfigProperty.PAYPAL_ADMIN_URL),"", "PayPal Admin URL should not be empty");
	}

	@Test(groups = { "P4" }, dependsOnMethods = { "testConfig" })
	public void testInitConfig(ITestContext context) {
		// Claws
		ClawsConfig.initConfig(context);
		Assert.assertNotNull(ClawsConfig.getConfigProperty(ClawsConfigProperty.HOSTNAME), "Config should not be null");
		// Jaws
		ClawsConfig.initConfig(context);
	}

	@Test(groups = { "P4" }, dependsOnMethods = { "testConfig" })
	public void testInitConfig_ChangeOption() {
		// Claws
		String hostname = ClawsConfig.getConfigProperty(ClawsConfigProperty.HOSTNAME);
		Map<ClawsConfigProperty, String> initValues = new HashMap<ClawsConfigProperty, String>();
		initValues.put(ClawsConfigProperty.HOSTNAME, "new" + hostname);
		ClawsConfig.initConfig(initValues);
		Assert.assertEquals(ClawsConfig.getConfigProperty((ClawsConfigProperty.HOSTNAME)), "new" + hostname,
				"hostname should be changed");
		// Jaws
	}

	@Test(groups = { "P4" }, dependsOnMethods = { "testConfig" })
	public void testSetConfigProperty() throws Exception {
		// Claws
		String hostname = ClawsConfig.getConfigProperty(ClawsConfigProperty.HOSTNAME);
		ClawsConfig.setConfigProperty(ClawsConfigProperty.HOSTNAME, "modified" + hostname);
		Assert.assertEquals(ClawsConfig.getConfigProperty((ClawsConfigProperty.HOSTNAME)), "modified" + hostname,
				"hostname should be changed");

	}

	@AfterClass(alwaysRun = true)
	public static void tearDown(ITestContext context) {
		// restore config
		ClawsConfig.initConfig(context);
		// Log Info
		logger.debug("tearDown");
		logger.debug("CLAWS_HOSTNAME   ->   " + ClawsConfig.getConfigProperty(ClawsConfigProperty.HOSTNAME));

	}
}
