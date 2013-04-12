package com.dd.test.catpaw.platform.config;

import java.io.IOException;
import java.util.Properties;

/**
 * A simple object which allows users to extract build time information related to CatPaw. This information is
 * auto-populated as part of a CatPaw build.
 * 
 */
public final class CatPawBuildInfo {

	private static final String BUILD_INFO_FILE = "/clawsbuildinfo.properties";
	private static CatPawBuildInfoProperties buildProperties = null;

	private CatPawBuildInfo() {

	}

	private static CatPawBuildInfoProperties getInfo() {
		if (buildProperties != null) {
			return buildProperties;
		}
		initInfo();
		return buildProperties;
	}

	private synchronized static void initInfo() {
		try {
			buildProperties = new CatPawBuildInfoProperties();
			buildProperties.load(CatPawBuildInfo.class.getResourceAsStream(BUILD_INFO_FILE));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load build time properties. Root cause: ", e);
		}
	}

	/**
	 * Returns values for build time info
	 * 
	 * @param property
	 *            - The {@link CatPawBuildProperty} of interest
	 * @return The build time value.</br></br> The fall back value which can be obtained via
	 *         {@link CatPawBuildProperty#getFallBackValue()} if the build time property is not defined.
	 */
	public static final String getBuildValue(CatPawBuildProperty property) {
		return getInfo().getProperty(property.getPropertyValue(), property.getFallBackValue());
	}

	private static class CatPawBuildInfoProperties extends Properties {
		private static final long serialVersionUID = -4808947170980686563L;

		public CatPawBuildInfoProperties() {
			super();
		}

		public String getProperty(String name, String fallBackValue) {
			String returnValue = super.getProperty(name, fallBackValue);
			if (returnValue.contains("${")) {
				return fallBackValue;
			}
			return returnValue;
		}
	}

	/**
	 * CatPaw build time properties
	 */
	public static enum CatPawBuildProperty {
		/**
		 * The version of CatPaw core
		 */
		CORE_VERSION("core.build.version"),
		/**
		 * The user name of the person that initiated the CatPaw core build
		 */
		CORE_USER_NAME("core.build.user.name");

		private CatPawBuildProperty(String value) {
			this.propertyValue = value;
			this.fallBackValue = "Undefined " + value;
		}

		private String propertyValue;
		private String fallBackValue = null;

		/**
		 * Returns the build property value
		 * 
		 * @return the property value
		 */
		public String getPropertyValue() {
			return this.propertyValue;
		}

		/**
		 * Returns the fall back value for this build property
		 * 
		 * @return The fall back value
		 */
		public String getFallBackValue() {
			return this.fallBackValue;
		}

		public String toString() {
			return this.propertyValue;
		}
	}
}
