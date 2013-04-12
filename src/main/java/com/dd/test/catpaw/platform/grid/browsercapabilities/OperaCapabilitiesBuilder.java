package com.dd.test.catpaw.platform.grid.browsercapabilities;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * This class represents the capabilities that are specific to Opera.
 *
 */
class OperaCapabilitiesBuilder extends DefaultCapabilitiesBuilder{

	@Override
	public DesiredCapabilities getCapabilities(DesiredCapabilities capabilities) {
		capabilities.setBrowserName(DesiredCapabilities.opera().getBrowserName());
		return capabilities;
	}
}

