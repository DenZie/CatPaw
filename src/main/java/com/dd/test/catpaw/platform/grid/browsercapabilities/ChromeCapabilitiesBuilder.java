package com.dd.test.catpaw.platform.grid.browsercapabilities;

import java.util.ArrayList;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;

/**
 * This class represents the capabilities that are specific to Chrome browser.
 * 
 */
class ChromeCapabilitiesBuilder extends DefaultCapabilitiesBuilder {

	@Override
	public DesiredCapabilities getCapabilities(DesiredCapabilities capabilities) {

		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC)) {
			System.setProperty("webdriver.chrome.driver",
					CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_CHROMEDRIVER_PATH));
		}
		capabilities.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
		String userAgent = CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_USERAGENT);
		ArrayList<String> switches = new ArrayList<String>();
		switches.add("--ignore-certificate-errors");
		if ((userAgent != null) && (!userAgent.trim().isEmpty())) {
			switches.add("--user-agent=" + userAgent);
		}
		capabilities.setCapability("chrome.switches", switches);
		return capabilities;
	}
}
