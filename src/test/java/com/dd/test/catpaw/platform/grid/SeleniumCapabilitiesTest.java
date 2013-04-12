package com.dd.test.claws.platform.grid;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import com.dd.test.claws.platform.grid.WebTest;
public class SeleniumCapabilitiesTest {
	@Test(groups = "unit")
	@WebTest(browser = "*firefox")
	public void testDefaultBrowser() {
		String userAgent = (String) Grid.driver().executeScript("return navigator.userAgent", "");
		assertTrue(userAgent.toLowerCase().contains("firefox"));
	}

	@Test(groups = "unit")
	@WebTest()
	public void testDefaultBrowser1() {
		String userAgent = (String) Grid.driver().executeScript("return navigator.userAgent", "");
		assertTrue(userAgent.toLowerCase().contains("firefox"));
	}

	@Test(groups = "unit")
	@WebTest(browser = "")
	public void testDefaultBrowser2() {
		String userAgent = (String) Grid.driver().executeScript("return navigator.userAgent", "");
		assertTrue(userAgent.toLowerCase().contains("firefox"));
	}
	
	@Test(groups = "unit")
	@WebTest(browser = "*iexplore")
	public void testIexploreBrowser() {
		String userAgent = (String) Grid.driver().executeScript("return navigator.userAgent", "");
		assertTrue(userAgent.toLowerCase().contains("msie"));
	}
	
	@Test(groups = "unit")
	@WebTest(browser = "*chrome")
	public void testChromeBrowser() {
		String userAgent = (String) Grid.driver().executeScript("return navigator.userAgent", "");
		assertTrue(userAgent.toLowerCase().contains("chrome"));
	}
	
	@Test(groups = "unit", expectedExceptions={RuntimeException.class})
	@WebTest(browser = "*mybrowser")
	public void testWrongBrowser() {
		fail("No such browser and hence Exception should have been thrown.");
	}
	
}
