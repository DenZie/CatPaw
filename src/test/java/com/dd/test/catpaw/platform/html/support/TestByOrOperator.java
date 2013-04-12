package com.dd.test.claws.platform.html.support;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;
import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertFalse;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;
import com.dd.test.claws.platform.html.Link;
import com.dd.test.claws.platform.html.TestObjectRepository;

/**
 * This class test the ByOrOperator
 * 
 */

public class TestByOrOperator {
	Link compltedLink = new Link(TestObjectRepository.COMPLETED_LINK_LOCATOR.getValueToUse());
	Link compltedLinkNeg = new Link(TestObjectRepository.COMPLETED_LINK_LOCATOR_NEG.getValueToUse());
	
	@Test(groups={"browser-tests"})
	@WebTest
	public void testValidLocator() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		compltedLink.click();
		String title = Grid.driver().getTitle();
		assertTrue(title.matches("Success"),"Validate valid locator using ByOrOperator");
	}
	@Test(groups={"browser-tests"})
	@WebTest
	public void testInValidLocator() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertFalse(compltedLinkNeg.isElementPresent(),"Validate invalid locator using ByOrOperator");
	}
}
