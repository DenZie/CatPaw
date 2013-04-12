package com.dd.test.claws.platform.html;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

/**
 * This class test the Label class methods
 */

public class LabelTest {
	Label editableTestField = new Label(TestObjectRepository.LABEL_EDITABLE.getValueToUse());

	@Test(groups={"browser-tests"})
	@WebTest
	public void testLabel() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue(editableTestField.isTextPresent("Editable text-field"),"Validated isTextPresent method");
	}
}
