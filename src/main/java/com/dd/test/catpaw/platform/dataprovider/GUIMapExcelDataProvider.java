package com.dd.test.catpaw.platform.dataprovider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Concrete DataProvider implementation for read data from the Excel sheets.
 */
public class GUIMapExcelDataProvider implements DataProvider {

	private InputStream inputStream;
	private Workbook workBook;
	
//	// private static SimpleLogger logger = CatPawLogger.getLogger();

	/**
	 * This is a public constructor to create an input stream & Workbook
	 * instance based on the type of input file.
	 * 
	 * @param fileName
	 *            the name of the Excel workbook data file.
	 * @throws IOException
	 */
	public GUIMapExcelDataProvider(String fileName) throws IOException {
//		// logger.entering(fileName);

		inputStream = getInputFileStream(fileName);
		if (inputStream == null) {
			FileNotFoundException e =  new FileNotFoundException("Resource " + fileName + " not found");
	//		// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		if (fileName.toLowerCase().endsWith("xlsx") == true) {
			workBook = new XSSFWorkbook(getInputFileStream(fileName));
		} else if (fileName.toLowerCase().endsWith("xls") == true) {
			workBook = new HSSFWorkbook(getInputFileStream(fileName));
		}
		inputStream.close();
//		// logger.exiting();
	}
	//TODO this method can be removed once the generic service caller logic has been implemented
	// within Jaws,because Jaws now would have a new method which would do this.
	private InputStream getInputFileStream(String fileName) {
//		// logger.entering(fileName);
		ClassLoader loader = this.getClass().getClassLoader();
		inputStream = loader.getResourceAsStream(fileName);
//		// logger.exiting(inputStream);
		return inputStream;
	}

	private HashMap<String, String> buildMapKeyAndValues(Sheet sheet, String locale) {
//		// logger.entering(new Object[]{sheet, locale});

		HashMap<String, String> instanceMap = new HashMap<String, String>();

		int colNum = returnColNumber(sheet, locale);

		String key = null;
		String value = null;

		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
			Row row = sheet.getRow(i);
			if (row!=null && row.getCell(0) != null) {
				/*
				 * The first column is the key which is used to retrieve the
				 * value from the other column from the same row. To determine
				 * which other column to extract the data, this tool needs to
				 * know the column header of the desired column by the input
				 * parameter "site" or "local".
				 */
				key = sheet.getRow(i).getCell(0).getRichStringCellValue().getString().trim();

				try {
					value = sheet.getRow(i).getCell(colNum).getRichStringCellValue().getString();

				} catch (NullPointerException e) {
					// It is the user responsibility to make sure that
					// the blank values in the sheet are not being
					// used later on.
					value = "null";
				}

				instanceMap.put(key, value);
			}

		}
//		// logger.exiting(instanceMap);
		return instanceMap;

	}

	private int returnColNumber(Sheet sheet, String locale) {
//		// logger.entering(new Object[]{sheet, locale});

		int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();
		int colNum = -1;
		for (int col = 1; col < totalCols; col++) {
			if (locale.compareTo(sheet.getRow(0).getCell(col).getRichStringCellValue().getString()) == 0) {
				colNum = col;
				break;
			}

		}

		if (colNum == -1) {
			IllegalArgumentException e = new IllegalArgumentException("The locale: " + locale + " does not exist, please add it the workbook");
	//		// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
//		// logger.exiting(colNum);
		return colNum;
	}

	/**
	 * The user needs to provide the locale for which data needs to be read.
	 * After successfully reading the data from the input stream, it is placed
	 * in the hash map and return to the users.
	 * 
	 * @param locale
	 *            Signifies the language or country column on a sheet to read.
	 */
	public HashMap<String, String> getDataProviderValues(String locale) {
//		// logger.entering(locale);
		HashMap<String, String> returnValue = null;
		Sheet sheet = workBook.getSheetAt(0);

		returnValue =  buildMapKeyAndValues(sheet, locale);
//		// logger.exiting(returnValue);
		return returnValue;

	}

	/**
	 * The user needs to provide the sheet name and locale for which data needs
	 * to be read. After successfully reading the data from the input stream, it
	 * is placed in the hash map and return to the users.
	 * 
	 * @param sheetName
	 *            Name of the Excel Sheet
	 * @param locale
	 *            Signifies the language or country column on a sheet to read.
	 */
	public HashMap<String, String> getDataProviderValues(String sheetName, String locale) {
//		// logger.entering(new Object[]{sheetName, locale});
		HashMap<String, String> returnValue = null;

		Sheet sheet = workBook.getSheet(sheetName);
		returnValue =  buildMapKeyAndValues(sheet, locale);
//		// logger.exiting(returnValue);
		return returnValue;

	}
}
