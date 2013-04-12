package com.dd.test.catpaw.platform.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.testng.Reporter;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.BrowserFlavors;
import com.dd.test.catpaw.platform.grid.Grid;
import com.dd.test.catpaw.platform.html.support.ByOrOperator;
import com.dd.test.catpaw.reports.runtime.WebReporter;


public abstract class AbstractElement {
    private String locator;
    private String controlName;
    private Map<String, String> propMap = new HashMap<String, String>();
    protected static final String LOG_DEMARKER  = "&#8594;";
    
//    // private static SimpleLogger logger = CatPawLogger.getLogger();

    public static RemoteWebElement locateElement(String locator) {
//    	// logger.entering(locator);
        RemoteWebElement element = null;
        By locatorBy = null;
        validateLocator(locator);
        locator=locator.trim();
        if(locator.indexOf("|")==-1){
        	locatorBy = getFindElementType(locator);
        }else{
        	String[] locators = locator.split("\\Q|\\E");
        	List<By> result = new ArrayList<By>();
        	for (String temp : locators){
        		result.add(getFindElementType(temp));
        	}
        	locatorBy = new ByOrOperator(result);
        }
        element = (RemoteWebElement) Grid.driver().findElement(locatorBy);
//        // logger.exiting(element);

        return element;
    }
    
    private static void validateLocator(String locator){
        if (locator == null) {
        	NoSuchElementException e = new NoSuchElementException("locator cannot be null");
    //    	// logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }else if(locator.trim().isEmpty()) {
        	NoSuchElementException e = new NoSuchElementException("locator cannot be empty");
    //    	// logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }

    public static List<WebElement> locateElements(String locator) {
//    	// logger.entering(locator);
		List<WebElement> webElementsFound = null;
		By locatorBy = null;
		validateLocator(locator);

		locator = locator.trim();

        if(locator.indexOf("|")==-1){
        	locatorBy = getFindElementType(locator);
        }else{
        	String[] locators = locator.split("\\Q|\\E");
        	List<By> result = new ArrayList<By>();
        	for (String temp : locators){
        		result.add(getFindElementType(temp));
        	}
        	locatorBy = new ByOrOperator(result);
        }

        webElementsFound = Grid.driver().findElements(locatorBy);
      //if element is empty list then throw exception since unlike findElement() findElements() always returns a list
        //irrespective of whether an element was found or not
        if (webElementsFound.isEmpty()) {
        	NoSuchElementException e = new NoSuchElementException("Could not use the locator " + locator
                    + ". Locator has to be either a name/id or xpath");
    //    	// logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
//        // logger.exiting(webElementsFound);
        return webElementsFound;
    }
    
	public static By getFindElementType(String locator){
		// logger.entering(locator);
		By valueToReturn = null;
    	locator=locator.trim();
		if(locator.startsWith("id=")){
			valueToReturn =  By.id(locator.substring("id=".length()));
    	}else if(locator.startsWith("name=")){
    		valueToReturn =  By.name(locator.substring("name=".length()));
    	}else if(locator.startsWith("link=")){
    		valueToReturn =  By.linkText(locator.substring("link=".length()));
    	}else if(locator.startsWith("xpath=")){
    		valueToReturn =  By.xpath(locator.substring("xpath=".length()));
    	}else if(locator.startsWith("/")){
    		valueToReturn =  By.xpath(locator);
    	}else if (locator.startsWith("css=")){
    		valueToReturn =  By.cssSelector(locator.substring("css=".length()));
    	}else{
    		valueToReturn =  new ByIdOrName(locator);
    	}
//		if (logger.isLoggable(Level.FINE)){
//			String msg = valueToReturn.getClass().getSimpleName() + " will be the location strategy that will be used for locating " + locator;  
//			// logger.log(Level.FINE, msg);
//		}
		// logger.exiting(valueToReturn);
		return valueToReturn;
    }

    public RemoteWebElement getElement() {
        return locateElement(getLocator());
    }

    public AbstractElement(String locator) {
        this.locator = locator;
    }
    public AbstractElement(String locator,String controlName) {
        this.locator = locator;
        this.controlName = controlName;
      }
    public String getLocator() {
        return locator;
    }
    public String getControlName() {
        return controlName;
        
    }
    public String getText() {
        return getElement().getText();
    }

    public boolean isElementPresent() {
//    	// logger.entering();
    	boolean returnValue = false;
    	try {
    		returnValue = getElement() != null;
    	}
    	catch(Exception e) {
    	}
//    	// logger.exiting(returnValue);
       return returnValue;
    }

    public boolean isVisible() {
        return ((RemoteWebElement) getElement()).isDisplayed();
    }
    
    public boolean isEnabled(){
    	return getElement().isEnabled();
    }

    public String getAttribute(String attributeName) {
        return getElement().getAttribute(attributeName);
    }

    /**
     * Gets the (whitespace-trimmed) value of an input field (or anything else
     * with a value parameter). For checkbox/radio elements, the value will be
     * "on" or "off" depending on whether the element is checked or not.
     * 
     * @return the element value, or "on/off" for checkbox/radio elements
     */
    public String getValue() {

    	return getAttribute("value");
    }

    public String getProperty(String key) {
        return propMap.get(key);
    }

    public void setProperty(String key, String value) {
        propMap.put(key, value);
    }

    protected String getWaitTime() {
        return CatPawConfig.getConfigProperty(CatPawConfigProperty.EXECUTION_TIMEOUT);
    }
    
    protected String fetchClassName(){
    	return Thread.currentThread().getStackTrace()[4].getClassName();
    }

	protected String resolvePageNameToUseForLogs(){
		// logger.entering();
		String pageName =fetchClassName();
		int firstChar = pageName.lastIndexOf ('.') + 1;
		pageName = pageName.substring ( firstChar );
		if (! pageName.toLowerCase().contains("page")){
				pageName="";
		}
		if (! pageName.isEmpty()){
			pageName = " in " + pageName;
		}
		// logger.exiting(pageName);
		return pageName;
	}

	protected String resolveControlNameToUseForLogs(){
		String resolvedName = getControlName();
		if (resolvedName == null){
			return getLocator();
		}
		return resolvedName;
	}

	protected void logUIAction(UIActions actionPerformed){
		logUIActions(actionPerformed, null);
	}

	protected void logUIActions(UIActions actionPerformed, String value){
		// logger.entering(new Object[] {actionPerformed, value});
		String valueToUse = (value == null) ? "" : value + " in ";
		Reporter.log( LOG_DEMARKER + actionPerformed.getAction() + valueToUse +  resolveControlNameToUseForLogs() + resolvePageNameToUseForLogs(), false);
		// logger.exiting();
	}
	
	protected void processScreenShot(){
		// logger.entering();
		processAlerts(Grid.getWebTestConfig().getBrowser());

		String title = "Default Title";
		try{
			title = Grid.driver().getTitle();
		}catch (Throwable thrown){
			//Do nothing with the exception
		}
		boolean logPages = CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.LOG_PAGES);
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.AUTO_SCREEN_SHOT)) {
			WebReporter.log(title, true, logPages);
		} else {
			WebReporter.log(title, false,logPages);
		}
		// logger.exiting();
	}
	//TODO: This method would need re-factoring in the future.
	//The moment alerts processing is supported in iPhone, we need to disable the edit
	//checks that are put in this method.
    private void processAlerts(String browser) {
//    	// logger.entering(browser);
        if (BrowserFlavors.IPHONE.getBrowser().equalsIgnoreCase(browser)
                || BrowserFlavors.IPAD.getBrowser().equalsIgnoreCase(browser)) {
    //    	// logger.exiting("Alerts are not supported in iPhone/iPad as of 2.25.0.");
            return;
        }
        try {
            Grid.driver().switchTo().alert();
//    //        logger.warning("Encountered an alert. Skipping processing of screenshots");
//    //        // logger.exiting();
            return;
        } catch (NoAlertPresentException exception) {
            // gobble the exception and do nothing with it. No alert was triggered.
            // so its safe to proceed with taking screenshots.
        }

    }
	
	protected void validatePresenceOfAlert(){
		try{
			Grid.driver().switchTo().alert();
			String errorMsg = "Encountered an alert. Cannot wait for an element when an operation triggers an alert.";
			throw new InvalidElementStateException(errorMsg);
		}catch (NoAlertPresentException exception){
			//gobble the exception and do nothing with it. No alert was triggered.
			//so its safe to proceed ahead.
		}
	}

}
