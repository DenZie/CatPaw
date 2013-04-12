package com.dd.test.catpaw.platform.grid.browsercapabilities;

import java.util.logging.Level;

import org.openqa.selenium.remote.DesiredCapabilities;


import com.dd.test.catpaw.platform.config.ExtendedConfig;
import com.dd.test.catpaw.platform.grid.BrowserFlavors;
import com.dd.test.catpaw.platform.grid.Grid;


/**
 * This factory class is responsible for providing the framework with a {@link DesiredCapabilities} instance based on
 * the browser type.
 * 
 */
public class DesiredCapabilitiesFactory {

	// private static SimpleLogger logger = CatPawLogger.getLogger();

	/**
	 * @return - A {@link DesiredCapabilities} object that represents the capabilities the browser to be spawned must possess.
	 * 
	 */
	public static DesiredCapabilities getCapabilities() {
		String browser = Grid.getWebTestConfig().getBrowser();
		DefaultCapabilitiesBuilder capabilitiesBuilder = null;

		if (browser.equals(BrowserFlavors.CHROME.getBrowser())) {
			capabilitiesBuilder = new ChromeCapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.FIREFOX.getBrowser())) {
			capabilitiesBuilder = new FireFoxCapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.INTERNET_EXPLORER.getBrowser())) {
			capabilitiesBuilder = new IECapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.HTMLUNIT.getBrowser())) {
			capabilitiesBuilder = new HtmlUnitCapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.IPHONE.getBrowser())) {
			capabilitiesBuilder = new IPhoneCapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.ANDROID.getBrowser())) {
			capabilitiesBuilder = new AndroidCapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.IPAD.getBrowser())){
			capabilitiesBuilder = new IPadCapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.OPERA.getBrowser())){
			capabilitiesBuilder = new OperaCapabilitiesBuilder();
		} else if (browser.equals(BrowserFlavors.SAFARI.getBrowser())){
			capabilitiesBuilder = new SafariCapabilitiesBuilder();
		} else {

			// None of the supported browsers matched. This is an error
			// condition throw a RuntimeException and bail out, else WebDriver will raise
			// an exception.
			StringBuffer errorMsg = new StringBuffer();
			errorMsg.append("Browser name \'");
			errorMsg.append(browser + "\' did not match any browser flavors supported by the Grid.\n");
			errorMsg.append("Current Supported Browser flavors are : [" + BrowserFlavors.getSupportedBrowsersAsCSV()
					+ "].");

			RuntimeException e = new RuntimeException(errorMsg.toString());
			// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;

		}
		DesiredCapabilities capability = capabilitiesBuilder.createCapabilities();
		capability.setCapability(ExtendedConfig.TEST_NAME.getConfig(), Grid.getWebTestConfig().getTestName());
		//Lets check if the user provided more capabilities either via the @WebTest annotation
		//or via the attributes of ITestResult object for the current test.
		//This info will be available in the WebTestConfig object for the current test.
		capability.merge(Grid.getWebTestConfig().getAdditionalCapabilities());
		return capability;
	}

}
