package com.dd.test.claws.platform.html;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.config.ClawsConfig;
import com.dd.test.claws.platform.config.ClawsConfig.ClawsConfigProperty;
import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * Testing the date picker widget from a webpage.
 */
//TODO: This test and DatePicker doesn't seem to work. Fix it.
public class DatePickerTest {
	@Test(groups = { "ie-broken-test", "ff-broken-test","chrome-broken-test" },enabled=false)
	@WebTest()
	public void setDate01Test() throws InterruptedException {
		String datePickerLoc = "//table[@id='cal1']/thead";
		String nextLoc = "//table[@id='cal1']/thead/tr[1]/th/div/a[2]";
		String prevLoc = "//table[@id='cal1']/thead/tr[1]/th/div/a[1]";
		String dateLoc = "//div[@class='calheader']";

		Grid.driver().get("http://developer.yahoo.com/yui/examples/calendar/quickstart.html");
		Grid.driver().waitForPageToLoad(ClawsConfig.getConfigProperty(ClawsConfigProperty.EXECUTION_TIMEOUT));
		DatePicker dt = new DatePicker(datePickerLoc, prevLoc, nextLoc, dateLoc);
		dt.setDate(2010, 05, 20);
		Thread.sleep(5000);
		
		System.out.println(dt.getDate());
		
	}

}
