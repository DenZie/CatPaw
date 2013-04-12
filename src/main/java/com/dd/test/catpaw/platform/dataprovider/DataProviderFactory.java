package com.dd.test.catpaw.platform.dataprovider;

import java.io.IOException;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;

/**
 * Data Provider Factory returns the Data Provider instance depending on the type of input data file (.xls,.xlsx or
 * .yaml).
 */
public class DataProviderFactory {
	

	private DataProviderFactory() {

	}

	/**
	 * Method to get the Data Provider instance depending on the input parameters.
	 * 
	 * @param pageDomain
	 *            domain folder under which the input data files are present.
	 * @param pageClassName
	 *            Page class name
	 * @return DataProvider instance
	 * @throws IOException 
	 */
	public static DataProvider getInstance(String pageDomain, String pageClassName) throws IOException {
//		// logger.entering(new Object[]{pageDomain, pageClassName});
		DataProvider dataProvider = null;

		String guiDataDir = CatPawConfig.getConfigProperty(CatPawConfigProperty.GUI_DATA_DIR);
		String rawDataFile = guiDataDir + "/" + pageDomain + "/" + pageClassName;
		String yamlFile = rawDataFile + ".yaml";
		String xlsFile = rawDataFile + ".xls";
		String xlsxFile = rawDataFile + ".xlsx";

		if (getFilePath(yamlFile) != null) {
			dataProvider = new GUIMapYAMLDataProvider(yamlFile);
		} else if (getFilePath(xlsFile) != null) {
			dataProvider = new GUIMapExcelDataProvider(xlsFile);
		} else if (getFilePath(xlsxFile) != null) {
			dataProvider = new GUIMapExcelDataProvider(xlsxFile);
		} else {
			IllegalArgumentException e =  new IllegalArgumentException("Data File does not exist for "+rawDataFile+". Supported file extensions: yaml, xls & xlsx.");
	//		// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
//		// logger.exiting(dataProvider);
		return dataProvider;
	}

	/**
	 * Method to get the complete file path.
	 * 
	 * @param file
	 * @return String file path
	 */
	private static String getFilePath(String file) {
//		// logger.entering(file);
		String filePath = null;

		try {
			filePath =  DataProviderFactory.class.getClassLoader().getResource(file).getPath();
		} catch (NullPointerException e) {
			//gobble exception and do nothing
		}
//		// logger.exiting(filePath);
		return filePath;

	}

}
