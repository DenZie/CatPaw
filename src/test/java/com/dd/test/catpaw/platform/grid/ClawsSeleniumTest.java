package com.dd.test.claws.platform.grid;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.openqa.selenium.TimeoutException;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.html.Button;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

public class ClawsSeleniumTest {

	final String badLocator = "//wrong locator or text or page title";
	final String locator = "//input[@id='lst-ib']";
	final String disappearElement = "btnI";
	final String pageTitle = "Google";
	final String text = "Maps";
	final String url = "http://www.google.com";

	@Test(groups = { "positive", "title" })
	@WebTest
	public void testWaitUntilPageTitlePresentPos() {
		Grid.driver().get(url);
		try {
			((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilPageTitlePresent(pageTitle);
			assertTrue(true);
		} catch (WaitTimedOutException e) {
			fail(e.getMessage());
		}
	}

	@Test(groups = { "positive", "elemVisible" })
	@WebTest
	public void testWaitUntilElementVisiblePos() {
		Grid.driver().get(url);
		try {
			((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilElementVisible(locator);
			assertTrue(true);
		} catch (WaitTimedOutException e) {
			fail(e.getMessage());
		}
	}

	@Test(groups = { "positive", "textPresent" })
	@WebTest
	public void testWaitUntilTextPresentPos() {
		Grid.driver().get(url);
		try {
			((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilTextPresent(text);
			assertTrue(true);
		} catch (WaitTimedOutException e) {
			fail(e.getMessage());
		}
	}

	@Test(groups = { "positive", "elemDissapear" })
	@WebTest
	public void testWaitUntilElementDisapearPos() {
		Grid.driver().get(url);
		((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilPageTitlePresent(pageTitle);
		Button btn = new Button(disappearElement);
		btn.click();
		try {
			((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilElementDisapear(disappearElement);
			assertTrue(true);
		} catch (WaitTimedOutException e) {
			fail(e.getMessage());
		}
	}

	@Test(groups = { "positive", "elemPresent" })
	@WebTest
	public void testWaitUntilElementPresentPos() {
		Grid.driver().get(url);
		try {
			((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilElementPresent(locator);
			assertTrue(true);
		} catch (WaitTimedOutException e) {
			fail(e.getMessage());
		}
	}

	@Test(groups = { "negative", "title" }, expectedExceptions = { WaitTimedOutException.class })
	@WebTest
	public void testWaitUntilPageTitlePresentNeg() {
		Grid.driver().get(url);
		((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilPageTitlePresent(badLocator);
		fail("Wait Timeout Exception was not thrown.");
	}

	@Test(groups = { "negative", "elemVisible" }, expectedExceptions = { WaitTimedOutException.class })
	@WebTest
	public void testWaitUntilElementVisibleNeg() {
		Grid.driver().get(url);
		((ScreenShotRemoteWebDriver) Grid.driver()).waitUntilElementVisible(badLocator);
		fail("Wait Timeout Exception was not thrown.");
	}

	@Test(groups = { "negative", "textPresent" }, expectedExceptions = { TimeoutException.class })
	@WebTest
	public void testWaitUntilTextPresentNeg() {
		Grid.driver().get(url);
		Grid.driver().waitUntilTextPresent(badLocator);
		fail("Timeout Exception was not thrown.");
	}

	@Test(groups = { "negative", "elemDissapear" }, expectedExceptions = { RuntimeException.class })
	@WebTest
	public void testWaitUntilElementDisapearNeg() {
		Grid.driver().get(url);
		Grid.driver().waitUntilElementDisapear(disappearElement);
		fail("Runtime Exception was not thrown.");
	}

	@Test(groups = { "negative", "elemPresent" }, expectedExceptions = { TimeoutException.class })
	@WebTest
	public void testWaitUntilElementPresentNeg() {
		Grid.driver().get(url);
		Grid.driver().waitUntilElementPresent(badLocator);
		fail("Timeout Exception was not thrown.");
	}
}
