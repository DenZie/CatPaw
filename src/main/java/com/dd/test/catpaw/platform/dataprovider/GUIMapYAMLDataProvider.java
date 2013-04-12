package com.dd.test.catpaw.platform.dataprovider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.yaml.snakeyaml.Yaml;


/**
 * Concrete DataProvider implementation for read data from the yaml files.
 */
public class GUIMapYAMLDataProvider implements DataProvider {

//

	private Yaml yamlFile;
	private Reader reader;
	List<Object> allObjects;

	/**
	 * This is a public constructor to create an input stream & Yaml instance
	 * for the input file.
	 * 
	 * @param fileName
	 *            the name of the YAML data file.
	 * @throws IOException
	 */
	public GUIMapYAMLDataProvider(String fileName) throws IOException {
//		// logger.entering(fileName);
		InputStream input = getInputFileStream(fileName);

		if (input == null) {
			FileNotFoundException e = new FileNotFoundException("Resource " + fileName + " not found");
	//		// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		yamlFile = new Yaml();

		allObjects = new ArrayList<Object>();
		Iterable<Object> it = yamlFile.loadAll(reader);
		for (Object temp : it) {
			allObjects.add(temp);
		}
		input.close();
//		// logger.exiting();
	}

	/**
	 * The user needs to provide the locale for which data needs to be read.
	 * After successfully reading the data from the input stream, it is placed
	 * in the hash map and returned to the users.
	 * 
	 * @param locale
	 *            Signifies the language or site language to read.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getDataProviderValues(String locale) {
//		// logger.entering(locale);

		HashMap<String, String> instanceMap = new HashMap<String, String>();

		for (Object temp : allObjects) {
			Map<String, String> map = (Map<String, String>) temp;
			try {
				instanceMap.put(map.get("Key"), map.get(locale));
			} catch (NullPointerException e) {
		//		// logger.log(Level.WARNING, "Kindly remove the Null document from the Yaml file. Ignoring the Null document.",e);
			}
		}
//		// logger.exiting(instanceMap);
		return instanceMap;
	}

	//TODO this method can be removed once the generic service caller logic has been implemented
	// within Jaws,because Jaws now would have a new method which would do this.
	private InputStream getInputFileStream(String fileName) {
//		// logger.entering(fileName);
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(fileName);
//		// logger.exiting(inputStream);
		return inputStream;
	}
}
