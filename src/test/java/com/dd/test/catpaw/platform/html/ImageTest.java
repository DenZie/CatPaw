package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;
import org.openqa.selenium.Alert;
import org.openqa.selenium.InvalidElementStateException;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * This class test the Image class methods
 */
public class ImageTest {
	Image imageTest = new Image(TestObjectRepository.IMAGE_TEST.getValueToUse());

	@Test(groups = { "browser-tests" })
	@WebTest(sessionName = "imagetest-flow", keepSessionOpen = true)
	public void imageTestClick() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		imageTest.click();
		Alert alert = Grid.driver().switchTo().alert();
		assertTrue(alert.getText().matches("onsubmit called"), "Validate Clcik method");
		alert.accept();
	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "imageTestClick", expectedExceptions = { InvalidElementStateException.class })
	@WebTest(sessionName = "imagetest-flow", openNewSession = false, keepSessionOpen = true)
	public void imageTestClickAndWaitNegativeTest() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		Image imageTest = new Image(TestObjectRepository.IMAGE_TEST.getValueToUse());
		String locatorToWaitFor = TestObjectRepository.LINK_LOCATOR.getValueToUse();
		imageTest.click(locatorToWaitFor);
	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "imageTestClickAndWaitNegativeTest")
	@WebTest(sessionName = "imagetest-flow", openNewSession = false, keepSessionOpen = true)
	public void imageTestClickAndWait() {
		new ButtonTest().flushAllAlerts();
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		Image imageTest = new Image(TestObjectRepository.CHROME_IMAGE_TEST.getValueToUse());
		String locatorToWaitFor = TestObjectRepository.SUCCESS_PAGE_TEXT.getValueToUse();
		imageTest.click(locatorToWaitFor);
		String title = Grid.driver().getTitle();
		assertTrue(title.matches("Success"), "Validate Click(Object...Expected) method");
	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "imageTestClickAndWait")
	@WebTest(sessionName = "imagetest-flow", openNewSession = false, keepSessionOpen = true)
	public void imageTestGetHeight() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue((imageTest.getHeight() == 41), "Validated GetHeight method");
	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "imageTestGetHeight")
	@WebTest(sessionName = "imagetest-flow", openNewSession = false)
	public void imageTestGetWidth() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue((imageTest.getWidth() == 41), "Validated GetWidth method");
		new ButtonTest().flushAllAlerts();
	}
}
