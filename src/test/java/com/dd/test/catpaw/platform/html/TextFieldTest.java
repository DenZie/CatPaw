package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * This class test the TextField class methods
 * 
 *
 */
public class TextFieldTest {
	static final String sTest = "TestString";
	
	TextField normalTextField = new TextField(TestObjectRepository.TEXTFIELD_LOCATOR.getValueToUse());
	TextField disabledTextField = new TextField(TestObjectRepository.TEXTFIELD_DISABLED_LOCATOR.getValueToUse());
	
	@Test(groups={"browser-tests"})
	@WebTest(sessionName = "txtfieldtest-flow", keepSessionOpen = true)
	public void txtFieldTestTypeText() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalTextField.type(sTest);
		assertTrue(normalTextField.getText().matches(sTest), "Validate SetText method");
		
	}

	@Test(groups={"browser-tests"}, dependsOnMethods="txtFieldTestTypeText")
	@WebTest(sessionName = "txtfieldtest-flow", openNewSession = false, keepSessionOpen = true)
	public void txtFieldTestGetText() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalTextField.type(sTest);
		assertTrue(normalTextField.getText().contains(sTest), "Vlaidate GetText method");
	}

	@Test(groups={"browser-tests"}, dependsOnMethods="txtFieldTestGetText")
	@WebTest(sessionName = "txtfieldtest-flow", openNewSession = false, keepSessionOpen = true)
	public void txtFieldTestClearText() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalTextField.type(sTest);
		assertTrue(normalTextField.getText().matches(sTest), "Validate Type method");
		normalTextField.clear();
		assertTrue(normalTextField.getText().length() == 0, "Validate ClearText method");
	}
	@Test(groups={"browser-tests"}, dependsOnMethods="txtFieldTestClearText")
	@WebTest(sessionName = "txtfieldtest-flow", openNewSession = false)
	public void txtFieldTestisEditable() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue(normalTextField.isEditable(), "Validate isEditable method");
	}

}
