package com.dd.test.claws.platform.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.utilities.excelDataProvider.USER;

public class ExcelDataProviderTest {
	private static String pathName = "src/test/resources/";
	private static String fileName = "User.xlsx";
	private static final String assertFailedMsg = "Assert condition failed.";
	
	@Test(groups="unit")
	public void testGetSingleExcelRowWithIndexFirstRowCondition() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = new Object[][]{{dataSource.getSingleExcelRow(new USER(), 1)}};
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"Thomas"}, fetchedNames.toArray()), assertFailedMsg);
	}

	@Test(groups="unit")
	public void testGetSingleExcelRowWithIndex() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = new Object[][]{{dataSource.getSingleExcelRow(new USER(), 4)}};
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"suri"}, fetchedNames.toArray()), assertFailedMsg);
	}
	
	@Test(groups="unit")
	public void testGetSingleExcelRowWithKeyFirstRowCondition() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = new Object[][]{{dataSource.getSingleExcelRow(new USER(), "tom")}};
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"Thomas"}, fetchedNames.toArray()), assertFailedMsg);
	}

	@Test(groups="unit")
	public void testGetSingleExcelRowWithKey() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = new Object[][]{{dataSource.getSingleExcelRow(new USER(), "3")}};
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"suri"}, fetchedNames.toArray()), assertFailedMsg);
	}

	//Not a good way to test for Exception being thrown. But the method has been written such that
	//it throws only Exception, when it should have thrown a specific exception.
	//This would need to be dealt with, when we revisit the entire Claws Framework
	//for standardization of Exceptions in the framework
	
	@Test(expectedExceptions={Exception.class},groups="unit")
	public void testGetSingleExcelRowWithInvalidKey() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		dataSource.getSingleExcelRow(new USER(), "claws");
	}

	@Test(groups="unit")
	public void testGetSingleExcelRowWithInvalidIndex() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		assertNull(dataSource.getSingleExcelRow(new USER(), 100),"Returned data should have been null");
	}
	
	@Test(expectedExceptions={IllegalArgumentException.class},groups="unit")
	public void testGetExcelRowsNegativeConditions() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		dataSource.getExcelRows(new USER(), "2~3");
		
	}
	@Test(groups="unit")
	public void testGetExcelRowsWithKeys() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = dataSource.getExcelRows(new USER(), new String[]{"tom","binh"});
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"Thomas","binh"}, fetchedNames.toArray()), assertFailedMsg);
	}
	
	//Not a good way to test for Exception being thrown. But the method has been written such that
	//it throws only Exception, when it should have thrown a specific exception.
	//This would need to be dealt with, when we revisit the entire Claws Framework
	//for standardization of Exceptions in the framework
	@Test(expectedExceptions={Exception.class},groups="unit")
	public void testGetExcelRowsWithInvalidKeys() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		dataSource.getExcelRows(new USER(), new String[]{"claws"});
		
	}
	
	@Test(groups="unit")
	public void testGetExcelRowsWithIndividualIndexes() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = dataSource.getExcelRows(new USER(), "2,3");
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"rama","binh"}, fetchedNames.toArray()), assertFailedMsg);
	}
	
	public synchronized List<String> transformExcelDataIntoList(Object[][] allUsers){
		List<String> fetchedNames = new ArrayList<String>();
		for (Object[] object : allUsers){
			USER user = (USER) object[0];
			fetchedNames.add(user.getName());
		}
		return fetchedNames;
	}
	@Test(groups="unit")
	public void testGetExcelRowsWithRangeOfIndexes() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = dataSource.getExcelRows(new USER(), "1-2");
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"Thomas","rama"}, fetchedNames.toArray()), assertFailedMsg);
	}
	
	@Test(groups="unit")
	public void testGetExcelRowsWithIndividualAndRangeOfIndexes() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = dataSource.getExcelRows(new USER(), "1-2,4,6");
		List<String> fetchedNames = transformExcelDataIntoList(allUsers);
		assertTrue(arrayComparer(new String[]{"Thomas","rama","suri","suri"}, fetchedNames.toArray()), assertFailedMsg);
	}
	
	@Test(groups="unit")
	public void testGetExcelRowsWhereRowIsNull() throws Exception{
		ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
		Object[][] allUsers = dataSource.getExcelRows(new USER(), "5");
		assertNull(allUsers[0][0], assertFailedMsg);
		
	}
	
	
	private synchronized boolean arrayComparer(String[] expected, Object[] actual){
		boolean isSame = false;
		for (int i = 0; i < expected.length; i ++){
			isSame = expected[i].matches((String)actual[i]);
		}
		return isSame;
	}

  @Test(groups="unit")
  public void testGetAllExcelRows() throws Exception{
	  ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
	  Object[][] allUsers = dataSource.getAllExcelRows(new USER());
	  assertNotNull(allUsers,"Data read from excel sheet failed");
	  //Reduce 2 from the actual count, since the test excel sheet has 1 blank row
	  // and 1 row for header
	  assertEquals(allUsers.length, getRowCountFromSheet(USER.class.getSimpleName())-1,"Failed reading all rows from spreadsheet");
  }
  @Test(groups="unit")
  public void testGetAllRowsAsHashTable() throws Exception{
	  ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
	  Hashtable<String, Object> allValues = dataSource.getAllRowsAsHashTable(new USER());
	  assertNotNull(allValues,"Data read from excel sheet failed");
	  assertEquals(allValues.size(), getRowCountFromSheet(USER.class.getSimpleName())-2,"Failed reading all rows from spreadsheet");
  }
  @Test(expectedExceptions={IllegalArgumentException.class},groups="unit")
  public void testGetAllRowsAsHashTableInvalidSheetName() throws Exception{
	  ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
	  Student student = new ExcelDataProviderTest().new Student();
	  dataSource.getAllRowsAsHashTable(student);
  }
  
  @Test(expectedExceptions={IllegalArgumentException.class},groups="unit")
  public void testGetallExcelRowsInvalidSheetName() throws Exception{
	  ExcelDataProvider dataSource = new ExcelDataProvider(pathName, fileName);
	  Student student = new ExcelDataProviderTest().new Student();
	  dataSource.getAllExcelRows(student);
  }
  
  @Test(expectedExceptions={IllegalArgumentException.class},groups="unit")
  public void negativeTestsWithExcelDataProviderConstructor() throws Exception{
	  new ExcelDataProvider(null);
  }
  
  @Test(expectedExceptions={IOException.class},groups="unit")
  public void negativeTestsInvalidFileName() throws IOException{
	  new ExcelDataProvider(null, "IdontExist.xls");
  }
  
  private int getRowCountFromSheet(String sheetName){
	  int rowCount = 0;
	  try {
		XSSFWorkbook workBook = new XSSFWorkbook(pathName + fileName);
		rowCount = workBook.getSheet(sheetName).getPhysicalNumberOfRows();
	} catch (IOException e) {
		//do nothing with the exception here
	}
	  
	  return rowCount;
  }
  public class Student{
	  private String studentName;
	  public void setStudentName(String name){
		  this.studentName = name;
	  }
	  public String getStudentName(){
		  return this.studentName;
	  }
  }
}
