package com.dd.test.claws.platform.html;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

public class AbstractElementTest {
	@Test(groups = { "unit" })
	@WebTest
	public void validateCssLocationStrategy(){
		Grid.open(TestObjectRepository.URL.getValueToUse());
		WebElement e = AbstractElement.locateElement("css=input[name=normal_text]");
		e.sendKeys("beamdaddy@paypal.com");
		assertTrue(e.getAttribute("value").equals("beamdaddy@paypal.com"));
	}
	
	@Test(groups = { "unit" })
	public void validateFindElementType(){
		HashMap<String, String> myElements = new HashMap<String, String>();
		myElements.put("id=foo", ById.class.getCanonicalName());
		myElements.put("name=foo", ByName.class.getCanonicalName());
		myElements.put("link=foo", ByLinkText.class.getCanonicalName());
		myElements.put("xpath=foo", ByXPath.class.getCanonicalName());
		myElements.put("/foo", ByXPath.class.getCanonicalName());
		myElements.put("//foo", ByXPath.class.getCanonicalName());
		myElements.put("css=foo", ByCssSelector.class.getCanonicalName());
		myElements.put("foo", ByIdOrName.class.getCanonicalName());
		Iterator<String> allElements = myElements.keySet().iterator();
		while (allElements.hasNext()){
			String eachElement = allElements.next();
			By b = AbstractElement.getFindElementType(eachElement);
			assertTrue(b.getClass().getCanonicalName().equals(myElements.get(eachElement)));
		}
	}
	

}
