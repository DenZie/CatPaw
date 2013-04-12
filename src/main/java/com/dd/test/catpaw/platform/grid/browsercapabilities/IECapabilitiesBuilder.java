package com.dd.test.catpaw.platform.grid.browsercapabilities;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;

/**
 *
 * This class represents the capabilities that are specific to IE.
 */
class IECapabilitiesBuilder extends DefaultCapabilitiesBuilder {

	@Override
	public DesiredCapabilities getCapabilities(DesiredCapabilities capabilities) {
		capabilities.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC)
				&& CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.PERFORM_APPSCAN)) {
			capabilities.setVersion("APPSCAN");
		}
		return capabilities;
	}
}
