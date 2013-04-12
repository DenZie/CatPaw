package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

import org.openqa.selenium.Alert;
import org.openqa.selenium.InvalidElementStateException;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * This class test the Link class methods
 * 
 */

public class LinkTest {

	Link confirmLink = new Link(TestObjectRepository.LINK_LOCATOR.getValueToUse());
	
	@Test(groups={"browser-tests"})
	@WebTest(sessionName="linktest-flow",keepSessionOpen=true)
	public void linkTestClick() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		confirmLink.click();
		Alert alert = Grid.driver().switchTo().alert();
		assertTrue(alert.getText().matches("You are about to go to a dummy page."),"Validate Click method");
		alert.accept();
	}
	
	@Test(groups={"browser-tests"}, dependsOnMethods="linkTestClick", expectedExceptions={InvalidElementStateException.class})
	@WebTest(sessionName="linktest-flow",openNewSession=false, keepSessionOpen=true)
	public void linkTestClickAndWaitNegativeTest() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		Link confirmLink = new Link(TestObjectRepository.LINK_LOCATOR.getValueToUse());
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		String locatorToWaitFor = TestObjectRepository.BUTTON_SUBMIT_LOCATOR.getValueToUse();
		confirmLink.click(locatorToWaitFor);
	}
	
	@Test(groups={"browser-tests"}, dependsOnMethods="linkTestClickAndWaitNegativeTest")
	@WebTest(sessionName="linktest-flow",openNewSession=false, keepSessionOpen=true)
	public void linkTestClickAndWait() {
		new ButtonTest().flushAllAlerts();
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		Link confirmLink = new Link(TestObjectRepository.CHROME_LINK_LOCATOR.getValueToUse());
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		String locatorToWaitFor = TestObjectRepository.SUCCESS_PAGE_TEXT.getValueToUse();

		confirmLink.click(locatorToWaitFor);
		String title = Grid.driver().getTitle();
		assertTrue(title.matches("Success"),"Validate Click(Object...Expected) method");
		Grid.driver().navigate().back();
	}
	
	@Test(groups={"browser-tests"}, dependsOnMethods="linkTestClickAndWait")
	@WebTest(sessionName="linktest-flow",openNewSession=false)
	public void linkTestClickOnly() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		confirmLink.clickonly();
		Alert alert = Grid.driver().switchTo().alert();
		assertTrue(alert.getText().matches("You are about to go to a dummy page."),"Validate clickonly method");
		alert.accept();
		new ButtonTest().flushAllAlerts();
	}
}
