package com.dd.test.claws.platform.html;

import static com.dd.test.claws.platform.asserts.ClawsAsserts.assertTrue;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

/**
 * This class test the Table class methods
 * 
 */

public class TableTest {
	Table testTable = new Table(TestObjectRepository.TABLE_LOCATOR.getValueToUse());
	CheckBox selectionCheck = new CheckBox(TestObjectRepository.CHECKBOX_LOCATOR.getValueToUse());
	
	@Test(groups={"browser-tests"})
	@WebTest(sessionName="tabletest-flow",keepSessionOpen=true)
	public void tableTestGetRowCounts() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue((testTable.getNumberOfRows()==4),"Validate getNumberOfRows method");
		
	}
	@Test(groups={"browser-tests"},dependsOnMethods="tableTestGetRowCounts")
	@WebTest(sessionName="tabletest-flow",openNewSession=false, keepSessionOpen=true)
	public void tableTestGetColumnCounts() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue((testTable.getNumberOfColumns()==3),"Validate getNumberOfColumns method");
		
	}
	@Test(groups={"browser-tests"},dependsOnMethods="tableTestGetColumnCounts")
	@WebTest(sessionName="tabletest-flow",openNewSession=false, keepSessionOpen=true)
	public void tableTestGetCellVlaue() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		assertTrue(testTable.getCellValue(1,1).matches("Payment"),"Validate getCellValue method");
		
	}
	@Test(groups={"browser-tests"},dependsOnMethods="tableTestGetCellVlaue")
	@WebTest(sessionName="tabletest-flow",openNewSession=false, keepSessionOpen=true)
	public void tableTestGetRowText() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		Grid.driver().waitUntilElementPresent(testTable.getLocator());
		assertTrue(testTable.getRowText(1).contains("Date"),"Validate getCellValue method");
		
	}
	@Test(groups={"browser-tests"},dependsOnMethods="tableTestGetRowText")
	@WebTest(sessionName="tabletest-flow",openNewSession=false, keepSessionOpen=true)
	public void tableTestClickLink() {
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		testTable.clickLinkInCell(2, 3);
		String title = Grid.driver().getTitle();
		assertTrue(title.matches("Success"),"Validate click Link in table cell");
	}
	@Test(groups={"browser-tests"},dependsOnMethods="tableTestClickLink")
	@WebTest(sessionName="tabletest-flow",openNewSession=false, keepSessionOpen=true)
	public void tableTestCheckCheckBox(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		testTable.checkCheckboxInCell(4, 1);
		assertTrue(selectionCheck.isChecked(),"Validate Checkbox Check method");
	}
	@Test(groups={"browser-tests"},dependsOnMethods="tableTestCheckCheckBox")
	@WebTest(sessionName="tabletest-flow",openNewSession=false, keepSessionOpen=true)
	public void tableTestUnCheckCheckBox(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		testTable.checkCheckboxInCell(4, 1);
		testTable.uncheckCheckboxInCell(4,1);
		assertTrue(!selectionCheck.isChecked(),"Validate Checkbox Uncheck method");
	}
	@Test(groups={"browser-tests"},dependsOnMethods="tableTestUnCheckCheckBox")
	@WebTest(sessionName="tabletest-flow",openNewSession=false)
	public void tableTestGetRowIndex(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		String rowValue[] ={"Date", "Type", "Status"};
		assertTrue((testTable.getRowIndex(rowValue)==1),"Validate Checkbox Clicked");
	}
}
