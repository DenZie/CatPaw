package com.dd.test.catpaw.platform.grid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.grid.common.JSONConfigurationUtils;
import org.openqa.grid.common.exception.GridConfigurationException;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;


/**
 * This class contains methods to start and shutdown local grid.
 */
public class LocalGridManager {

	private static boolean wasLocalHubStarted = false;
	private static boolean wasLocalNodeStarted = false;
	private static boolean wasLocalNodeRegistered = false;
	private static boolean wasIPhoneNodeRegistered = false;
	private static Hub localHub;

	private static boolean isLocalRC = CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.SELENIUM_USE_LOCAL_RC);

	private static SeleniumServer node;

	/**
	 * This method is responsible for spawning a local hub for supporting local executions
	 */
	public static synchronized void spawnLocalHub(String browser) {
		boolean attachiPhoneNode = false;
		if ((browser != null ) && !(browser.isEmpty()) ){
			attachiPhoneNode = browser.endsWith(BrowserFlavors.IPHONE.getBrowser());
		}
		boolean environmentStatus = wasLocalHubStarted && wasLocalNodeStarted && wasLocalNodeRegistered;
		if (attachiPhoneNode) {
			environmentStatus = environmentStatus && wasIPhoneNodeRegistered;
		}
		if ((!isLocalRC) || (environmentStatus)) {
			return;
		}

		int port = 0;
		JSONObject request = null;
		JSONObject registrationRequest = null;
		String localJSONFileName = CatPawConfig
				.getConfigProperty(CatPawConfigProperty.SELENIUM_LOCAL_GRID_CONFIG_FILE);
		try {
			request = JSONConfigurationUtils.loadJSON(localJSONFileName);
			JSONObject jsonConfig = request.getJSONObject("configuration");
			port = jsonConfig.getInt("port");
		} catch (JSONException e1) {
			String errorMsg = "An error occured while working with the JSON file : " + localJSONFileName
					+ ". Root cause: ";
			throw new GridConfigurationException(errorMsg, e1);
		}
		if (attachiPhoneNode) {
			localJSONFileName = CatPawConfig
					.getConfigProperty(CatPawConfigProperty.SELENIUM_LOCAL_GRID_IPHONE_CONFIG_FILE);
			registrationRequest = JSONConfigurationUtils.loadJSON(localJSONFileName);
		}

		GridHubConfiguration config = new GridHubConfiguration();
		config.loadDefault();
		config.setHost("localhost");
		config.setPort(CatPawConfig.getIntConfigProperty(CatPawConfigProperty.SELENIUM_PORT));
		// the below logic will make sure that hub or node or link node actions
		// will not be done several times when executing multiple testcase
		// locally.
		try {
			if (!wasLocalHubStarted) {
				localHub.start();
				wasLocalHubStarted = true;
			}

			if (!wasLocalNodeStarted) {
				RemoteControlConfiguration c = new RemoteControlConfiguration();
				c.setPort(port);
				node = new SeleniumServer(c);
				node.boot();
				wasLocalNodeStarted = true;
			}
			URL registration = new URL(localHub.getUrl().toString() + "/grid/register");
			if (!wasLocalNodeRegistered) {
				registerNodeToHub(registration, request.toString());
				wasLocalNodeRegistered = true;
			}
			if ((attachiPhoneNode) && (!wasIPhoneNodeRegistered)) {
				registerNodeToHub(registration, registrationRequest.toString());
				wasIPhoneNodeRegistered = true;
			}
		} catch (Exception e) {
			// logger.log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * This method helps shut down the already spawned hub for local runs
	 */
	protected final static synchronized void shutDownHub() {
		if (!isLocalRC) {
			return;
		}
		if (node != null) {
			node.stop();
		}
		if (localHub != null) {
			try {
				localHub.stop();
			} catch (Exception e) {
//				String errorMsg = "An error occured while attempting to shut down the local Hub. Root cause: ";
				// logger.log(Level.SEVERE, errorMsg, e);
			}
		}
		// logger.exiting();

	}

	/**
	 * This method helps with creating a node and associating it with the already spawned Hub instance
	 * 
	 * @param registrationURL
	 *            - The registration URL of the hub
	 * @param json
	 *            - A string that represents the capabilities and configurations in the JSON text file
	 */
	private static void registerNodeToHub(URL registrationURL, String json) {
		// logger.entering(new Object[]{registrationURL,json});
		BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST",
				registrationURL.toExternalForm());
		String errorMsg = "Error sending the node registration request. ";

		try {
			r.setEntity(new StringEntity(json));
		} catch (UnsupportedEncodingException e) {
			// logger.log(Level.SEVERE, errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpHost host = new HttpHost(registrationURL.getHost(), registrationURL.getPort());

		HttpResponse response = null;
		try {
			response = client.execute(host, r);
		} catch (IOException e) {
			// logger.log(Level.SEVERE, errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			errorMsg += "Received status code " + response.getStatusLine().getStatusCode();
			// logger.log(Level.SEVERE, errorMsg);
			throw new RuntimeException(errorMsg);
		}
		// logger.exiting();
	}
}
