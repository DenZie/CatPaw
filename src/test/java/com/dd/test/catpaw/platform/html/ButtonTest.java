package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

import org.openqa.selenium.Alert;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * This class test the Button class methods
 * 
 */
public class ButtonTest {
	
	Button submitButton = new Button(TestObjectRepository.BUTTON_SUBMIT_LOCATOR.getValueToUse());

	@Test(groups={"browser-tests"})
	@WebTest(sessionName="buttontest-flow",keepSessionOpen=true)
	public void btnTestClick(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		submitButton.click();
		Alert alert = Grid.driver().switchTo().alert();
		assertTrue(alert.getText().matches("onsubmit called"),"Validated Click method");
		alert.accept();
		alert.dismiss();
	}
	@Test(groups={"browser-tests"},dependsOnMethods="btnTestClick")
	@WebTest(sessionName="buttontest-flow",openNewSession=false, keepSessionOpen=true)
	public void btnTestClickonly(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		submitButton.clickonly();
		Alert alert = Grid.driver().switchTo().alert();
		assertTrue(alert.getText().matches("onsubmit called"),"Validate ClockOnly method");
		alert.accept();
		alert.dismiss();
	}
	@Test(groups={"browser-tests"},dependsOnMethods="btnTestClickonly",expectedExceptions={InvalidElementStateException.class})
	@WebTest(sessionName="buttontest-flow",openNewSession=false, keepSessionOpen=true)
	public void btnTestClickAndWaitNegativeTest(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		Button submitButton = new Button(TestObjectRepository.BUTTON_SUBMIT_LOCATOR.getValueToUse());
		String locatorToWaitFor = TestObjectRepository.LINK_LOCATOR.getValueToUse();
		submitButton.click(locatorToWaitFor);
	}
	
	@Test(groups={"browser-tests"},dependsOnMethods="btnTestClickAndWaitNegativeTest")
	@WebTest(sessionName="buttontest-flow",openNewSession=false)
	public void btnTestClickAndWait(){
		flushAllAlerts();
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		Button submitButton = new Button(TestObjectRepository.CHROME_BUTTON_SUBMIT_LOCATOR.getValueToUse());
		String locatorToWaitFor = TestObjectRepository.SUCCESS_PAGE_TEXT.getValueToUse();
		submitButton.click(locatorToWaitFor);
		String title = Grid.driver().getTitle();
		assertTrue(title.matches("Success"),"Validate Click(Object...Expected) method");
		flushAllAlerts();
	}
	
	public void flushAllAlerts(){
		while (true){
			try{
				Alert alert = Grid.driver().switchTo().alert();
				alert.accept();
				alert.dismiss();
			}catch(NoAlertPresentException exception){
				//No more alerts were present. So time to break the loop.
				break;
			}
		}
	}
}
