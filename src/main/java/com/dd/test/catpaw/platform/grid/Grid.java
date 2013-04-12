package com.dd.test.catpaw.platform.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITestResult;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;


/**
 * Utility class making it easy to write tests based on Selenium driver in a
 * multi-thread context.
 * 
 * 
 * in parallel to those from WebSelenium since we have to support both Selenium
 * and WebDriver in this release.
 * 
 * In the next release we will deprecate Selenium, CatPawSelenium,
 * WebDriverBackedSelenium and only support WebDriver part of the Selenium-2
 * 
 */
public class Grid {


	private static ThreadLocal<CatPawSelenium> threadLocalSelenium = new ThreadLocal<CatPawSelenium>();
	private static ThreadLocal<ScreenShotRemoteWebDriver> threadLocalWebDriver = new ThreadLocal<ScreenShotRemoteWebDriver>();
	private static ThreadLocal<WebTestConfig> threadWebTestConfig = new ThreadLocal<WebTestConfig>();

	private final static String TIME_OUT = CatPawConfig
			.getConfigProperty(CatPawConfigProperty.EXECUTION_TIMEOUT);

	private static boolean isLocalRC = CatPawConfig
			.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC);
	
	static {
		Logger.getLogger("").setLevel(Level.OFF);
	}

	/**
	 * Starts a web session based on the browser flavor.
	 * The browser flavor and also the test name (which becomes relevant for Sauce Labs runs) are now
	 * passed as attributes to the {@link ITestResult} object.
	 * 
	 * @param webTestConfig - A {@link WebTestConfig} object that represents the Web Configurations of a 
	 * <code>{@literal @}WebTest</code> annotated method.
	 * 
	 * @return a {@link CatPawWebSession} that represents the created session
	 */
	public static CatPawWebSession startSession(WebTestConfig webTestConfig){
		//First save the current incoming webTest configuration, because this is what is going to be 
		//queried by everyone else.
		threadWebTestConfig.set(webTestConfig);
		String browser = webTestConfig.getBrowser();
		// logger.entering(browser);
		ScreenShotRemoteWebDriver driver = ScreenShotRemoteWebDriver.createInstance();
		threadLocalWebDriver.set(driver);
		//WebDriverBackedSelenium is not compatible with iPhone capabilities
		//It should not be instantiated when the user requests for an iPhone capability
		if (browser.endsWith(BrowserFlavors.IPHONE.getBrowser())){
			threadLocalSelenium.set(null);
		}else{
			threadLocalSelenium.set(new CatPawSelenium(driver, "_blank"));
			selenium().setTimeout(TIME_OUT);
		}
		
		boolean isLocalRC = CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC);
		boolean isSauceLabGrid = CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_SAUCELAB_GRID);

		boolean logIPandPort = ((isLocalRC == false) && (isSauceLabGrid == false));
		if (logIPandPort) {
			String hostName = CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_HOST);
			int port = CatPawConfig.getIntConfigProperty(CatPawConfigProperty.SELENIUM_PORT);
			String[] hostAndPort = getHostNameAndPort(hostName, port, driver.getSessionId());
			// logger.info("Connected to Remote Node " + hostAndPort[0] + " on port " + hostAndPort[1]);
		}
		CatPawWebSession session = new CatPawWebSession(driver(), selenium());
		// logger.exiting(session);
		return session;
	}

	/**
	 * Switches to a different web driver session
	 * 
	 * @param session
	 *            the {@link CatPawWebSession} sessions to switch to
	 * @return a {@link CatPawWebSession} that represents the previous session
	 * @deprecated - This method is deprecated as of 0.6.0. Use {@link Grid#switchSession(CatPawWebSession, WebTestConfig)}
	 */
	public static synchronized CatPawWebSession switchSession(
			CatPawWebSession session) {
	    return switchSession(session, new WebTestConfig());
	}
	
	/**
	 * Switches to a different web driver session.
	 * @param session  the {@link CatPawWebSession} sessions to switch to
	 * @param config the {@link WebTestConfig} to save.
	 * @return a {@link CatPawWebSession} that represents the previous session
	 */
	public static synchronized CatPawWebSession switchSession(CatPawWebSession session, WebTestConfig config){
		// logger.entering(session);
	    ScreenShotRemoteWebDriver activeWebDriver = driver();
	    CatPawSelenium activeSelenium = selenium();
	    threadLocalWebDriver.set(session.getWebDriver());
	    threadLocalSelenium.set(session.getSelenium());
	    threadWebTestConfig.set(config);
	    CatPawWebSession sessionToReturn =  new CatPawWebSession(activeWebDriver, activeSelenium);
	    // logger.exiting(sessionToReturn);
	    return sessionToReturn;
	    
	}

	public static long getNewTimeOut() {
		// logger.entering();
		String stringTimeOut = CatPawConfig
				.getConfigProperty(CatPawConfigProperty.EXECUTION_TIMEOUT);
		long returnValue = Long.parseLong(stringTimeOut.trim());
		// logger.exiting(returnValue);
		return returnValue;
	}

	/**
	 * selenium()
	 * 
	 * @return session example: Grid.selenium().stop();
	 * @deprecated As of CatPaw 0.1.0 this method is deprecated. Please use
	 *             {@link Grid#driver()} instead
	 */
	public static CatPawSelenium selenium() {
		return threadLocalSelenium.get();
	}

	public static ScreenShotRemoteWebDriver driver() {
		return threadLocalWebDriver.get();
	}
	
	/**
	 * @return - A {@link WebTestConfig} object that represents the 
	 * Web configurations for the currently running <code>{@literal @}WebTest</code> annotated method.
	 */
	public static WebTestConfig getWebTestConfig(){
		return threadWebTestConfig.get();
	}

	/**
	 * The existing session is closed when this method is invoked. This method
	 * takes care of closing both the selenium session as well as the driver
	 * sessions as well
	 */
	public static synchronized void closeSession() {
		// logger.entering();
		if (selenium() != null) {
			CatPawSelenium selenium = selenium();
			threadLocalSelenium.set(null);
			selenium.stop();
		}
		if (driver() != null) {
			ScreenShotRemoteWebDriver driver = driver();
			threadLocalWebDriver.set(null);
			driver.quit();
		}
		// logger.exiting();
	}

	/**
	 * Closes the session specified.
	 * 
	 * @param session
	 *            the {@link CatPawWebSession} to close
	 */
	public static synchronized void closeSession(CatPawWebSession session) {
		// logger.entering(session);
		CatPawWebSession previouslyActiveSession = switchSession(session,Grid.getWebTestConfig());
		closeSession();
		if (!session.equals(previouslyActiveSession)) {
			switchSession(previouslyActiveSession, Grid.getWebTestConfig());
		}
		// logger.exiting();
	}

	/**
	 * runOnLocalRC()
	 * 
	 * @return localRC, Seleniuum run local if localRC=true, Selenium will run
	 *         remote if localRC=false example: Grid.runOnLocalRC()
	 */
	public static boolean getRunOnLocalRC() {
		return isLocalRC;
	}

	/**
	 * Helper method that can be used to load a URL in a browser.
	 * @param url - The url of the web application that needs to be opened.
	 */
	public static void open(String url) {
		Grid.driver().get(url);
	}

	/**
	 * Converts a http response into a JSON Object
	 * 
	 * @param resp
	 *            - a http response obtained from executing a rqst against a
	 *            host
	 * @return - An object of type {@link JSONObject}
	 * @throws IOException
	 * @throws JSONException
	 */
	private static JSONObject extractObject(HttpResponse resp)
			throws IOException, JSONException {
		// logger.entering(resp);
		BufferedReader rd = new BufferedReader(new InputStreamReader(resp
				.getEntity().getContent()));
		StringBuffer s = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			s.append(line);
		}
		rd.close();
		JSONObject objToReturn = new JSONObject(s.toString());
		// logger.exiting(objToReturn);
		return objToReturn;
	}

	/**
	 * For a given Session ID against a host on a particular port, this method
	 * returns the remote webdriver node and the port to which the execution was
	 * redirected to by the hub.
	 * 
	 * @param hostName
	 *            - The name of the hub machine
	 * @param port
	 *            - The port on which the hub machine is listening to
	 * @param session
	 *            - An object of type {@link SessionId} which represents the
	 *            current session for a user.
	 * @return - An array of string wherein the first element represents the
	 *         remote node's name and the second element represents its port.
	 */
	private static String[] getHostNameAndPort(String hostName, int port,
			SessionId session) {
		// logger.entering(new Object[]{hostName, port, session});
		String[] hostAndPort = new String[2];
		String errorMsg = "Failed to acquire remote webdriver node and port info. Root cause: ";

		try {
			HttpHost host = new HttpHost(hostName, port);
			DefaultHttpClient client = new DefaultHttpClient();
			URL sessionURL = new URL("http://" + hostName + ":" + port
					+ "/grid/api/testsession?session=" + session);
			BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest(
					"POST", sessionURL.toExternalForm());
			HttpResponse response = client.execute(host, r);
			JSONObject object = extractObject(response);
			URL myURL = new URL(object.getString("proxyId"));
			if ((myURL.getHost() != null) && (myURL.getPort() != -1)) {
				hostAndPort[0] = myURL.getHost();
				hostAndPort[1] = Integer.toString(myURL.getPort());
			}
		} catch (Exception e) {
			// logger.log(Level.SEVERE, errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		}
		// logger.exiting(hostAndPort);
		return hostAndPort;
	}
}
