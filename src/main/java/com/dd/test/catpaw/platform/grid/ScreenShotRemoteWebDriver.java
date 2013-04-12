package com.dd.test.catpaw.platform.grid;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.SystemClock;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.browsercapabilities.DesiredCapabilitiesFactory;
import com.dd.test.catpaw.platform.html.AbstractElement;
import com.dd.test.catpaw.platform.html.TextField;

import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

/**
 * This class is an extension of {@link RemoteWebDriver} 
 * and provides the following additional capabilities as well:
 * 
 * waitUntilElementPresent(final String elementLocator);
 * waitUntilElementDisapear(final String elementLocator)
 * waitUntilTextPresent(final String searchString)
 * waitUntilTwoElementsPresent(final String elementLocator1, final String
 * elementLocator2) waitUntilElementVisible(final String elementLocator)
 * waitUntilPageTitlePresent(final String pageTitle) isElementPresent(String
 * locator)
 * 
 * 
 */
public class ScreenShotRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot {
	

	
	
    private ScreenShotRemoteWebDriver(URL url, DesiredCapabilities capabilities) {
        super(url, capabilities);
    }
    
    protected static String showCapabilities(DesiredCapabilities dc){
//    	// logger.entering(dc);
    	StringBuffer capabilitiesAsString = new StringBuffer();
    	Map<String, ?> capabilityMap = dc.asMap(); 
    	Iterator<String> keys = capabilityMap.keySet().iterator();
    	while (keys.hasNext()){
    		String key = keys.next();
    		capabilitiesAsString.append(key).append(":");
    		if (!key.toLowerCase().contains("profile")){
    			capabilitiesAsString.append(capabilityMap.get(key));
    		}else{
    			boolean isLocalRun = CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC);
    			if (isLocalRun){
    				capabilitiesAsString.append(CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_PROFILE_NAME));
    			}else{
    				capabilitiesAsString.append("<Not Applicable for remote runs>");
    			}
    		}
    		capabilitiesAsString.append(",");
    	}
    	//Not logging the return value, because this is the value that will be printed by createInstance
    	//If the return value also gets logged, then user will see this information twice in the log files.
//    	// logger.exiting();
    	return capabilitiesAsString.toString();
    }


    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
//    	// logger.entering(target);
    	X returnValue = null;
        if ((Boolean) getCapabilities().getCapability(CapabilityType.TAKES_SCREENSHOT)) {
        	 returnValue = target.convertFromBase64Png(execute(DriverCommand.SCREENSHOT).getValue().toString());
        }
        
        //Not logging the return value, because this  is clogging the detailed log files.
//        // logger.exiting();
        return returnValue;
    }
    /**
     * Apart from opening up the URL in a browser, this method optionally
     * takes care of logging in the "open" action on to the test report viz., the reports
     * generated via BFReporter.
     *
     */
    @Override
    public void get(String url) {
        super.get(url);
        if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING)){
        	Reporter.log("&#8594;Loaded URL: " + url,false);
        }
        printDebugInfoForUser();
     }

	private void printDebugInfoForUser() {
		Properties props = new Properties();
		 props.put("Selenium V", new org.openqa.selenium.internal.BuildInfo().getReleaseLabel());
		props.put("Client OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
		String userAgent = (String) this.executeScript("return navigator.userAgent", "");
		props.put("Browser", extractBrowserInfo(userAgent));
		// logger.log(Level.INFO, "Running on :" + props.toString());

	}
	
	private String extractBrowserInfo(String userAgent){
		Browser browser = Browser.parseUserAgentString(userAgent);
		OperatingSystem os = OperatingSystem.parseUserAgentString(userAgent);
		StringBuffer sb = new StringBuffer();
		sb.append(browser.getName());
		sb.append(" v:").append(browser.getVersion(userAgent));
		sb.append(" running on ").append(os.getName());
		return sb.toString();

	}
    /**
     * Waits until element with locator elementLocator appears at the browser
     * 
     * @param elementLocator
     *            identifier of element to be found
     */
    public void waitUntilElementPresent(final String elementLocator) {
//    	// logger.entering(elementLocator);
        WebDriverWait myWait = new WebDriverWait(Grid.driver(), Grid.getNewTimeOut()/1000);
        myWait.until(new WaitCondition(elementLocator));
//        // logger.exiting();

    }

    /**
     * Waits until element with locator elementLocator disappears at the browser
     * 
     * @param elementLocator
     *            identifier of element to be found
     */
    public void waitUntilElementDisapear(final String elementLocator) {
//    	// logger.entering(elementLocator);
        try {
            AbstractElement.locateElement(elementLocator);
        } catch (NoSuchElementException e) {
    //    	// logger.exiting();
            return;
        }
        Clock clock = new SystemClock();
        long end = clock.laterBy(Grid.getNewTimeOut());
        boolean waitPeriodOver = false;
        while (true) {
            waitPeriodOver = clock.isNowBefore(end);
            try {
                AbstractElement.locateElement(elementLocator);
            } catch (NoSuchElementException e) {
        //    	// logger.exiting();
                return;
            }
            if (waitPeriodOver == false) {
            	RuntimeException e = new RuntimeException(elementLocator + " didnt disappear within " + Grid.getNewTimeOut());
        //    	// logger.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }

    /**
     * Waits until text appears at the browser
     * 
     * @param searchString
     *            text will be waited for
     */
    public void waitUntilTextPresent(final String searchString) {
//    	// logger.entering(searchString);
        WebDriverWait myWait = new WebDriverWait(Grid.driver(), Grid.getNewTimeOut()/1000);
        ExpectedCondition<Boolean> conditionToCheck = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver input) {
                WebElement bodyTag = Grid.driver().findElement(By.tagName("body"));
                return bodyTag.getText().contains(searchString) == true;
            }
        };
        myWait.until(conditionToCheck);
//        // logger.exiting();
    }

    /**
     * Waits until both two elements appear at the page
     * 
     * @param elementLocator1
     *            identifier of first element expected to appear
     * @param elementLocator2
     *            identifier of second element expected to appear
     */
    public void waitUntilTwoElementsPresent(final String elementLocator1, final String elementLocator2) {
//    	// logger.entering(new Object[]{elementLocator1, elementLocator2});
        for (int second = 0;; second++) {
            if (second >= 60)
                throw new SeleniumException("timedout");
            try {
				if ((AbstractElement.locateElement(elementLocator1) != null)
						&& (AbstractElement.locateElement(elementLocator2) != null))
                    break;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e1) {
                }
            }
        }
//        // logger.exiting();
    }

    /**
     * Waits until element with locator elemLocator becomes visible
     * 
     * @param elementLocator
     *            identifier of element to be visible
     */
    public void waitUntilElementVisible(final String elementLocator) {
//    	// logger.entering(elementLocator);
        String message = "Wait for element to get visible " + elementLocator;
        new Wait() {
            @Override
            public boolean until() {
                WebElement element;
                try {
					element = AbstractElement.locateElement(elementLocator);
                    return ((RemoteWebElement) element).isDisplayed();
                    // return (element.getAttribute("hidden") == "false");
                } catch (Exception e) {
                    return false;
                }
            }
        }.wait(message, Grid.getNewTimeOut());
//        // logger.exiting();
    }

    /**
     * Waits until Page expected page title appears
     * 
     * @param pageTitle
     *            title of page expected to appear
     */
    public void waitUntilPageTitlePresent(final String pageTitle) {
//    	// logger.entering();
        String message = "Wait for title to appear " + pageTitle;
        new Wait() {
            @Override
            public boolean until() {
                return pageTitle.indexOf(Grid.driver().getTitle()) >= 0;
            }
        }.wait(message, Grid.getNewTimeOut());
//        // logger.exiting();

    }

    public boolean isElementPresent(String locator) {
//    	// logger.entering(locator);
    	boolean flag = false;
        try {
			flag =  AbstractElement.locateElement(locator) != null;
        } catch (Exception e) {
        }
//        // logger.exiting(flag);
        return flag;
    }
    
    public boolean isTextPresent (String text) {
//    	// logger.entering(text);
        WebElement bodyTag = Grid.driver().findElement(By.tagName("body"));
        boolean flag =  bodyTag.getText().contains(text) == true;
//        // logger.entering(flag);
        return flag;
    }

    public void waitForPageToLoad(String timeout) {
//    	// logger.entering(timeout);
        String message = "Waiting for page to load";
        new Wait() {
            @Override
            public boolean until() {
                Object returnValue = ((ScreenShotRemoteWebDriver) Grid.driver())
                        .executeScript("return document['readyState'] == 'complete'");
                return Boolean.parseBoolean(returnValue.toString());
            }
        }.wait(message, Long.parseLong(timeout));
//        // logger.exiting();
    }
    
    /**
     * @deprecated As of CatPaw 0.5.0 this method is deprecated.
     * Please use <br>
     * 
     * <b>{@link org.openqa.selenium.WebDriver.Window#maximize()}</b> instead
     * <br/>
     * <br/>
     * General usage for {@link org.openqa.selenium.WebDriver.Window#maximize()} from a test case is
     * <b>Grid.driver().manage().window().maximize()</b>
     */
    public void maximize(){
//    	// logger.entering();
        String script = "if (window.screen){window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};";
        ((JavascriptExecutor) this).executeScript(script);
//        // logger.exiting();
    }
    
    /**
     * locateElement will use the AbstractElement class to find an
     * element on a current web page without the users have to specify
     * the type of its locator. Inaddition to finElement this should
     * be handy.
     * 
     * @param locator
     * @return RemoteWebElement
     */
    public RemoteWebElement locateElement (String locator) {
    	return AbstractElement.locateElement(locator);
    }

	public static ScreenShotRemoteWebDriver createInstance() {
		// logger.entering();
        URL url = null;
        String hostToRun = CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_HOST);
        boolean isLocalRC = CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC);
        String port = CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_PORT);

        if (isLocalRC) {
            hostToRun = "localhost";
        }
        try {
            url = new URL("http://" + hostToRun + ":" + port + "/wd/hub");
        } catch (MalformedURLException e) {
    //    	// logger.log(Level.SEVERE, e.getMessage(), e);
        }

        DesiredCapabilities capability = DesiredCapabilitiesFactory.getCapabilities();
       
//		if (logger.isLoggable(Level.FINE)){
//			// logger.log(Level.FINE, "Spawning a browser with the following capabilities : " +  showCapabilities(capability));
//		}

        ScreenShotRemoteWebDriver driver  = new ScreenShotRemoteWebDriver(url, capability);
//        // logger.exiting(driver);
        
        return driver;		
	}
	
	/**
	 * A Utility method that helps with uploading a File to a Web Application.
	 * @param filePath - A String that represents the file name to be uploaded along with its path.
	 * @param filePathTextField - A {@link TextField} that represents the text box wherein the path is to be keyed in.
	 * 
	 * <p><b>Sample Usage: </b></p>
	 * <pre>
	 * TextField txtBox = new TextField("upfile");
	 * // Here the html snippet for the above text box would be 
	 * // {@literal <}input type=file name=upfile{@literal >}
	 * // Make sure you are providing the locator of the "file" type textbox
	 * // else uploadFile() method will fail. 
	 * Grid.driver().uploadFile("src/test/resources/upload.txt", txtBox);
	 * Button submitButton = new Button("//input[@value='Press']");
	 * submitButton.click();
	 */
	public void uploadFile(String filePath, com.dd.test.catpaw.platform.html.TextField filePathTextField){
		RemoteWebElement element = filePathTextField.getElement();
		String filePathToUse = new File(filePath).getAbsolutePath();
		LocalFileDetector detector = new LocalFileDetector(); 
		element.setFileDetector(detector);
		element.sendKeys(detector.getLocalFile(filePathToUse).getAbsolutePath());
	}

}

class WaitCondition implements ExpectedCondition<Boolean>{
    
    private String locator;
    
    public WaitCondition(String locator){
        this.locator = locator;
    }

    public Boolean apply(WebDriver driver) {
         return AbstractElement.locateElement(locator) != null;
    }
    
}