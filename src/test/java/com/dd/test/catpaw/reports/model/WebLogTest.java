package com.dd.test.claws.reports.model;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for WebLog.java
 */
public class WebLogTest {
	
	/**
	 * Tests setCal and WebLog.toString methods
	 */
	@Test(groups={"functional"})
	public void setCalTest(){
		WebLog current = new WebLog();
		current.setCal("web=123456");
		Assert.assertTrue(current.toString().contains("CAL="), " Cal should be set");
		Assert.assertTrue(current.toString().contains("web=123456"), " Cal should be set");
	}

	/**
	 * Tests setCal and getCal methods
	 */
	@Test(groups={"functional"})
	public void getCalTest(){
		WebLog current = new WebLog();
		current.setCal("web=123456");
		String calLog = current.getCal();
		Assert.assertTrue(calLog.contains("web=123456"), " Cal should be set");
	}	

}
