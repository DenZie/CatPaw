package com.dd.test.catpaw.platform.grid.browsercapabilities;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;

/**
 * This class will create instance of {@link DesiredCapabilities} which is pre-filled with all the common properties
 * that are considered de-facto for all browsers.
 * 
 */
public abstract class DefaultCapabilitiesBuilder {
	
	public DesiredCapabilities createCapabilities(){
		return getCapabilities(getDefaultCapabilities());
	}
	
	public abstract DesiredCapabilities getCapabilities(DesiredCapabilities capabilities);

	public DesiredCapabilities getDefaultCapabilities() {

		DesiredCapabilities capability = new DesiredCapabilities();

		// Set the capability to capture screenshots based on the config property
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.AUTO_SCREEN_SHOT)) {
			capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		}

		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
		// if user has explicitly asked for javascript to be turned off, then switch it off
		if (!CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.BROWSER_CAPABILITY_SUPPORT_JAVASCRIPT)) {
			capability.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, false);
		}

		String browserVersion = CatPawConfig.getConfigProperty(CatPawConfigProperty.BROWSER_CAPABILITY_VERSION);

		if (CatPawConfig.getConfigProperty(CatPawConfigProperty.BROWSER_CAPABILITY_VERSION) != null) {
			capability.setVersion(browserVersion);
		}

		String platform = CatPawConfig.getConfigProperty(CatPawConfigProperty.BROWSER_CAPABILITY_PLATFORM);
		if (!platform.equalsIgnoreCase("ANY")) {
		    capability.setCapability(CapabilityType.PLATFORM, platform);
		}

		// If we are using sauce labs, set the selenium version to use, otherwise saucelabs's default version is about
		// 15 versions behind.
		boolean isSauceLabGrid = CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_SAUCELAB_GRID);
		if (isSauceLabGrid) {
			String seleniumVersion = new org.openqa.selenium.internal.BuildInfo().getReleaseLabel();
			capability.setCapability("selenium-version", seleniumVersion);

		}
		
		return capability;
	}
}
