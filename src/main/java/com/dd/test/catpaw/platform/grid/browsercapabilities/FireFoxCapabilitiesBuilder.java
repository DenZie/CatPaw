package com.dd.test.catpaw.platform.grid.browsercapabilities;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;

/**
 * This class represents the capabilities that are specific to firefox.
 * 
 */
class FireFoxCapabilitiesBuilder extends DefaultCapabilitiesBuilder {

	@Override
	public DesiredCapabilities getCapabilities(DesiredCapabilities capabilities) {
		capabilities.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
		capabilities.setCapability(FirefoxDriver.PROFILE, prepareFireFoxProfile());
		return capabilities;
	}

	private FirefoxProfile prepareFireFoxProfile() {
		FirefoxProfile profile = null;
		//TODO : Point to ponder. 
		//Currently CatPaw supports working with firefox profiles only for local runs.
		//This is a restriction that CatPaw is imposing on its users even though Grid allows this.
		//Do we enable this for remote runs also ? The cons of this the possibility of JVM crashes
		//due to large FirefoxProfile (size on file system)
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC)) {
			String firefoxProfileName = CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_PROFILE_NAME);
			profile = new ProfilesIni().getProfile(firefoxProfileName);
		}
		if (profile == null) {
			profile = new FirefoxProfile();
		}

		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.PERFORM_APPSCAN)) {
			Proxy proxy = new Proxy();
			String proxyHost = CatPawConfig.getConfigProperty(CatPawConfigProperty.PROXY_HOST) + ":"
					+ CatPawConfig.getConfigProperty(CatPawConfigProperty.PROXY_PORT);
			proxy.setHttpProxy(proxyHost);
			proxy.setFtpProxy(proxyHost);
			proxy.setSslProxy(proxyHost);
			profile.setProxyPreferences(proxy);
		}
		String userAgent = CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_USERAGENT);
		if ((userAgent != null) && (!userAgent.trim().isEmpty())) {
			profile.setPreference("general.useragent.override", userAgent);
		}
		return profile;
	}
}
