package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertFalse;
import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * This class test the CheckBox class methods.
 */
public class CheckBoxTest {

	CheckBox beansCheckBox = new CheckBox(TestObjectRepository.CHECKBOX_BEANS_LOCATOR.getValueToUse());
	CheckBox chilliCheckBox = new CheckBox(TestObjectRepository.CHECKBOX_CHILLI_LOCATOR.getValueToUse());
	
	@Test(groups={"browser-tests"})
	@WebTest(sessionName="checkbox-test-flow",keepSessionOpen=true)
	public void chkboxTestIsEnabled() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue(beansCheckBox.isEnabled(), "Validate isEnabled method");

	}
	@Test(groups={"browser-tests"},dependsOnMethods="chkboxTestIsEnabled")
	@WebTest(sessionName = "checkbox-test-flow", openNewSession = false, keepSessionOpen = true)
	public void chkboxTestCheck() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		chilliCheckBox.check();
		assertTrue(chilliCheckBox.isChecked(), "Validate Check method");
	}

	@Test(groups={"browser-tests"},dependsOnMethods="chkboxTestCheck")
	@WebTest(sessionName = "checkbox-test-flow", openNewSession = false, keepSessionOpen = true)
	public void chkboxTestUnCheck() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		beansCheckBox.uncheck();
		assertFalse(beansCheckBox.isChecked(), "Validate Uncheck method");
	}
	@Test(groups={"browser-tests"},dependsOnMethods="chkboxTestUnCheck")
	@WebTest(sessionName = "checkbox-test-flow", openNewSession = false, keepSessionOpen = true)
	public void chkboxTestClick() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		chilliCheckBox.click();
		assertTrue(chilliCheckBox.isChecked(), "Validate Click method");

	}
	@Test(groups={"browser-tests"},dependsOnMethods="chkboxTestClick")
	@WebTest(sessionName = "checkbox-test-flow", openNewSession = false, keepSessionOpen = true)
	public void chkboxTestClickAndWait() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		chilliCheckBox.click(beansCheckBox.getLocator());
		assertTrue(chilliCheckBox.isChecked(), "Validate Click(Object..expected) method");

	}
	@Test(groups={"browser-tests"},dependsOnMethods="chkboxTestClickAndWait")
	@WebTest(sessionName = "checkbox-test-flow", openNewSession = false, keepSessionOpen = true)
	public void chkboxTestCheckAndWait() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		chilliCheckBox.check(beansCheckBox.getLocator());
		assertTrue(beansCheckBox.isChecked(), "Validate Check(Object...expected) method");

	}
	@Test(groups={"browser-tests"},dependsOnMethods="chkboxTestCheckAndWait")
	@WebTest(sessionName = "checkbox-test-flow", openNewSession = false)
	public void chkboxTestUnCheckAndWait() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		beansCheckBox.uncheck(chilliCheckBox.getLocator());
		assertFalse(beansCheckBox.isChecked(), "Validate uncheck(Object...expected) method");
		new ButtonTest().flushAllAlerts();
	}

}
