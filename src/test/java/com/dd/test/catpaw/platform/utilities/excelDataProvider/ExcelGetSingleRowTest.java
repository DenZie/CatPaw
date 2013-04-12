package com.dd.test.claws.platform.utilities.excelDataProvider;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.utilities.ExcelDataProvider;

public class ExcelGetSingleRowTest {

	String pathName = "src/test/resources/";
	String fileName = "User.xlsx";
	ExcelDataProvider excelDataProvider;

	private void printInformation(USER data) {
		Reporter.log("User name is: " + data.getName(), true);
		Assert.assertNotNull(data.getName());
		Reporter.log("User password is: " + data.getPassword(), true);
		Assert.assertNotNull(data.getPassword());
		Reporter.log("Test integer: " + data.getPreintTest(), true);
		Assert.assertNotNull(data.getPreintTest());
		Reporter.log("Test boolean: " + data.getIsBooleanGood(), true);
		Assert.assertNotNull(data.getIsBooleanGood());
		Reporter.log("Test double: " + data.getDoubleTest(), true);
		Assert.assertNotNull(data.getDoubleTest());
		Reporter.log("Test long: " + data.getLongTest(), true);
		Assert.assertNotNull(data.getLongTest());
		Reporter.log("Test float: " + data.getFloatTest(), true);
		Assert.assertNotNull(data.getFloatTest());
		Reporter.log("Test byte: " + data.getByteTest(), true);
		Assert.assertNotNull(data.getByteTest());
		for (int i = 0; i < data.getAreaCode().length; i++) {
			Reporter.log("User areacode [" + i + "] is: " + data.getAreaCode()[i].getAreaCode(), true);
			Assert.assertNotNull(data.getAreaCode()[i].getAreaCode());
		}
	}

	@Test
	public void getSingleRowTest() throws Exception {
		Object obj;
		obj = excelDataProvider.getSingleExcelRow(new USER(), "binh");

		USER myData = (USER) obj;

		printInformation(myData);
	}

	@Test
	public void getSingleRowsByIndexTest() throws Exception {
		Object obj;
		obj = excelDataProvider.getSingleExcelRow(new USER(), 5);
		USER myData = (USER) obj;
		printInformation(myData);
	}

	@Test
	public void getSingleRowByKeyTest() throws Exception {
		Object obj;
		obj = excelDataProvider.getSingleExcelRow(new USER(), "3");

		USER myData = (USER) obj;
		printInformation(myData);

	}

	@BeforeClass
	public void beforeClass() throws IOException {
		excelDataProvider = new ExcelDataProvider(pathName, fileName);
	}

	@AfterClass
	public void afterClass() {
		excelDataProvider.closeFileHandle();
	}
}
