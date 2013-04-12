package com.dd.test.catpaw.platform.config;

/**
 * Commonly referred claws configurations. 
 *
 */
public enum ExtendedConfig {
	/**
	 * This can be set from within the configuration method of a given test-script.
	 * Here's an example of how to use this.
	 * <pre>
	 * {@literal @}BeforeMethod
	 * public void setup(ITestResult result, Method method){
	 * DesiredCapabilities dc = new DesiredCapabilities();
	 * //customize the capabilities
	 * result.setAttribute(ExtendedConfig.CAPABILITIES, dc);	 
	 * </pre>
	 */
	CAPABILITIES("capabilities"),
	TEST_NAME("name");
	private String configName;
	private ExtendedConfig(String configName){
		this.configName = configName;
	}
	
	public String getConfig(){
		return this.configName;
	}
}
