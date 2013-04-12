package com.dd.test.claws.platform.asserts;


import org.testng.annotations.Test;

/**
 * Unit Tests for ClawsAssert and SoftAssertCapabilities
 */

public class SoftAssertCapabilitiesTest {
	@Test(groups = { "unit"})
	public void testSoftAssertCapabilities() {
		ClawsAsserts.verifyTrue(true);
		ClawsAsserts.verifyEquals(true, true);
		ClawsAsserts.verifyEquals("OK", "OK");
		ClawsAsserts.verifyFalse(false);
		ClawsAsserts.assertTrue(true);
		ClawsAsserts.assertEquals(true, true);
		ClawsAsserts.assertFalse(false);
		ClawsAsserts.assertEquals("OK", "OK");
				
	}

}
