package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

public class TextAreaTest {
	static final String sTest = "Testing multi line text for TextArea object";
	static final String sLine = "Testing multi line" + "\n" + "text for TextArea" + "\n" + "object";
	TextField normalTextField = new TextField(TestObjectRepository.TEXT_AREA_LOCATOR.getValueToUse());

	@Test(groups = { "browser-tests" })
	@WebTest(sessionName = "txtareatest-flow", keepSessionOpen = true)
	public void textAreaTestTypeText() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalTextField.type(sTest);
		String s = normalTextField.getText();
		assertTrue(s.matches(sTest), "Validate SetText method");
	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "textAreaTestTypeText")
	@WebTest(sessionName = "txtareatest-flow", openNewSession = false, keepSessionOpen = true)
	public void textAreaTestGetText() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalTextField.type(sTest);
		assertTrue(normalTextField.getText().contains(sTest), "Vlaidate GetText method");
	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "textAreaTestGetText")
	@WebTest(sessionName = "txtareatest-flow", openNewSession = false, keepSessionOpen = true)
	public void textAreaTestClearText() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalTextField.type(sTest);
		assertTrue(normalTextField.getText().matches(sTest), "Validate Type method");
		normalTextField.clear();
		assertTrue(normalTextField.getText().length() == 0, "Validate ClearText method");
	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "textAreaTestClearText")
	@WebTest(sessionName = "txtareatest-flow", openNewSession = false, keepSessionOpen = true)
	public void textAreaTestisEditable() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue(normalTextField.isEditable(), "Validate isEditable method");

	}

	@Test(groups = { "browser-tests" }, dependsOnMethods = "textAreaTestisEditable")
	@WebTest(sessionName = "txtareatest-flow", openNewSession = false)
	public void textAreaTestTypeTextNewLine() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalTextField.type(sLine);
		assertTrue(normalTextField.getText().contains("Testing"), "Validate SetText method");

	}
}
