package com.dd.test.catpaw.pages;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.dd.test.catpaw.platform.html.WebPage;
import com.dd.test.catpaw.platform.asserts.CatPawAsserts;
import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.dataprovider.DataProvider;
import com.dd.test.catpaw.platform.dataprovider.DataProviderFactory;
import com.dd.test.catpaw.platform.grid.Grid;
import com.dd.test.catpaw.platform.grid.ScreenShotRemoteWebDriver;


/**
 * The Class BasePageImpl.
 */
public abstract class BasePageImpl implements WebPage {

	/** The UNKNOWN_PAGE_TITLE. */
	private static String UNKNOWN_PAGE_TITLE = "unknown-title";

	/** The CatPaw_GUI_COMPONENTS. */
	private static String CatPaw_GUI_COMPONENTS = "com.dd.test.catpaw.platform.html";

	/** The CatPaw_GUI_BASECLASS. */
	private static String CatPaw_GUI_BASECLASS = "com.dd.test.catpaw.testcomponents.BasePageImpl";

	// used to determine our locale (e.g. US, UK, DE, etc.)
	/** The site. */
	private String site;
	// Initialization state of WebPage
	/** The page initialized. */
	private boolean pageInitialized;
	// Object map queue for loading
	/** The map queue. */
	private Queue<String[]> mapQueue;

	/** HashMap to store our GUI object map content. */
	protected HashMap<String, String> objectMap;

	/** The page title. */
	protected String pageTitle;

	/**
	 * Instantiates a new base page impl.
	 */
	protected BasePageImpl() {
		pageTitle = UNKNOWN_PAGE_TITLE;
		mapQueue = new LinkedList<String[]>();
		site = CatPawConfig.getConfigProperty(CatPawConfigProperty.SITE_LOCALE);
		pageInitialized = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dd.test.catpaw.platform.html.WebPage#initPage(java.lang.String, java.lang.String)
	 */
	public void initPage(String pageDomain, String pageClassName) {
		// add the page domain and class name to the load queue
		mapQueue.add(new String[] { pageDomain, pageClassName });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dd.test.catpaw.platform.html.WebPage#initPage(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void initPage(String pageDomain, String pageClassName, String siteLocale) {
		site = siteLocale;
		initPage(pageDomain, pageClassName);
	}

	/**
	 * Load object map.
	 */
    protected void loadObjectMap() {
        while (mapQueue.size() > 0) {
            String[] map = mapQueue.poll();
            String pageDomain = map[0];
            String pageClassName = map[1];
            HashMap<String, String> currentObjectMap;
            try {

                DataProvider dataProvider = DataProviderFactory.getInstance(pageDomain, pageClassName);
                currentObjectMap = dataProvider.getDataProviderValues(site);

                pageTitle = currentObjectMap.get("pageTitle");

                if (objectMap != null) {
                    objectMap.putAll(currentObjectMap);
                } else {
                    objectMap = currentObjectMap;
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to initialize page data for " + pageDomain + "/" + pageClassName
                        + ". Root cause:" + e, e);
            }
        }
        pageInitialized = true;
    }

	/**
	 * Load object map. This method takes a HashMap<String, String> and uses it to populate the objectMap This is
	 * intended to allow for the use of programmatically generated locators in addition to the excel file format IDs and
	 * Locators
	 * 
	 * @param sourceMap
	 *            the source map
	 */
	protected void loadObjectMap(HashMap<String, String> sourceMap) {
		if (sourceMap.containsKey("pageTitle")) {
			pageTitle = sourceMap.get("pageTitle");
		}

		if (sourceMap != null) {
			if (objectMap == null) {
				objectMap = new HashMap<String, String>();
			}
			objectMap.putAll(sourceMap);
		}
		pageInitialized = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dd.test.catpaw.platform.html.WebPage#getExpectedPageTitle()
	 */
	public String getExpectedPageTitle() {
		return getPage().pageTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dd.test.catpaw.platform.html.WebPage#isInitialized()
	 */
	public boolean isInitialized() {
		return pageInitialized;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dd.test.catpaw.platform.html.WebPage#getSiteLocale()
	 */
	public String getSiteLocale() {
		return site;
	}

	/**
	 * This driver() method can be used by any PayPalPage example: paypalPage.driver()
	 * 
	 * @return the WebDriver
	 */
	public static ScreenShotRemoteWebDriver driver() {
		return Grid.driver();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dd.test.catpaw.platform.html.WebPage#waitForPage()
	 */
	public void waitForPage() {
		Grid.driver().waitUntilPageTitlePresent(getExpectedPageTitle());
		validatePage();
		String script = "return window.document.readyState";
		long totalWaitTime = 0;
		long defaultTimeOut = Long.parseLong(CatPawConfig.getConfigProperty(CatPawConfigProperty.EXECUTION_TIMEOUT));
		String returnVal = null;
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (((returnVal = (String) Grid.driver().executeScript(script, "")) != null)
				&& (returnVal.trim().equalsIgnoreCase("complete") != true)) {
			try {
				Thread.sleep(5000);
				totalWaitTime += 5000;
				if (totalWaitTime >= defaultTimeOut) {
					throw new RuntimeException("Timed-out waiting for the page to load");
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e.getLocalizedMessage());
			}
		}
		// Falling back to waiting for the page, incase the javascript returns a Null on the page
		if (returnVal == null) {
			Grid.driver().waitUntilPageTitlePresent(getExpectedPageTitle());
		}
	}

	/**
	 * Perform page validations against the object map.
	 */
	// TODO :: Needs further work.
	private void validatePage() {
		// for (String key : objectMap.keySet() ) {
		// //ignore blank data from object map
		// if ( (key.equalsIgnoreCase("")) ||
		// (objectMap.get(key).equalsIgnoreCase("")) ) {
		// continue;
		// }
		//
		// if (! ( Grid.selenium().isElementPresent(objectMap.get(key)) ||
		// Grid.selenium().isTextPresent(objectMap.get(key)) ) ) {
		// throw new RuntimeException("Landed on a page which did not pass load validation... " +
		// this.getClass().getSimpleName() + " missing element " + objectMap.get(key) );
		// }
		// }
	}

	/**
	 * Require extended class to provide this implementation.
	 * 
	 * @return the page
	 */
	public abstract BasePageImpl getPage();

	/**
	 * This method is responsible for automatically initializing the PayPal HTML Objects with their corresponding key
	 * values obtained from the hash map.
	 * 
	 * @param whichClass
	 *            - Indicate for what object you want the initialization to be done for e.g., the GUI Page class name
	 *            such as PayPalLoginPage, PayPalAddBankPage, etc
	 * @param objectMap
	 *            - Pass the {@link HashMap} that contains the key, value pairs read from the excel sheet
	 */

	public void initializeHtmlObjects(Object whichClass, HashMap<String, String> objectMap) {

		ArrayList<Field> fields = new ArrayList<Field>();
		Class<?> incomingClass = whichClass.getClass();

		while (!incomingClass.getName().equals(CatPaw_GUI_BASECLASS)) {
			fields.addAll(Arrays.asList(incomingClass.getDeclaredFields()));
			incomingClass = incomingClass.getSuperclass();
		}

		String errorDesc = " while initializaing HTML fields from the object map. Root cause:";
		try {
			for (Field field : fields) {
				// proceed further only if the data member and the key in the .xls file match with each other
				// below condition checks for this one to one mapping presence
				if (objectMap.containsKey(field.getName())) {
					field.setAccessible(true);

					String packageName = field.getType().getPackage().getName();

					// We need to perform initialization only for the paypal html objects and
					// We need to skip for any other objects such String, custom Classes etc.
					if (packageName.equals(CatPaw_GUI_COMPONENTS)) {

						Class<?> dataMemberClass = Class.forName(field.getType().getName());
						Class<?> parameterTypes[] = new Class[2];

						parameterTypes[0] = String.class;
						parameterTypes[1] = String.class;
						Constructor<?> constructor = dataMemberClass.getConstructor(parameterTypes);

						Object[] constructorArgList = new Object[2];
						String locatorValue = objectMap.get(field.getName());
						if (locatorValue == null){
						    continue;
						}
						constructorArgList[0] = new String(locatorValue);
						constructorArgList[1] = new String(field.getName());
						Object retobj = constructor.newInstance(constructorArgList);
						field.set(whichClass, retobj);
					}
				}
			}
		} catch (ClassNotFoundException exception) {
			throw new RuntimeException("Class not found" + errorDesc + exception, exception);
		} catch (IllegalArgumentException exception) {
			throw new RuntimeException("An illegal argument was encountered" + errorDesc + exception, exception);
		} catch (InstantiationException exception) {
			throw new RuntimeException("Could not instantantiate object" + errorDesc + exception, exception);
		} catch (IllegalAccessException exception) {
			throw new RuntimeException("Could not access data member" + errorDesc + exception, exception);
		} catch (InvocationTargetException exception) {
			throw new RuntimeException("Invocation error occured" + errorDesc + exception, exception);
		} catch (SecurityException exception) {
			throw new RuntimeException("Security error occured" + errorDesc + exception, exception);
		} catch (NoSuchMethodException exception) {
			throw new RuntimeException("Method specified not found" + errorDesc + exception, exception);
		}
	}

	/**
	 * This method takes a HashMap of expected content in which the keys are an identifier for the content (field name,
	 * id etc.) and the values are the expected strings. The Method iterates through the HashMap and verifies that the
	 * full page text contains each string. This method does not validate positioning on the page or association between
	 * elements. It only checks that the expected content strings appear somewhere on the page.
	 * 
	 * @param expectedStrings
	 *            the expected content strings
	 */
	public static void validateExpectedContentOnPage(HashMap<String, String> expectedStrings) {
		Set<String> keys = expectedStrings.keySet();

		String actualText = Grid.driver().findElementByTagName("body").getText();
		for (String key : keys) {
			String expectedText = expectedStrings.get(key);

			boolean textPresent = actualText.contains(expectedText);
			String message = expectedText + " ";
			if (textPresent) {

				message += "found ";
			} else {
				message += "not found ";
			}
			message += "on the page";
			CatPawAsserts.assertTrue(textPresent, message);
		}
	}
}
