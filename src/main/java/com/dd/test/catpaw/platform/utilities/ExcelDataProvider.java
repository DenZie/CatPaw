package com.dd.test.catpaw.platform.utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




/**
 * This class provide several methods to retrieve test data from an Excel
 * workbook. Users can get a single row of data by providing the excel filename,
 * the data sheet name, and they key. Or get the whole data sheet by providing
 * excel file name and the data sheet name.
 * 
 */

public class ExcelDataProvider {

	private Workbook workBook;
	private InputStream inputStream;
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();

	/**
	 * The constructor will use the path name and the file name of the Excel
	 * workbook to initialize the input stream before the stream is being used
	 * by several methods to get the test data from the Excel workbook.
	 * 
	 * If pathName is not null then the users delibereated specified the
	 * resource file in other location than the classpaths. If pathName is null,
	 * then the resouce file can be found using the classpath.
	 * 
	 * <h3>Sample usage:</h3>
	 * 
	 * <pre>
	 * String   pathName = "src/test/java";
	 * String   fileName = "DataReaderTest.xls"
	 * LOCAL_DATA myData = new LOCAL_DATA();
	 * Object [][] myObj;
	 * 
	 * // To get a single row of excel sheet using a key associated with the data
	 * myData = (LOCAL_DATA) ExcelDataProvider dataProvider = new ExcelDataProvider(
	 *                          pathName, fileName).getSingleExcelRow(myData, "4");
	 * 
	 * // To get a whole sheet of excel data. This will not need key.
	 * myObj =  new ExcelDataProvider(pathName,
	 *                 fileName).getAllExcelRows(myData);
	 * myData = (LOCAL_DATA)myObj[1][0];
	 * System.out.println(myObj.seller.bank[0].name);
	 * </pre>
	 * 
	 * @param pathName
	 *            the path where the excel file is located.
	 * @param fileName
	 *            the name of the excel file to be read.
	 * @throws IOException
	 *             If the file cannot be located, or cannot read by the method.
	 */
	public ExcelDataProvider(String pathName, String fileName)
			throws IOException {
		super();
		// logger.entering(new Object[]{pathName, fileName});
		if ( (fileName == null) || (fileName.trim().isEmpty()) ){
			throw new IllegalArgumentException("fileName cannot be null/empty");
		}

		String resourcePath = fileName;
		String pad = "/";

		// If pathName is not null then the users delibereated
		// specified the resource file in other location
		// than the classpaths.
		// If pathName is null, then the resouce file can be
		// found using the classpath.
		if (pathName != null) {
			// Checking the last "/" from the pathName
			if (pathName.endsWith("/")){
				pad = "";
			}

			resourcePath = pathName + pad + fileName;
		}

		// By default file stream writes one byte of data at a time,
		// BufferedInputStream will buffer the stream and write multi-bytes
		// at a time, this will enhance file throughput
		InputStream fileStream = getInputFileStream(resourcePath);

		if(fileStream == null) {
			IOException e = new IOException("File '" + resourcePath + "' is not found.");
			// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		
		inputStream = new BufferedInputStream(fileStream);

		if (fileName.toLowerCase().endsWith("xlsx")) {
			workBook = new XSSFWorkbook(inputStream);

		} else if (fileName.toLowerCase().endsWith("xls")) {
			workBook = new HSSFWorkbook(inputStream);
		}
		// logger.exiting();
	}

	/**
	 * Use this constructor when a file that is available in the classpath is to be
	 * read by the ExcelDataProvider for supporting Data Driven Tests.
	 *  
	 * @param fileName
	 *            the name of the excel file to be read.
	 * @throws IOException
	 *             If the file cannot be located, or cannot read by the method.
	 */
	public ExcelDataProvider(String fileName) throws IOException {
		this(null, fileName);
	}

	/**
	 * Uses the full path and file name of the excel workbook to creates and
	 * initialize file input stream that other will use to read test data from
	 * the Excel workbook.
	 * 
	 * @param fileName
	 *            the full path name of the Excel file
	 * @return  the input stream resource for reading the Excel file.
	 */
	private InputStream getInputFileStream(String fileName) {
		// logger.entering(fileName);

		ClassLoader loader = this.getClass().getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(fileName);
		if (inputStream == null) {

			try {
				inputStream = new FileInputStream(fileName);

			} catch (FileNotFoundException e) {
				closeFileHandle();
				// logger.exiting(null);
				return null;
			}
		}
		// logger.exiting(inputStream);
		return inputStream;
	}

	/**
	 * This function will closes file input stream and release the file handle
	 * to release the memory used by the stream.
	 * 
	 * <h3>Sample usage:</h3>
	 * 
	 * <pre>
	 * closeFileHandle();
	 * </pre>
	 */
	public void closeFileHandle() {
		// logger.entering();
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// logger.log(Level.SEVERE, "Could not close the file handle: " + e.getMessage(), e);
			}
		}
		// logger.exiting();
	}

	/**
	 * Search for the input key from the specified sheet name and return the
	 * index position of the row that contained the key
	 * 
	 * @param sheetName - A String that represents the Sheet name from which data is to be read
	 * @param key - A String that represents the key for the row for whcih search is being done.
	 * @return - An int that represents the row number for which the key matches. Returns -1 if the search
	 * did not yield any results.
	 * 
	 * @throws Exception
	 */
	private int getRowIndex(String sheetName, String key) throws Exception {
		// logger.entering(new Object[]{sheetName, key});
		int index = -1;
		Sheet sheet = fetchSheet(sheetName);
		
		int rowCount = sheet.getPhysicalNumberOfRows();
		for (int i = 0; i < rowCount; i++) {
			Row row = sheet.getRow(i);
			if (row == null){
				continue;
			}
			String cellValue = row.getCell(0).toString();
			if ((key.compareTo(cellValue) == 0)
					&& (cellValue.contains("#") == false)) {
				index = i;
				break;
			}
		}
		// logger.exiting(index);
		return index;
	}
	
	/**
	 * A utility method, which returns {@link Sheet} for a given sheet name.
	 * @param sheetName - A string that represents a valid sheet name.
	 * 
	 * @return - An object of type {@link Sheet}
	 */
	private Sheet fetchSheet(String sheetName){
		// logger.entering(new Object[]{sheetName});
		Sheet sheet = workBook.getSheet(sheetName);

		if (sheet == null) {
			IllegalArgumentException e = new IllegalArgumentException("Sheet '" + sheetName + "' is not found.");
			// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		// logger.exiting(sheet);
		return sheet;
	}

	/**
	 * This function will parse the index string in to separated individual
	 * index as needed. Call the method with a string contains
	 * "1, 3, 5-7, 11, 12-14, 8" would return a list of integer as {1, 3, 5, 6,
	 * 7, 11, 12, 13, 14, 8} <h3>Sample usage:</h3>
	 * 
	 * 
	 * @param value
	 *            the input string represent the indexes to be parse.
	 * @return
	 */
	private List<Integer> parseIndexString(String value) {
		// logger.entering(value);
		ArrayList<Integer> rows = new ArrayList<Integer>();
		int begin, end;
		String[] parsed;
		String[] parsedIndex = value.split(",");
		for (String index : parsedIndex) {
			if (index.contains("-")) {
				parsed = index.split("-");
				begin = Integer.parseInt(parsed[0].trim());
				end = Integer.parseInt(parsed[1].trim());
				for (int i = begin; i <= end; i++)
					rows.add(i);
			} else
				try{
					rows.add(Integer.parseInt(index.trim()));
				}catch(NumberFormatException exception){
					String msg = "Index '" + index + "' is invalid. Please provide either individual numbers or ranges.";
					msg += "Range needs to be de-marked by '-'";
					IllegalArgumentException e = new IllegalArgumentException(msg);
					// logger.log(Level.SEVERE, e.getMessage(), e);
					throw e;					
				}

		}
		// logger.exiting(rows);
		return rows;
	}

	/**
	 * This function will read all rows of a specified excel sheet and store the
	 * data to a hash table. Users can get a row of data from the hash table by
	 * call a get with a specified key. This excel reader function is for users
	 * who want to control the data feed to the test cases manually without the
	 * benefit of TestNG DataProvider. <br> <br>
	 * <b>Note:</b> Unlike {@link ExcelDataProvider#getAllExcelRows(Object)} this method will skip ALL blank rows
	 * that may occur in between data in the spreadsheet. <br>
	 * Hence the number of rows that are fetched by this method and {@link ExcelDataProvider#getAllExcelRows(Object)}
	 * <b>NEED NOT</b> be the same.
	 * 
	 * <h3>Example:</h3>
	 * 
	 * <pre>
	 * ...
	 * MyDataStructure myObj = new MyDataStructure();
	 * HashTable&lt;String, Object&gt; myExcelTableData;
	 * ...
	 * myExceltableData = ExcelDataProvider.getAllRowAsHasTable(myObj);
	 * </pre>
	 * 
	 * @param myObj
	 *            the user defined type object which provide details structure
	 *            to this function.
	 * @return an object of type {@link Hashtable} that represents the excel sheet data in form of hashTable.
	 * @throws Exception
	 *             if invalid class name from input parameter myObj
	 */
	public Hashtable<String, Object> getAllRowsAsHashTable(Object myObj)
			throws Exception {
		// logger.entering(myObj);
		Hashtable<String, Object> hashTable = new Hashtable<String, Object>();

		Class<?> cls = Class.forName(myObj.getClass().getName());
		Sheet sheet = fetchSheet(cls.getSimpleName());
		int numRows = sheet.getPhysicalNumberOfRows();
		
		for (int i = 2; i <= numRows; i++) {
			Row row = sheet.getRow(i-1);
			if ((row != null ) && (row.getCell(0) != null)){
				Object obj = getSingleExcelRow(myObj, i, false);
				String key = row.getCell(0).toString();
				if ((key != null) && (obj != null)){
					hashTable.put(key, obj);
				}
			}
		}
		// logger.exiting(hashTable);
		return hashTable;

	}

	/**
	 * Currently this function will handle these data types:
	 * <ul>
	 * <li>1. Primitive data type: int, boolean, double, float, long</li>
	 * <li>2. Object data type: String, Integer, Double, Float, Long</li>
	 * <li>3. Jaws object data type: Country, Currency, BankAccountType,
	 * CreditCardType, PPAccountType </li>
	 * <li>4. Array of primitive data type: int[], boolean[], double[], float[],
	 * long[]</li>
	 * <li>5. Array of object data type: String[], Integer[], Boolean[],
	 * Double[], Float[], Long[]</li>
	 * <li>6. Array od Jaws object data type: Country[], Currency[],
	 * BankAccountTypep[], CreditCardType[], PPAccountTYpe[]</li>
	 * <li>7. User defined data type.</li>
	 * <li>8. Array of user defined data type.</li>
	 * </ul>
	 * 
	 * 
	 * @param userObj
	 *            this object is used by the function to extract the object
	 *            info, such as class name, objects declarations, object data
	 *            structure...
	 * @param fields
	 *            the array contains the list of name in the specify data structure
	 * @param excelRowData
	 * 			  the raw data read from the excel sheet to be extracted and filled
	 *            up the object before return the full object to the caller.
	 * @return Object which can be cast into a user defined type to get access
	 *         to its fields
	 */
	private Object prepareObject(Object userObj, Field[] fields,
			List<String> excelRowData) throws Exception, IllegalAccessException {
		// logger.entering(new Object[]{userObj, fields, excelRowData});
		String fieldType, data;
		Object obj = null, myObj = null;
		int i, j;
		String[] arrayData;
		Object[] arrayObj;

		try {
			// Create a new instance of the data so we can
			// store it here before return everything to the users.
			myObj = userObj.getClass().newInstance();
		} catch (InstantiationException e1) {
			// logger.log(Level.SEVERE, e1.getMessage(), e1);
		}

		// Assigning data to user defined object
		for (i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			fieldType = fields[i].getType().getName();
			data = excelRowData.get(i);

			if ((data == null) || (data == ""))
				continue;

			// ---===***===---
			// Array data
			// ---===***===---
			if (fieldType.contains("[")) {
				arrayData = data.split(",");

				for (j = 0; j < arrayData.length; j++)
					arrayData[j] = arrayData[j].trim();

				// ======================================================================================
				// ---===***===---
				// Array of primitive boolean.
				// ---===***===---
				if (fieldType.contains("[Z")) { // boolean[]
					boolean[] arrayBoolean = new boolean[arrayData.length];
					for (j = 0; j < arrayData.length; j++) {
						arrayBoolean[j] = false;
						if (arrayData[j] != null) {
							arrayData[j] = arrayData[j].toLowerCase();
							if (arrayData[j].contains("true"))
								arrayBoolean[j] = true;
						}
					}
					fields[i].set(myObj, arrayBoolean);
				}
				// ---===***===---
				// Array of primitive int.
				// ---===***===---
				else if (fieldType.contains("[I")) { // int[]
					int[] arrayInt = new int[arrayData.length];
					for (j = 0; j < arrayData.length; j++) {
						if (arrayData[j] != null) {
							arrayInt[j] = Integer.parseInt(arrayData[j]);
						}
					}
					fields[i].set(myObj, arrayInt);
				}
				// ---===***===---
				// Array of primitive float
				// ---===***===---
				else if (fieldType.contains("[F")) { // float[]
					float[] arrayFloat = new float[arrayData.length];
					for (j = 0; j < arrayData.length; j++) {
						arrayFloat[j] = Float.parseFloat(arrayData[j]);
					}
					fields[i].set(myObj, arrayFloat);
				}
				// ---===***===---
				// Arrray of primitive double
				// ---===***===---
				else if (fieldType.contains("[D")) { // double[]
					double[] arrayDouble = new double[arrayData.length];
					for (j = 0; j < arrayData.length; j++) {
						arrayDouble[j] = Double.parseDouble(arrayData[j]);
					}

					fields[i].set(myObj, arrayDouble);
				}
				// ---===***===---
				// Array of primitive long
				// ---===***===---
				else if (fieldType.contains("[J")) { // long[]
					long[] arrayLong = new long[arrayData.length];
					for (j = 0; j < arrayData.length; j++) {
						arrayLong[j] = Long.parseLong(arrayData[j]);
					}
					fields[i].set(myObj, arrayLong);
				}
				// ================================================================================================

				// ---===***===---
				// Array of object type
				// ---===***===---
				if (fieldType.contains("[L")) { // Array of user defined type
					arrayObj = (Object[]) Array.newInstance(Class
							.forName(fieldType.substring(2,
									fieldType.length() - 1)), arrayData.length);
					for (j = 0; j < arrayData.length; j++) {
						if (arrayData[j] != null) {
							if (fieldType.contains("Integer")) // Integer[]
								obj = new Integer(arrayData[j]);
							else if (fieldType.contains("String")) // String[]
								obj = new String(arrayData[j]);
							else if (fieldType.contains("Double")) // Double[]
								obj = new Double(arrayData[j]);
							else if (fieldType.contains("Long")) // Long[]
								obj = new Long(arrayData[j]);
							else if (fieldType.contains("Boolean")) // Boolean[]
								obj = new Boolean(arrayData[j]);
							else if (fieldType.contains("Float")) // Float[]
								obj = new Float(arrayData[j]);
							else if (fieldType.contains("Byte")) // Byte[]
							    obj = new Byte(arrayData[j]);
//							else if (fieldType.endsWith("ppo.Country;"))
//								obj = Country.getCountry(arrayData[j]);
//							else if (fieldType.endsWith("ppo.Currency;"))
//								obj = Currency.getCurrency(arrayData[j]);
//							else if (fieldType.endsWith("ppo.BankAccountType;"))
//								obj = BankAccountType.getBankAccountType(arrayData[j]);
//							else if (fieldType.endsWith("ppo.CreditCardType;"))
//								obj = CreditCardType.getCreditCardType(arrayData[j]);
//							else if (fieldType.endsWith("ppo.PPAccountType;"))
//								obj = PPAccountType.valueOf(arrayData[j]);
							else { // array of user defined
								try {
									obj = Class.forName(
											fieldType.substring(2, fieldType
													.length() - 1))
											.newInstance();
									obj = getSingleExcelRow(obj, arrayData[j]);

								} catch (InstantiationException e) {
									// logger.log(Level.SEVERE, e.getMessage(), e);
								}
							}
						}
						arrayObj[j] = obj;
					}
					// Write the whole array to the real user define data
					fields[i].set(myObj, arrayObj);
				}
			}

			// ---===***===---
			// primitive data type, not array
			// ---===***===---
			else {
				if (data != null) {
					if (fieldType.contains(".String"))
						fields[i].set(myObj, data);
					else if (fieldType.contains(".Integer"))
						fields[i].set(myObj, new Integer(data));
					else if (fieldType.contains("int")
							&& (fieldType.length() == 3))
						fields[i].set(myObj, Integer.parseInt(data));
					else if (fieldType.contains(".Boolean"))
						fields[i].set(myObj, new Boolean(data));
					else if (fieldType.contains("boolean")
							&& (fieldType.length() == 7))
						fields[i].set(myObj, Boolean.parseBoolean(data));
					else if (fieldType.contains(".Double"))
						fields[i].set(myObj, new Double(data));
					else if (fieldType.contains("double")
							&& (fieldType.length() == 6))
						fields[i].set(myObj, Double.parseDouble(data));
					else if (fieldType.contains(".Long"))
						fields[i].set(myObj, new Long(data));
					else if (fieldType.contains("long")
							&& (fieldType.length() == 4))
						fields[i].set(myObj, Long.parseLong(data));
					else if (fieldType.contains(".Float"))
						fields[i].set(myObj, new Float(data));
					else if (fieldType.contains("float")
							&& (fieldType.length() == 5))
						fields[i].set(myObj, Float.parseFloat(data));
					else if (fieldType.contains(".Byte"))
						fields[i].set(myObj, new Byte(data));
					else if (fieldType.contains("byte")
							&& (fieldType.length() == 4))
						fields[i].set(myObj, Byte.parseByte(data));
//					else if (fieldType.endsWith("ppo.Country"))
//						fields[i].set(myObj,Country.getCountry(data));
//					else if (fieldType.endsWith("ppo.Currency"))
//						fields[i].set(myObj, Currency.getCurrency(data));
//					else if (fieldType.endsWith("ppo.BankAccountType"))
//						fields[i].set(myObj, BankAccountType.getBankAccountType(data));
//					else if (fieldType.endsWith("ppo.CreditCardType"))
//						fields[i].set(myObj, CreditCardType.getCreditCardType(data));
//					else if (fieldType.endsWith("ppo.PPAccountType"))
//						fields[i].set(myObj, PPAccountType.valueOf(data));
					else {
						Class<?> classToLoad = Class.forName(fieldType);
						try {
							obj = classToLoad.newInstance();
							obj = getSingleExcelRow(obj, data);
							fields[i].set(myObj, obj);

						} catch (InstantiationException e) {
							// logger.log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
			}
		}
		// logger.exiting(myObj);
		return myObj;
	}

	/**
	 * Using the specified key to search for the date row from the specified
	 * Excel sheet, then return the row contents in a list of string format.
	 * @param sheetName - The name of the Excel sheet to read.
	 * @param key -  The specifies the row to read.
	 * @param colCount -  The number of columns to read, including empty and blank
	 *            column.
	 * @return List<String> String array contains the read data.
	 */
	private List<String> getRowContents(String sheetName, int rowIndex,
			int size) {
		// logger.entering(new Object[]{sheetName, rowIndex, size});
		Sheet sheet = fetchSheet(sheetName);

		int actualExcelRow = rowIndex - 1;
		List<String> rowData = new ArrayList<String>();
		Row row = sheet.getRow(actualExcelRow); 

		// bvu: Please do not change these 8 lines
		// They look strange but they are working.
		if (row != null) {
		    for (int i = 1; i <= size; i++) {
		        if (row.getCell(i) != null) 
		            rowData.add(row.getCell(i).toString());
		        else
		            rowData.add(null);
		    }
		}
		
		// logger.exiting(rowData);
		return rowData;
	}

	/**
	 * This method fetches a specific row from an excel sheet which can be identified using a key 
	 * and returns the data as an Object which can be cast back into the user's actual data type.
	 * @param userObj - An Object into which data is to be packed into
	 * @param key - A string that represents a key to search for in the excel sheet
	 * @return - An Object which can be cast into the user's actual data type.
	 * 
	 * @throws Exception
	 */
	public Object getSingleExcelRow(Object userObj, String key)
			throws Exception {
		return getSingleExcelRow(userObj, key, true);
	}
	
	/**
	 * This method fetches a specific row from an excel sheet which can be identified using a key 
	 * and returns the data as an Object which can be cast back into the user's actual data type.
	 * @param userObj - An Object into which data is to be packed into
	 * @param key - A string that represents a key to search for in the excel sheet
	 * @param isExternalCall - A boolean that helps distinguish internally if the call is being made internally or by the user.
	 * For external calls the index of the row would need to be bumped up,because the first row is to be ignored always.
	 * @return - An Object which can be cast into the user's actual data type.
	 * @throws Exception
	 */
	private Object getSingleExcelRow(Object userObj, String key, boolean isExternalCall) throws Exception{
		Class<?> cls = Class.forName(userObj.getClass().getName());
		int rowIndex = getRowIndex(cls.getSimpleName(), key);
		
		if(rowIndex == -1) {
			Exception e = new Exception("Row with key '" + key + "' is not found");
			// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		return getSingleExcelRow(userObj, rowIndex, isExternalCall);
		
	}

	/**
	 * This method can be used to fetch a particular row from an excel sheet.
	 * 
	 * @param userObj - The User defined object into which the data is to be packed into.
	 * @param index - The row number from the excel sheet that is to be read. For e.g., if you wanted to read
	 * the 2nd row (which is where your data exists) in your excel sheet, the value for index would be 1. <b>This method assumes that
	 * your excel sheet would have a header which it would EXCLUDE.</b> When specifying index value
	 * always remember to ignore the header, since this method will look for a particular row ignoring the header row.
	 * @return - An object that represents the data for a given row in the excel sheet.
	 * @throws Exception
	 */
	public Object getSingleExcelRow(Object userObj, int index)
			throws Exception {
		return getSingleExcelRow(userObj, index, true);
	}
	
	/**
	 * @param userObj - The User defined object into which the data is to be packed into.
	 * @param index - The row number from the excel sheet that is to be read. For e.g., if you wanted to read
	 * the 2nd row (which is where your data exists) in your excel sheet, the value for index would be 1. <b>This method assumes that
	 * your excel sheet would have a header which it would EXCLUDE.</b> When specifying index value
	 * always remember to ignore the header, since this method will look for a particular row ignoring the header row.
	 * @param isExternalCall - A boolean that helps distinguish internally if the call is being made internally or by the user.
	 * 
	 * @return - An object that represents the data for a given row in the excel sheet.
	 * 
	 * @throws Exception
	 */
	private Object getSingleExcelRow(Object userObj, int index, boolean isExternalCall) throws Exception{
		if (isExternalCall){
			index++;
			
		}
		// logger.entering(new Object[] {userObj, index});
		Object obj = null;

		Class<?> cls = Class.forName(userObj.getClass().getName());
		Field[] fields = cls.getDeclaredFields();

		List<String> excelRowData = getRowContents(cls.getSimpleName(), index, fields.length);
		if (excelRowData.size() != 0){
			obj = prepareObject(userObj, fields, excelRowData);
		}
		
		// logger.exiting(obj);

		return obj;

	}
	

	/**
	 * This function will use the input string representing the indexes to
	 * collect and return the correct excel sheet data rows as two dimensional
	 * object to be used as TestNG DataProvider.
	 * 
	 * @param myData
	 *            the user defined type object which provide details structure
	 *            to this function.
	 * @param indexes
	 *            the string represent the keys for the search and return the
	 *            wanted rows. It is in the format of:
	 *            <li> "1, 2, 3" for individual indexes. 
	 *            <li> "1-4, 6-8, 9-10" for ranges of indexes.
	 *            <li> "1, 3, 5-7, 10, 12-14" for mixing individual and range of
	 *            indexes.
	 * @return Object[][] Two dimensional object to be used with TestNG
	 *         DataProvider
	 * @throws Exception
	 */
	public Object[][] getExcelRows(Object myData, String indexes)
			throws Exception {
		// logger.entering(new Object[]{myData, indexes});
		List<Integer> arrayIndex = parseIndexString(indexes);

		Object[][] obj = new Object[arrayIndex.size()][1];
		for (int i = 0; i < arrayIndex.size(); i++) {
			int actualIndex = arrayIndex.get(i) + 1;
			obj[i][0] = getSingleExcelRow(myData, actualIndex, false);
			
		}
		// logger.exiting(obj);
		return obj;
	}

	/**
	 * This function will use the input string representing the keys to collect
	 * and return the correct excel sheet data rows as two dimensional object to
	 * be used as TestNG DataProvider.
	 * 
	 * @param myObj
	 *            the user defined type object which provides details structure
	 *            to this function.
	 * @param keys
	 *            the string represents the list of key for the search and
	 *            return the wanted row. It is in the format of {"row1", "row3",
	 *            "row5"}
	 * @return Object[][] two dimensional object to be used with TestNG
	 *         DataProvider
	 * @throws Exception
	 */
	public Object[][] getExcelRows(Object myObj, String[] keys)
			throws Exception {
		// logger.entering(new Object[]{myObj, keys});
		Object[][] obj = new Object[keys.length][1];

		for (int i = 0; i < keys.length; i++) {
			obj[i][0] = getSingleExcelRow(myObj, keys[i],true);
		}
		// logger.exiting(obj);
		return obj;
	}

	/**
	 * This function will read the whole excel sheet and map the data into
	 * two-dimensional array of object which is compatible with TestNG
	 * DataProvider to provide real test data driven development. This 
	 * function will ignore all rows in which keys are preceded by "#"
	 * as a comment character.
	 * 
	 * For the function to work, the sheet names have to be exactly named as the
	 * user defined data type. In the example below, there must be a sheet name
	 * "LOCAL_DATA" in the workbook.
	 * 
	 *<h3>Example how to use TestNG DataProvider:</h3>
	 * 
	 * <pre>
	 * '@DataProvider(name = "dataProvider1")'
	 * public Object[][] createData1() throws Exception {
	 * 
	 * 	// Declare your objects
	 * 	String pathName = "src/test/java/com/paypal/test/datareader";
	 * 	String fileName = "DataReader.xls";
	 * 	
	 * 	// Declare your data block
	 * 	LOCAL_DATA myData = new LOCAL_DATA();
	 * 	 
	 * 	// Pass your data block to "getAllExcelRows"
	 * 	Object[][] object = new ExcelDataProvider(pathName, fileName)
	 * 			.getAllExcelRows(myData);
	 * 
	 * 	// return the two-dimensional array object
	 * 	return object;
	 * }
	 * 
	 * // Specify our TestNG DataProvider	
	 * '@Test(dataProvider = "dataProvider1")'
	 * public void verifyLocalData1(LOCAL_DATA data) {
	 * 	
	 * 	// Your data will be distribute to your test case
	 * 	// one row per instance, and all can be run at the same time.
	 * 	System.out.println("Name: " + data.name);
	 * 	System.out.println("Password: " + data.password);
	 * 	System.out.println("the bank: " + data.bank.bankName);
	 * 
	 * 	System.out.println("Ph1: " + data.phone.areaCode);
	 * 	System.out.println("Ph2: " + data.cell.areaCode);
	 * 	System.out.println("Bank Address: " + data.bank.address.street);
	 * 	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	 * }
	 *</pre>
	 * 
	 * @param myObj
	 *            the user defined type object which provide details structure
	 *            to this function.
	 * @return Object[][] a two-dimensional object to be used with TestNG
	 *         DataProvider
	 * @throws Exception
	 */
	public Object[][] getAllExcelRows(Object myObj) throws Exception {
		// logger.entering(myObj);

		int i;

		Class<?> cls = Class.forName(myObj.getClass().getName());

		Sheet sheet = fetchSheet(cls.getSimpleName());

		int numRows = sheet.getPhysicalNumberOfRows();

		// Extracting number of rows of data to read
		// Notice that numRows is returning the actual
		// number of non-blank rows. Thus if there are
		// blank rows in the sheet then we will miss
		// some last rows of data.
		ArrayList<Integer> rowToBeRead = new ArrayList<Integer>();
		int currentRow = 1;
		int rowCount = 1;
		
        while (rowCount < numRows) {
            Row row = sheet.getRow(currentRow);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    if (!cell.toString().contains("#")) {
                        // Since first row is the header, we need to always skip
                        // it
                        // So we are bumping up the index by 1
                        rowToBeRead.add(currentRow + 1);
                    }
                    // Even cell contain "#" we need to  increase 
                    // rowCount, because this row is already counted
                    // in numRows since it is not blank.
                    rowCount = rowCount + 1;
                }
            }
            // We are using currentRow to indexing the rows
            // need to be read later, this will not index blank
            // row.
            currentRow = currentRow + 1;
        }
		
		i = 0;
		Object[][] obj = new Object[rowToBeRead.size()][1];
		for (int index : rowToBeRead) {
			obj[i++][0] = getSingleExcelRow(myObj, index,false);
		}
		// logger.exiting(obj);
		return obj;
	}
}
