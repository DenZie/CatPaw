package com.dd.test.catpaw.platform.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.LogFactory;
import org.testng.ISuite;
import org.testng.ITestContext;

import com.dd.test.catpaw.annotations.NeedsAttention;

/**
 * A configuration object that contains properties needed throughout CatPaw.
 * These options can be configured via the TestNG formated testng-suite.xml file
 * The testng-suite.xml file is used to override the default settings, so only
 * the data you want to modify should be specified in the configuration file.
 * Any data not found in the testng-suite.xml file will use the defaults. <br>
 * 
 * 
 * <pre>
 * &lt;!-- If parameter is empty string or omitted, please see defaults --&gt;                       
 * 
 * &lt;!-- SELENIUM CONFIGURATION --&gt;                                                             
 * 
 * &lt;!-- optional, defaults to clawslvs24.qa.paypal.com (grid) or localhost when usLocalRC is true  --&gt;                                 
 * &lt;parameter name="seleniumhost" value="" /&gt;                                                  
 * &lt;!-- optional, defaults to 4444  --&gt;                                                        
 * &lt;parameter name="seleniumport" value="" /&gt;                                                  
 * &lt;!-- optional, defaults to *firefox  --&gt;                                                    
 * &lt;parameter name="browser" value="*firefox" /&gt;	                                              
 * &lt;!-- optional, defaults to false  --&gt;                                                       
 * &lt;parameter name="useLocalRC" value="true" /&gt;                                                
 * &lt;!-- optional, turns automatic screen shots for click handlers on/off, defaults to true --&gt;                                                              
 * &lt;parameter name="autoScreenShot" value="true" /&gt;    
 * &lt;!-- optional, used when useLocalRC is true, defaults to 'default' --&gt;                                                              
 * &lt;parameter name="profileName" value="SeleniumProfile" /&gt;     *                                
 *                                                                                               
 * &lt;!-- STAGE & HOST CONFIGURATION --&gt;                                                         
 * 
 * &lt;!-- mandatory for  stage2 connectivity, no defaults --&gt;                                                             
 * &lt;parameter name="hostname" value="stage2xxxx.sc4.paypal.com" /&gt;                           
 * &lt;!-- optional, defaults to the current System username --&gt;                                  
 * &lt;parameter name="sshUser" value="" /&gt;                                                       
 * &lt;!-- optional, defaults to STAGEXXXX (based off hostname) --&gt;                               
 * &lt;parameter name="stageName" value="" /&gt;                                                     
 * &lt;!-- optional, defaults to URL is based off hostname --&gt;                                    
 * &lt;parameter name="paypalURL" value="" /&gt;                                                     
 * &lt;!-- optional, defaults to URL is based off hostname --&gt;                                    
 * &lt;parameter name="paypalAdminURL" value="" /&gt;                                                
 * &lt;!-- optional, defaults to https://api.stagexxx.paypal.com/nvp (based off hostname) --&gt;     
 * &lt;parameter name="nvpUrl"  value="" /&gt;                                                       
 * &lt;!-- optional, site locale of stage default is US --&gt;                                              
 * &lt;parameter name="siteLocale" value="" /&gt;
 * 
 * &lt; !-- eZTracker CONFIGRATION --&gt;
 *  
 * &lt;!-- optional,to update test results into eZTracker, defaults to false --&gt;                                                             
 * &lt;parameter name="updateEzTracker" value="true" /&gt;                                                         
 * &lt;!--mandatory if updateEzTracker is set to true, defaults to Push /&gt;
 * &lt;parameter name="pushName" value="" /&gt;
 * &lt;!--mandatory if updateEzTracker is set to true, defaults to Unknown /&gt;
 * &lt;parameter name="releaseCycle" value="" /&gt;
 * &lt;!--mandatory if updateEzTracker is set to true, defaults to Unknown /&gt;
 * &lt;!parameter name="groupName" value="" /&gt; 
 *                                                                                                 
 * &lt;!-- CLAWS AND JAWS FILES LOCATIONS --&gt;                                                   
 * 
 * &lt;!-- optional, default to clawsFiles --&gt;                                                  
 * &lt;parameter name="basedir" value=""  /&gt;                                                      
 * &lt;!-- optional, default to ${clawsFiles.basedir}/clawsLogs --&gt;                           
 * &lt;parameter name="logsDir" value=""  /&gt;                                                      
 * &lt;!-- optional, default to ${clawsFiles.basedir}/clawsWorkDir --&gt;                        
 * &lt;parameter name="workDir"  value="" /&gt;
 * </pre>
 * 
 * System properties and/or environment variables can also be used to configure
 * CatPaw. The values used should always start with "CLAWS_" and end with the
 * value you would like the set. The variable equals the
 * {@link CatPawConfigProperty} variable name, so for instance, to set the
 * hostname to "myHostname", the following system property or environment
 * variable should be set prior to initializing CatPaw:
 * 
 * <pre>
 * CLAWS_HOSTNAME = myHostname
 * </pre>
 * 
 * Any other system or environment variables can be set in a similar fashion.<br>
 * <br>
 * <h4>Order of Initialization</h4>
 * <ol>
 * <li>Environment variables</li>
 * <li>System properties
 * <li>From a testng-suite.xml file</li>
 * <li>CatPaw defaults</li>
 * </ol>
 */
public class CatPawConfig {
	private static final String CONFIG_LOCATION = "/oldclawsConfig.xml";
	private static XMLConfiguration config = null;
	private static String configLocatiton = CONFIG_LOCATION;

	/**
	 * Changes default location for configuration file and creates Config object
	 * from it
	 * 
	 * @param location
	 *            specify name of the new config file
	 */
	public static void setConfigLocation(String location) {
		configLocatiton = location;
		initConfig();
	}

	private static XMLConfiguration getConfig() {
		if (config != null) {
			return config;
		}
		initConfig();
		return config;
	}

	/**
	 * Parses suite parameters and generates CatPawConfig object
	 * 
	 * @param suite
	 *            list of parameters from configuration file within
	 *            &lt;suite&gt;&lt;/suite&gt; tag
	 */
	public synchronized static void initConfig(ISuite suite) {
		Map<CatPawConfigProperty, String> initialValues = new HashMap<CatPawConfigProperty, String>();
		for (CatPawConfigProperty prop : CatPawConfigProperty.values()) {
			// Check if parameter is here
			if (suite.getParameter(prop.getName()) != null && suite.getParameter(prop.getName()) != "") {
				initialValues.put(prop, suite.getParameter(prop.getName()));
			}
		}
		initConfig(initialValues);

	}

	/**
	 * Parses configuration file and extracts values for test environment
	 * 
	 * @param context
	 *            list of parameters includes values within
	 *            &lt;suite&gt;&lt;/suite&gt; and &lt;test&gt;&lt;/test&gt; tags
	 */
	public synchronized static void initConfig(ITestContext context) {
		Map<CatPawConfigProperty, String> initialValues = new HashMap<CatPawConfigProperty, String>();
		for (CatPawConfigProperty prop : CatPawConfigProperty.values()) {
			// Check if parameter is here
			String newValue = context.getCurrentXmlTest().getAllParameters().get(prop.getName());
			if (newValue != null && newValue != "") {
				initialValues.put(prop, newValue);
			}
		}
		initConfig(initialValues);


	}

	/**
	 * Reads and parses configuration file Initializes the configuration,
	 * reloading all data
	 */
	public synchronized static void initConfig() {
		Map<CatPawConfigProperty, String> initialValues = new HashMap<CatPawConfigProperty, String>();
		initConfig(initialValues);
	}

	/**
	 * Prints CatPawConfig Values
	 */
	public static void printCatPawConfigValues() {
		System.out.println("Print CatPaw configuration");
		System.out.println("seleniumPort = " + CatPawConfig.getIntConfigProperty(CatPawConfigProperty.SELENIUM_PORT));
		for (CatPawConfigProperty configProperty : CatPawConfigProperty.values()) {
			System.out.print(configProperty + "   ->   ");
			System.out.println(CatPawConfig.getConfigProperty(configProperty));
		}
	}

	/**
	 * Initializes the configuration, reloading all data while adding the
	 * supplied <code>initialValues</code> to the configuration.
	 * 
	 * @param initialValues
	 *            The initial set of values used to configure CatPaw
	 */
	@SuppressWarnings("unchecked")
	@NeedsAttention("CAL logging should not be tied to AUTO_SCREEN_SHOT and WebReporter.java")
	public synchronized static void initConfig(Map<CatPawConfigProperty, String> initialValues) {
		/*
		 * Internally, HtmlUnit uses Apache commons logging. Each class that
		 * uses logging in HtmlUnit creates a Logger by using the LogFactory,
		 * and the defaults it generates. So to modify the Logger that is
		 * created, we need to set this attribute
		 * "org.apache.commons.logging.Log" to the Logger we want it to use.
		 * 
		 * Note: this has to be the *first* thing done prior to any apache code
		 * getting a handle, so we're putting it in here because the next call
		 * is to XMLConfiguration (apache code).
		 */
		// NoOpLog essentially disables logging of HtmlUnit
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");

		config = new XMLConfiguration();

		// don't auto throw, let each config value decide
		config.setThrowExceptionOnMissing(false);
		// because we can config on the fly, don't auto-save
		config.setAutoSave(false);

		/*
		 * Set defaults
		 */
		CatPawConfigProperty[] configProps = CatPawConfigProperty.values();
		for (int i = 0; i < configProps.length; i++) {
			config.setProperty(configProps[i].getName(), configProps[i].getDefaultValue());
		}

		/*
		 * Load the user-defined config, (maybe) overriding defaults
		 */
		XMLConfiguration userConfig = null;
		try {
			userConfig = new XMLConfiguration(CatPawConfig.class.getResource(configLocatiton));
		} catch (ConfigurationException e) {
			// we can safely ignore this exception
		} finally {
			// only load in the user config if defined
			if (userConfig != null) {
				/*
				 * We need to treat this as a separate configuration, because if
				 * we load it into our config, it will append values rather than
				 * overwrite values... so lets do this manually
				 */
				Iterator<String> keys = userConfig.getKeys();
				while (keys.hasNext()) {
					String key = keys.next();
					// System.out.println("key--->"+key);
					config.setProperty(key, userConfig.getString(key));
				}
			}
		}

		/*
		 * Load in environment variables (if defined)
		 */
		CatPawConfigProperty[] clawsConfigProps = CatPawConfigProperty.values();
		for (int i = 0; i < clawsConfigProps.length; i++) {
			String value = System.getenv("CLAWS_" + clawsConfigProps[i].name());
			if ((value != null) && (!value.equals(""))) {
				config.setProperty(clawsConfigProps[i].getName(), value);
			}
		}

		/*
		 * Load in System properties variables (if defined)
		 */
		for (int i = 0; i < clawsConfigProps.length; i++) {
			String value = System.getProperty("CLAWS_" + clawsConfigProps[i].name());
			if ((value != null) && (!value.equals(""))) {
				config.setProperty(clawsConfigProps[i].getName(), value);
			}
		}

		/*
		 * Load in our supplied values (if defined)
		 */
		if (initialValues != null) {
			// System.out.println("initialValues contains " +
			// initialValues.size() + " key value pair.");
			for (CatPawConfigProperty configProperty : initialValues.keySet()) {
				// System.out.println("configProperty.getName(--->" +
				// configProperty.getName() + "---->>>>" +
				// initialValues.get(configProperty));
				config.setProperty(configProperty.getName(), initialValues.get(configProperty));
			}
		}

		/*
		 * Set dynamically configurable values which are safe to set at this
		 * point Safe means they; 1. Do not connect to a remote machine to
		 * determine value. 2. Do not have a requirement on another mandatory
		 * config variable. Otherwise set the value dynamically at first use, in
		 * loadValueAtFirstUsage(CatPawConfigProperty configProperty)
		 */

		// Find Selenium configuration
		String useLocalRC = config.getString(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC.getName());
		if (useLocalRC.equalsIgnoreCase("true")) {
			config.setProperty(CatPawConfigProperty.SELENIUM_HOST.getName(), "localhost");
		}

	}

	/*
	 * This method initializes specific values when they are first used
	 * effectively, lazy loading them
	 */
	private static void loadValueAtFirstUsage(String configProperty) {
		config = getConfig();

		/*
		 * Error Checking
		 */

		// HOSTNAME
		if (configProperty.equals(CatPawConfigProperty.HOSTNAME.getName())) {
			// At this point, we MUST have a hostname defined
			if (config.getProperty(CatPawConfigProperty.HOSTNAME.getName()).equals("")) {
				throw new RuntimeException("You must configure a " + CatPawConfigProperty.HOSTNAME.toString()
						+ " before interacting with a  environment."
						+ " Typically, this is done by specifying CLAWS_"
						+ CatPawConfigProperty.HOSTNAME.toString().toUpperCase() + "=<stagename> at runtime.");
			}
		}

		// STAGE_NAME
		if ((configProperty.equals(CatPawConfigProperty.STAGE_NAME.getName())) &&
			(config.getProperty(CatPawConfigProperty.STAGE_NAME.getName()) == null)) {
			if (config.getProperty(CatPawConfigProperty.HOSTNAME.getName()).equals("")) {
				return;
			}

		}

	}

	/**
	 * Returns a configuration property <b>String</b> value based off the
	 * {@link CatPawConfigProperty}
	 * 
	 * @param configProperty
	 *            The configuration property value to return
	 * @return The configuration property <b>String</b> value
	 */
	public static String getConfigProperty(CatPawConfigProperty configProperty) {
		loadValueAtFirstUsage(configProperty.getName());
		return CatPawConfig.getConfig().getString(configProperty.getName());
	}

	/**
	 * Returns a configuration property <b>String</b> value based off the
	 * {@link CatPawConfigProperty}
	 * 
	 * @param configProperty
	 *            String Property Name
	 * @return The configuration property <b>String</b> values
	 */
	public static String getConfigProperty(String configProperty) {
		loadValueAtFirstUsage(configProperty);
		return CatPawConfig.getConfig().getString(configProperty);
	}

	/**
	 * Returns a configuration property <b>int</b> value based off the
	 * {@link CatPawConfigProperty}
	 * 
	 * @param configProperty
	 *            The configuration property value to return
	 * @return The configuration property <b>int</b> value
	 */
	public static int getIntConfigProperty(CatPawConfigProperty configProperty) {
		loadValueAtFirstUsage(configProperty.getName());
		return CatPawConfig.getConfig().getInt(configProperty.getName());
	}

	/**
	 * Returns a configuration property <b>boolean</b> value based off the
	 * {@link CatPawConfigProperty}
	 * 
	 * @param configProperty
	 *            The configuration property value to return
	 * @return The configuration property <b>boolean</b> value
	 */
	public static boolean getBoolConfigProperty(CatPawConfigProperty configProperty) {
		loadValueAtFirstUsage(configProperty.getName());
		return CatPawConfig.getConfig().getBoolean(configProperty.getName());
	}

	/**
	 * Checks if property exists in the configuration
	 * 
	 * @param propertyName
	 *            String Property Name
	 * @return <b>true</b> or <b>false</b>
	 */
	public static boolean checkPropertyExists(String propertyName) {
		return CatPawConfig.getConfig().containsKey(propertyName);

	}

	/**
	 * Sets a CatPaw configuration values. This is useful when you want to
	 * override a setting but don't want to use the configuration file. <b>Note:
	 * This does not affect other configuration properties that may already be
	 * based off the original value of this configuration property</b>
	 * 
	 * @param configProperty
	 *            The configuration element to set
	 * @param configPropertyValue
	 *            The value of the configuration element
	 * @throws IllegalArgumentException
	 *             If problems occur during the set
	 */
	public static void setConfigProperty(CatPawConfigProperty configProperty, String configPropertyValue)
			throws IllegalArgumentException {
		if (configPropertyValue == null) {
			throw new IllegalArgumentException("Config property value must not be null");
		}
		if (configPropertyValue.equals("")) {
			throw new IllegalArgumentException("Config property value must not be empty");
		}
		CatPawConfig.getConfig().setProperty(configProperty.getName(), configPropertyValue);

		}

	/**
	 * Synchronize CatPaw with Jaws values
	 * 
	 * @param clawsConfigProperty
	 *            CatPawConfigProperty needs to be updated from Jaws
	 * @param jawsConfigValue
	 *            Value from Jaws for same name property
	 */
	public static void synchronizeWithJaws(CatPawConfigProperty clawsConfigProperty, String jawsConfigValue) {
		CatPawConfig.getConfig().setProperty(clawsConfigProperty.getName(), jawsConfigValue);
	}

	/**
	 * CatPaw config properties
	 */
	public static enum CatPawConfigProperty {
		// Settings specific to CatPaw
		/**
		 * Automatically take screen shots.<br>
		 */
		AUTO_SCREEN_SHOT("autoScreenShot", "true"),
		

		/**
		 * Selenium host might be localhost or other where server is running.<br>
		 * Default is set to <b>clawslvs24.qa.paypal.com</b>
		 */
		SELENIUM_HOST("seleniumhost", "seleniumhost.denzie.com"),

		/**
		 * Selenium port, any port where Selenium is running.<br>
		 * Default is set to <b>4444</b>
		 */
		SELENIUM_PORT("seleniumport", "4444"),

		/**
		 * Use or not localhost RC.<br>
		 * Default is set to <b>false</b>
		 */
		SELENIUM_USE_LOCAL_RC("useLocalRC", "false"),

		/**
		 * Name of Browser's profile used<br>
		 * Value used when SELENIUM_USE_LOCAL_RC is set to true<br>
		 * <b>No defaults</b>
		 */
		SELENIUM_PROFILE_NAME("profileName", "default"),
		
		/**
		 * The name of the JSON file that can be used, incase for a local run
		 * the end user wants to create their own customization for the local grid.
		 */
		SELENIUM_LOCAL_GRID_CONFIG_FILE("localGridConfigFile","localnode.json"),
		
		SELENIUM_LOCAL_GRID_IPHONE_CONFIG_FILE("localGridIPhoneConfigFile","iphonenode.json"),
		
		/**
		 * The path to the chromedriver executable on the local machine.
		 * This parameter is taken into consideration for local runs involving googlechrome
		 * browser alone. 
		 */
		SELENIUM_CHROMEDRIVER_PATH("chromeDriverPath",""),
		
		/**
		 * Use this parameter to set the user agent for firefox when working with Mobile version.
		 * This parameter should be set in conjunction with the parameter 
		 * {@link CatPawConfigProperty#BROWSER}
		 */
		SELENIUM_USERAGENT("userAgent", ""),

		/**
		 * Use this parameter to indicate if your remote runs are to be run against the sauce lab grid or against the QI
		 * owned grid/your own grid. This flag is required because when running against Sauce lab furnished grid,
		 * we are to ensure that fetching of WebDriver node IP and Port is to be disabled.
		 *
		 */
		SELENIUM_USE_SAUCELAB_GRID("useSauceLabGrid", "false"),

		/**
		 * Directory with Excel files to read info about GUI controls from.<br>
		 * Default is set to <b>GUIData</b> in resources
		 */
		GUI_DATA_DIR("GUIDataDir", "GUIData"),

		/**
		 * Site will show country used for tests.<br>
		 * Default is set to <b>US</b>
		 */
		SITE_LOCALE("siteLocale", "US"),

		/**
		 * Browser specified by user.<br>
		 * Default is set to <b>firefox</b>
		 */
		BROWSER("browser", "*firefox"),

		/**
		 * version specified by user when working with custom browser needs.<br>
		 */
		BROWSER_CAPABILITY_VERSION("version", ""),

		/**
		 * platform is specified by user.<br>
		 * Default is set to <b>XP</b> Supporting values are: ANDROID, ANY,
		 * LINUX, MAC, UNIX, VISTA, WINDOWS, XP.
		 */
		BROWSER_CAPABILITY_PLATFORM("platform", "ANY"),
		
		/**
		 * Should javascript capability be enabled on the browser for the AUT.
		 * By default javascript will be enabled on the client browser, but this flag can be used
		 * to toggle this setting.
		 */
		BROWSER_CAPABILITY_SUPPORT_JAVASCRIPT("enableJavaScript","true"),
		
		// stage validation settings
		/**
		 * buildProfiles is a list of keys pointing to the entries of
		 * services list in an excel data sheet. buildProfile only used 
		 * for stage validation test from BAT suite.
		 * When used, it should be one or more build profiles specified
		 * from at vertical at the deploying time. These parameters point
		 * to a list of services supposed to start after the build. This 
		 * list of buildProfiles is maintained by the ICE team.
		 */
		BUILD_PROFILES("buildProfiles", null),
		
		/**
		 * extraProfiles is a list of keys pointing to the entries of services
		 * list in an excel data sheet. extraProfiles only used for stage
		 * validation test from a BAT suite.
		 * extraProfiles are those extra services in addition to the buildProfiles
		 * to specify an addition set of services other than those in buildProfile.
		 * This list of extraProfiles is maintained by each vertical.
		 */
		EXTRA_SERVICES("extraServices", null),
		
		/**
		 * Timeout for an execution command, in milliseconds.<br>
		 * Used in Jaws by {@link com.paypal.test.jaws.execution.Execution}.<br>
		 * Also used in Bluefin to configure Selenium timeouts/<br>
		 * Default is set to <b>120000</b>
		 */
		EXECUTION_TIMEOUT("executionTimeout", "120000"),


		/**
		 * Turn this flag ON to see GUI actions such as loading a URL, click/setting text etc.,
		 * being logged into the test reports that get generated by CatPaw.
		 *
		 */
		ENABLE_GUI_LOGGING("enableGUILogging", "false"),

		/**
		 * Proxy Server settings incase required by the user. By default the
		 * proxy server host is configured to point to the AppScan Proxy Server
		 * for 
		 * 
		 */
		PROXY_HOST("ProxyHost", "sc4-scan01.corp.ebay.com"),

		/**
		 * Proxy Server settings incase required by the user. By default the
		 * proxy server port is configured to point to the AppScan Proxy Server
		 * port for 
		 */
		PROXY_PORT("ProxyPort", "9808"),

		// Settings shared with Jaws
		/**
		 * The fully qualified hostname of the stage you are testing against.<br>
		 * An example value would be <code>stage2sc4000.sc4.paypal.com</code><br>
		 * No defaults<br>
		 * <b>Mandatory</b> field for  stage2 connectivity
		 */
		HOSTNAME("hostname", ""),

		/**
		 * The stage name (in all CAPS as seen in /x/web/STAGEXX).<br>
		 * If not explicitly set at startup, this value is derived from the
		 * {@link CatPawConfigProperty#HOSTNAME} value. <br>
		 * <br>
		 * For example; <br>
		 * <br>
		 * If <code>HOSTNAME</code> is set to
		 * <code>stage2sc4000.sc4.paypal.com</code>, <br>
		 * <code>STAGE_NAME</code> will be set to
		 * <code><b>STAGE2SC4000</b></code>
		 */
		STAGE_NAME("stageName", null),

		/**
		 * The  URL <br>
		 * If not explicitly set at startup, this value is derived from the
		 * {@link CatPawConfigProperty#HOSTNAME} value. <br>
		 * <br>
		 * For example; <br>
		 * <br>
		 * If <code>HOSTNAME</code> is set to
		 * <code>stage2sc4000.sc4.paypal.com</code>, <br>
		 * <code>PAYPAL_URL</code> will be set to
		 * <code><b>https://www.stage2sc4000.qa.paypal.com</b></code>
		 */

		/**
		 * The base directory for CatPaw files<br>
		 * Default is set to <b>clawsFiles</b>
		 */
		BASE_DIR("baseDir", "clawsFiles"),

		/**
		 * The logs directory of CatPaw
		 */
		LOGS_DIR("logsDir", BASE_DIR.defaultValue + "/clawsLogs"),

		/**
		 * The work directory of CatPaw
		 */
		WORK_DIR("workDir", BASE_DIR.defaultValue + "/clawsWorkDir"),

		/**
		 * Should we log to the console (dev, user, off/no/false).<br>
		 * Used in Jaws by {@link com.dd.test.jaws.logging.JawsLogger}.<br>
		 * Default is set to <b>false</b>
		 */
		LOG_TO_CONSOLE("log.console", "false");


		private String name = null;
		private String defaultValue = null;
		private int intDefaultValue;

		private CatPawConfigProperty(String name, String defaultValue) {
			this.name = name;
			this.defaultValue = defaultValue;
		}

		private CatPawConfigProperty(String name, int intDefaultValue) {
			// System.out.println("in int" + name + "---" + intDefaultValue);
			this.name = name;
			this.intDefaultValue = intDefaultValue;
		}

		/**
		 * Returns the name of this configuration property
		 * 
		 * @return The name of this configuration property
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Returns the default value for this configuration property
		 * 
		 * @return The default <b>String</b> value for this configuration
		 *         property
		 */
		public String getDefaultValue() {
			return this.defaultValue;
		}

		/**
		 * Returns default <b>int</b> value for this configuration property
		 * 
		 * @return The default <b>int</b> value for this configuration property
		 */
		public int getIntDefaultValue() {
			return this.intDefaultValue;
		}
	}

}
