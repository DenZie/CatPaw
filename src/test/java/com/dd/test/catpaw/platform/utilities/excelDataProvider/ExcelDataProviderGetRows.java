package com.dd.test.claws.platform.utilities.excelDataProvider;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.utilities.ExcelDataProvider;

public class ExcelDataProviderGetRows {

	String fileName = "User.xlsx";
	ExcelDataProvider excelDataProvider;

	@DataProvider(parallel = true)
	public Object[][] getExcelDataRowsByKeys() throws Exception {
		Object[][] object = null;
		String[] keys = { "1", "binh" };
		object = excelDataProvider.getExcelRows(new USER(), keys);

		return object;

	}

	@Test(dataProvider = "getExcelDataRowsByKeys")
	public void getExcelDataRowsByKeys(USER myData) throws Exception {
		Assert.assertNotNull(myData, "The user data object should not have been null");
		Reporter.log(myData.getName(), true);
		Assert.assertNotNull(myData.getName(), "User Name should not have been null");
		Assert.assertNotNull(myData.getPassword(), "Password should not have been null");
		for (int j = 0; j < myData.getAreaCode().length; j++) {
			Assert.assertNotNull(myData.getAreaCode()[j].getAreaCode(), "Area code should not have been null");
		}
	}

	@DataProvider(parallel = true)
	public Object[][] getExcelDataRowsByIndexes() throws Exception {
		Object[][] object = null;
		String indexes = "2, 3-4";
		object = excelDataProvider.getExcelRows(new USER(), indexes);

		return object;

	}

	@Test(dataProvider = "getExcelDataRowsByIndexes")
	public void getExcelDataRowsByIndexes(USER myData) throws Exception {

		Assert.assertNotNull(myData, "The user data object should not have been null");
		Reporter.log(myData.getName(), true);
		Assert.assertNotNull(myData.getName(), "User Name should not have been null");
		Assert.assertNotNull(myData.getPassword(), "Password should not have been null");
		for (int j = 0; j < myData.getAreaCode().length; j++) {
			Assert.assertNotNull(myData.getAreaCode()[j].getAreaCode(), "Area code should not have been null");
		}

	}

	@DataProvider(parallel = true)
	public Object[][] getAllExcelRows() throws Exception {
		Object[][] object = null;
		object = excelDataProvider.getAllExcelRows(new USER());

		return object;

	}

	@Test(dataProvider = "getAllExcelRows")
	public void getAllExcelRows(USER myData) throws Exception {
		Assert.assertNotNull(myData, "The user data object should not have been null");
		Reporter.log(myData.getName(), true);
		Assert.assertNotNull(myData.getName(), "User Name should not have been null");
		Assert.assertNotNull(myData.getPassword(), "Password should not have been null");
		for (int j = 0; j < myData.getAreaCode().length; j++) {
			Assert.assertNotNull(myData.getAreaCode()[j].getAreaCode(), "Area code should not have been null");
		}
	}

	@BeforeClass
	public void beforeClass() throws IOException {
		excelDataProvider = new ExcelDataProvider(fileName);
	}

}
