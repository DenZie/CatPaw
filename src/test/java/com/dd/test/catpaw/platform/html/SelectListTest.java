package com.dd.test.claws.platform.html;


/**
 * This class test the SelectList class methods
 */

import org.testng.annotations.Test;
import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;
import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

public class SelectListTest {
	static final String sByVal = "White";
	static final String sExpected ="Red";
	static final String sLabel="black";
	SelectList normalSelectList = new SelectList(TestObjectRepository.SELECTLIST_LOCATOR.getValueToUse());
	
	@Test(groups={"browser-tests"})
	@WebTest(sessionName="selectlisttest-flow",keepSessionOpen=true)
	public void selectListTestSelectByValue(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalSelectList.selectByValue(sByVal);
		assertTrue(normalSelectList.getSelectedValue().matches(sByVal),"Validate SelectByValue method");
	}
	@Test(groups={"browser-tests"},dependsOnMethods="selectListTestSelectByValue")
	@WebTest(sessionName="selectlisttest-flow",openNewSession=false, keepSessionOpen=true)
	public void selectListTestSelecByIndex(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalSelectList.selectByIndex(1);
		assertTrue(normalSelectList.getSelectedLabel().matches(sExpected),"Validate SelectByIndex method");
		
	}
	@Test(groups={"browser-tests"},dependsOnMethods="selectListTestSelecByIndex")
	@WebTest(sessionName="selectlisttest-flow",openNewSession=false)
	public void selectListTestSelecByLabel(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		normalSelectList.selectByLabel(sLabel);
		assertTrue(normalSelectList.getSelectedValue().matches(sLabel),"Validate SelectByLabel method");
		
	}
}
