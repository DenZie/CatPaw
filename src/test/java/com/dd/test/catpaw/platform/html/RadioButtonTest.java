package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

public class RadioButtonTest {

	RadioButton baseRadioButton = new RadioButton(TestObjectRepository.RADIOBUTTON_SPUD_LOCATOR.getValueToUse());
	RadioButton baseRiceRadioButton = new RadioButton(TestObjectRepository.RADIOBUTTON_RICE_LOCATOR.getValueToUse());


	@Test(groups={"browser-tests"})
	@WebTest(sessionName="radioBtntest-flow",keepSessionOpen=true)
	public void radioBtnTestIsChecked(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue(baseRadioButton.isChecked(),"Validate isChecked method");
	}
	@Test(groups={"browser-tests"},dependsOnMethods="radioBtnTestIsChecked")
	@WebTest(sessionName="radioBtntest-flow",openNewSession=false, keepSessionOpen=true)
	public void radioBtnTestCheck(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		baseRiceRadioButton.check();
		assertTrue(baseRiceRadioButton.isChecked(),"Validate check method");

	}
	@Test(groups={"browser-tests"},dependsOnMethods="radioBtnTestCheck")
	@WebTest(sessionName="radioBtntest-flow",openNewSession=false, keepSessionOpen=true)
	public void radioBtnTestClick(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		baseRiceRadioButton.click();
		assertTrue(baseRiceRadioButton.isChecked(),"Validate Click method");
	}
	@Test(groups={"browser-tests"},dependsOnMethods="radioBtnTestClick")
	@WebTest(sessionName="radioBtntest-flow",openNewSession=false)
	public void radioBtnTestClickAndWait(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		baseRiceRadioButton.click(TestObjectRepository.BUTTON_SUBMIT_LOCATOR.getValueToUse());
		assertTrue(baseRiceRadioButton.isChecked(),"Validate Click method");
	}
}
