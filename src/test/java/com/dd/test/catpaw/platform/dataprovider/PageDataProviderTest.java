package com.dd.test.claws.platform.dataprovider;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PageDataProviderTest {
	public Map<String, String> myMap = null, myYamlMap = null, myLocalizedYamlMap = null;

	@BeforeMethod(groups = { "unit" })
	public void setUp() {
		try {
			myMap = new GUIMapExcelDataProvider("PayPalProfilePage.xls").getDataProviderValues("US");
			myYamlMap = new GUIMapYAMLDataProvider("PayPalProfilePage.yaml").getDataProviderValues("US");
			myLocalizedYamlMap = new GUIMapYAMLDataProvider("PayPalProfilePage.yaml").getDataProviderValues("FR");

		} catch (IOException e) {
			fail(e.toString());
		}

	}

	@Test(groups = { "unit" })
	public void testLoadGuiMap() {
		assertNotNull(myMap);
		assertNotNull(myYamlMap);
	}

	@Test(dependsOnMethods = { "testLoadGuiMap" })
	public void testGetValues() {
		String value = myMap.get("CreditCardsLink");
		assertEquals(value,"link=Credit/Debit Cards");

	}

	@Test
	public void testOneCannotRetrieveValuesForKeysNotSpecifiedInDataSheet() {
		// foo does not exists in the sheet
		String value = myMap.get("foo");
		assertNull(value);
		Reporter.log("The key and values are : " + myMap,true);
	}

	@Test(groups = { "unit" })
	public void testOneCanReadValuesFromExcel2003() {
		String value = myMap.get("CreditCardsLink");
		assertEquals(value, "link=Credit/Debit Cards", "passed: ");
	}

	@Test(expectedExceptions = { IOException.class })
	public void testFileOpenDataProviderException() throws IOException {
		myMap = new GUIMapExcelDataProvider("foo.xlsx").getDataProviderValues("US");
	}

	/*
	 * IllegalArgumentException should be thrown when trying to non existence
	 * locale
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testForNonExistanceLocale() throws IOException {
		myMap = new GUIMapExcelDataProvider("PayPalProfilePage.xlsx").getDataProviderValues("IN");
	}

	/*
	 * Check can one read values from .csv or .txt
	 */
	@Test(expectedExceptions = { IOException.class })
	public void testCheckForUnSupporteFiles() throws IOException {
		myMap = new GUIMapExcelDataProvider("PayPalProfilePage.csv").getDataProviderValues("IN");
	}

	@Test
	public void testOneCanReadValuesFromMulipleSheet() {
		try {
			myMap = new GUIMapExcelDataProvider("PayPalProfilePage.xlsx").getDataProviderValues("PayPalBillingPage", "US");
			String value = myMap.get("BillCurrencyBalancesLink");
			assertEquals(value,"link=BillCurrency Balances");
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testOneCanReadValuesFromMultipleLocales() {
		try {
			myMap = new GUIMapExcelDataProvider("PayPalProfilePage.xlsx").getDataProviderValues("PayPalProfilePage",
					"FR");
			String value = myMap.get("CreditCardsLink");
			assertEquals(value,"link= French Credit/Debit Cards");
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testOneCanReadValuesFromMulipleSheetExcel2003() {
		try {
			myMap = new GUIMapExcelDataProvider("PayPalProfilePage.xls").getDataProviderValues("PayPalBillingPage","US");
			String value = myMap.get("BillCurrencyBalancesLink");
			assertEquals(value, "link=BillCurrency Balances2003");
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testOneCanReadValuesFromMultipleLocalesExcel2003() {
		try {
			myMap = new GUIMapExcelDataProvider("PayPalProfilePage.xls").getDataProviderValues("FR");
			String value = myMap.get("CreditCardsLink");
			assertEquals(value,"link= French Credit/Debit Cards2003");
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	@Test(groups = { "unit" })
	public void testLoadYamlGUIMap() {
		assertNotNull(myYamlMap);
		assertNotNull(myLocalizedYamlMap);
	}

	@Test(dependsOnMethods = { "testLoadYamlGUIMap" })
	public void testYamlGetValues() {
		String value = myYamlMap.get("BankAccountLink");
		assertEquals(value, "link=Bank Accounts");
	}

	@Test(dependsOnMethods = { "testLoadYamlGUIMap" })
	public void testYamlLocalizedGetValues() {
		String value = myLocalizedYamlMap.get("BankAccountLink");
		assertEquals(value,"link=French Bank Accounts");
	}

}
