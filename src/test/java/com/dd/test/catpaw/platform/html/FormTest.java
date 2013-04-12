package com.dd.test.claws.platform.html;

import org.openqa.selenium.UnhandledAlertException;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * This class test the Form class methods
 */
public class FormTest {
	//submit() method will throw up exceptions when it encounters Alerts
	@Test(groups={"ie-broken-test"},expectedExceptions={UnhandledAlertException.class})
	@WebTest
	public void testSubmitNegativeTest(){
		Form searchForm =new Form(TestObjectRepository.FORM_SEARCH.getValueToUse()); 
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		searchForm.submit();
	}
}
