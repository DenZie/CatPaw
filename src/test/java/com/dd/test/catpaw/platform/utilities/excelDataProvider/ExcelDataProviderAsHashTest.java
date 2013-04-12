package com.dd.test.claws.platform.utilities.excelDataProvider;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.utilities.ExcelDataProvider;

/**
 * This example will read the whole excel sheet and put all data rows into a hash table with the keys are the value of
 * the cells in the first column.
 * 
 * User can have this hash table and get the desired row all by themself by providing a key to the hash table get
 * function. This way, the users can bypass the TestNG DataProvider to manuall assign row to each test case.
 * 
 */
public class ExcelDataProviderAsHashTest {

	/**
	 * Globally declaring objects here since many they are used in many functions. But they will be initialized before
	 * running the test.
	 * 
	 * pathName is the directory where the Excel workbook resides fileName is the name of the Excel workbook about to be
	 * read excelDataProvider is the instantiation of the ExcelProvider object. hashTable contains the returned data
	 * from the workbook myData is the user define-data-type which is to be filled by the data from the Excel workbook.
	 * 
	 */
	String pathName = null;
	String fileName = null;
	ExcelDataProvider excelDataProvider = null;
	Hashtable<?, ?> hashTable = null;
	USER myData = null;

	private void printInformation(USER data) {
		Reporter.log("Name: " + data.getName(), true);
		Assert.assertNotNull(data.getName());
		Reporter.log("Password: " + data.getPassword(), true);
		Assert.assertNotNull(data.getPassword());
		Reporter.log("Areacode: " + data.getAreaCode()[0].getAreaCode(), true);
		Assert.assertNotNull(data.getAreaCode()[0].getAreaCode());
	}

	@Test
	public void getAllRowsAsHash() throws Exception {
		Hashtable<String, Object> hashTable;
		hashTable = excelDataProvider.getAllRowsAsHashTable(new USER());
		Assert.assertNotNull(hashTable);
	}

	/**
	 * After intialized the hashTable and filled it up with all the data from the Excel worksheet, we can use the native
	 * function "get" from the hashtable to retrieve the data. Since the hash table stored our data as key-value pair,
	 * all you need to retrieve a value is supplying a key. Hash table will return a generic object with a specified
	 * key. In this test case, the key is "binh", and the value is a whole structure of user-defined-data "USER" is
	 * returned and mapped into "myData" before used.
	 * 
	 */
	@Test
	public void getSheetAsHashByKeyTest1() {
		USER myData = null;

		myData = (USER) hashTable.get("binh");
		Reporter.log("From getSheetAsHashByKeyTest1:", true);
		printInformation(myData);
	}

	@Test(groups = { "unit" })
	public void getSheetAsHashByKeyTest2() {
		USER myData = null;

		myData = (USER) hashTable.get("1");
		Reporter.log("From getSheetAsHashByKeyTest2:", true);
		printInformation(myData);
	}

	/**
	 * Before running the test, we want to initialize our global variables The path name is set to "src/test/resource"
	 * since we already knew the parent directory, "Claws-Core". which is the directory where this test case started.
	 * So we don't have to specify the whole path name here. The fileName is set to the name of the Excel workbook about
	 * to read. With the pathName and fileName the ExcelDataProvider construtor will construct a full qualified path
	 * name to the file to be read.
	 * 
	 * After we instantiated the ExcelDataProvider, we can use it to invoke "getAllRowsAsHashTable". For the function to
	 * work, you have to pass in the "user-defined-data-type" so it can reverse engineer it and know exactly what kind
	 * of data it should assign to each field while reading the data from the Excel worksheet.
	 * 
	 * The end results are assigned back to the hashTable for further exploring.
	 * 
	 * Reading data from the Excelsheet could generate several exception. For example, file not found, cannot read from
	 * file, invalid value...
	 * 
	 * @throws Exception
	 *             if file reading errors.
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {

		// Initialize golbal data before using them
		pathName = "src/test/resources/";
		fileName = "User.xlsx";
		myData = new USER();

		try {
			// Construct the data object.
			excelDataProvider = new ExcelDataProvider(pathName, fileName);
			// Read the data from Excel worksheet and assign it
			// to the global variable for further exploring.
			hashTable = excelDataProvider.getAllRowsAsHashTable(myData);
		} catch (Exception ex) {
			Reporter.log(ex.getLocalizedMessage(), true);
		}

	}

	/**
	 * After all test cases ran, (sucessfully or unsuccessfully) this method will close the file handle and release all
	 * memories acquired by global varibles. We don't have to do this, since Java has a very decent gabage collector to
	 * reclaim the memory after all test cases are done. But if you want to do it yourself, then this is how to.
	 */
	@AfterClass
	public void afterClass() {
		excelDataProvider.closeFileHandle();
		pathName = null;
		fileName = null;
		myData = null;
		hashTable = null;
		excelDataProvider = null;
	}
}
