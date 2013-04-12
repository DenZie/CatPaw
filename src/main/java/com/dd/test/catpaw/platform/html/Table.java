package com.dd.test.catpaw.platform.html;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;


import com.dd.test.catpaw.platform.grid.Grid;

/**
 * Generates Table object and methods for its functioning
 * 
 */
public class Table extends AbstractElement {
	
	/**
	 * Table Construction method<br>
	 * <b>Usage:</b> <br>
	 * {@code Table tblTransactionTable = new Table( "//TABLE[@id='transactionTable']"}
	 * 
	 * @param locator
	 *            the element locator
	 * 
	 */
	public Table(String locator) {
		super(locator);
	}
	public Table(String locator, String controlName) {
		super(locator, controlName);
	}
	/**
	 * Returns the number of rows in a table.
	 * 
	 * @return int number of rows
	 */
	public int getNumberOfRows() {
		String xPath = getXPathBase() + "tr";
		return locateElements(xPath).size();
	}

	/**
	 * Returns the number of columns in a table.
	 * 
	 * @return int number of columns
	 */
	public int getNumberOfColumns() {
		List<WebElement> cells;
		String xPath = getXPathBase() + "tr";
		
		List<WebElement> elements = locateElements(xPath);

		if(elements.size() > 0) {
			cells = elements.get(1).findElements(By.xpath("td"));
			return cells.size();
		}
		
		return 0;
	}

	/**
	 * Searches all rows from a table for the occurrence of the input search
	 * strings and returns the index to the row containing all the search
	 * strings.<br>
	 * 
	 * <b>Usage:</b> <br>
	 * String[] search = {"Payment To", "-$7.00 USD"};<br>
	 * int searchRow = findRowNumber(search);<br>
	 * 
	 * @param searchKeys
	 *            String[] array with as many values as need to identify the row
	 * @return int number of first row where all conditions were met <br>
	 *         Negative number indicates that row was not found
	 */
	public int getRowIndex(String[] searchKeys) {
		int numKey = searchKeys.length;
		int rowCount = getNumberOfRows();

		String xPathBase, xPath, value;
		xPathBase = getXPathBase();

		int rowIndex = 0;
		for (int i = 1; i <= rowCount; i++) {
			xPath = xPathBase + "tr[" + i + "]";
			// get table row as text
			value = locateElement(xPath).getText();

			// search the table row for the key words
			if (value.length() > 0) {
				for (int s = 0; s < numKey; s++) {
					if (searchKeys[s] != null
							&& ((String) searchKeys[s]).length() > 0) {
						if (value.contains((CharSequence) searchKeys[s])) {
							rowIndex = i;
						} else {
							rowIndex = -1;
							break;
						}
					}
				}
			}
			if (rowIndex > 0)
				break;
		}

		return rowIndex;
	}

	/**
	 * Finds value of a cell in a table indicated by row and column indices.
	 * 
	 * @param row
	 *            int number of row for cell
	 * @param column
	 *            int number of column for cell
	 * @return String value of cell with row and column
	 */
	public String getCellValue(int row, int column) {
		List<WebElement> elements = locateElements(getXPathBase() + "tr");
		List<WebElement> cells = elements.get(row).findElements(By.xpath("td"));

		if (cells.size() > 0)
			return cells.get(column).getText();

		return null;
	}

	/**
	 * Goes to the cell addressed by row and column indices and clicks link in
	 * that cell. Performs wait until page would be loaded
	 * 
	 * @param row
	 *            int number of row for cell
	 * @param column
	 *            int number of column for cell
	 */
	public void clickLinkInCell(int row, int column) {
		String xPath = getXPathBase() + "tr[" + row + "]/td[" + column + "]/a";
		locateElement(xPath).click();

		Grid.driver().waitForPageToLoad(getWaitTime());
	}

	/**
	 * Generates xPath from element locator and path to the row (&lt;tr&gt; tag)<br>
	 * Checks if table has &lt;TBODY&gt; tag and adds it to the xPath<br>
	 * 
	 * @return String with beginning of xPath<br>
	 */
	public String getXPathBase() {
		String xPathBase = "";
		if (this.getElement() != null) {
			
			String locator = getLocator();
			if(!locator.startsWith("link=")&& !locator.startsWith("xpath=")&& !locator.startsWith("/")){
				if (locator.startsWith("id=") || locator.startsWith("name=")){
					String tmp = getLocator();
					tmp= tmp.substring(tmp.indexOf("=",1)+1);
					if (locator.startsWith("id=")){
						xPathBase= "//table[@id='"+ tmp +"']/TBODY/";
					}
					else{
						xPathBase= "//*[@name='"+ tmp +"']/TBODY/";
					}
						
				}
				else{
					xPathBase="//*[@id='"+ super.getLocator()+"']/TBODY/";
				}
						
			}
			else{
				if (locator.startsWith("xpath=")){
					locator= locator.substring(locator.indexOf("=",1)+1);
				}
				if (locateElements(locator + "/TBODY").size() > 0) {
					xPathBase = locator + "/TBODY/";
				} else {
					xPathBase = locator + "//";
				}
			}
		} else {
			throw new NotFoundException("Table" + this.getLocator()
					+ " does not exist.");
		}
		return xPathBase;
	}

	/**
	 * Returns the single row of a table as a long string of text using the
	 * input row index.
	 * 
	 * @param rowIndex
	 *            the index to the row which text is about to retrieve.
	 * @return rowText a text string represents the single row of a table
	 */
	public String getRowText(int rowIndex) {
		String rowText = null;
		String xPath = getXPathBase() + "TR[" + rowIndex + "]";
		rowText = locateElement(xPath).getText();
		
		return rowText;
	}

	/**
	 * Tick the checkbox in a cell of a table indicated by input row and column
	 * indices
	 * 
	 * @param row
	 *            int number of row for cell
	 * @param column
	 *            int number of column for cell
	 */
	public void checkCheckboxInCell(int row, int column) {
		String locator = getXPathBase() + "tr[" + row + "]/td[" + column
				+ "]/input";
		CheckBox cb = new CheckBox(locator);
		cb.check();
		
		Grid.driver().waitForPageToLoad(getWaitTime());
	}

	/**
	 * Untick a checkbox in a cell of a table indicated by the input row and
	 * column indices.
	 *
	 * @param row
	 *            int number of row for cell
	 * @param column
	 *            int number of column for cell
	 */
	public void uncheckCheckboxInCell(int row, int column) {
		String locator = getXPathBase() + "tr[" + row + "]/td[" + column
				+ "]/input";
		CheckBox cb = new CheckBox(locator);
		cb.uncheck();

		Grid.driver().waitForPageToLoad(getWaitTime());
	}

	public String getLocator() {
		return super.getLocator();

	}
}
