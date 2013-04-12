package com.dd.test.catpaw.platform.grid;

import java.io.File;
import java.util.HashMap;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.ServiceLoaderManager;
import com.dd.test.catpaw.reports.reporter.services.TestCaseUtility;
import com.dd.test.catpaw.reports.runtime.WebReporter;
//import com.dd.test.catpaw.reports.reporter.services.TestCaseCalData;

/**
 * Contains the logic that will take care of all the selenium related
 */
public class SeleniumGridListener implements IInvokedMethodListener, ISuiteListener, ITestListener{

	// used to track browser sessions across all threads
	// data structure format <HashMap<String "sessionName", CatPawWebSession>
	private volatile HashMap<String, CatPawWebSession> sessionMap;

	// private static SimpleLogger logger = CatPawLogger.getLogger();
	
	/**
	 * 
	 * Identifies which version and name of browser to start if it specified in
	 * &#064;webtest <br>
	 * <b>sample</b><br>
	 * 
	 * &#064;webtest(<b>browser="*firefox"</b>)<br>
	 * Identifies if test case wants to open new session <br>
	 * <b>sample</b><br>
	 * &#064;webtest(browser="*firefox", <b>openNewSession = true</b>)
	 * 
	 * @see org.testng.IInvokedMethodListener#beforeInvocation(org.testng.IInvokedMethod,
	 *      org.testng.ITestResult)
	 */
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// logger.entering(new Object[] { method, testResult });
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		boolean webTest = method.isTestMethod()
				&& method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(WebTest.class) != null;

		TestCaseUtility.appendECaseId(method, testResult);
		if (webTest) {
			try {

				// Acquire @WebTest annotation parameter values specified by
				// user
				WebTestConfig webTestConfig = new WebTestConfig().initWebTestConfig(method, sessionMap, testResult);
				String sessionName = webTestConfig.getSessionName();
				// Lazy start the grid on the first encounter of a @WebTest
				// annotated test method
				LocalGridManager.spawnLocalHub(webTestConfig.getBrowser());

				synchronized (this) {
					Long threadId = Thread.currentThread().getId();

					if (webTestConfig.getOpenNewSession()) {
						// if we already have a session with this name, on this
						// thread, close it
						if (sessionMap.containsKey(sessionName)) {
							IllegalStateException e = new IllegalStateException(
									"Found an existing  session ["
											+ sessionName
											+ "]. Please either change the session name to a unique value or re-use that session.");
							// logger.log(Level.SEVERE, e.getMessage(), e);
							throw e;

						}
						
						CatPawWebSession newSession = Grid.startSession(webTestConfig);
						sessionMap.put(sessionName, newSession);
//						if (logger.isLoggable(Level.FINE)) {
//							// logger.log(Level.FINE, "Thread " + threadId + " created new " + sessionName + " = "
//									+ sessionMap.get(sessionName).toString());
//						}
					} else {
						// try to switch into a session by the same name
						if (sessionMap.containsKey(sessionName)) {
							CatPawWebSession session = sessionMap.get(sessionName);
							if ((session == null) || (session.getWebDriver() == null)) {
								closeSession(sessionName, true);
								// Tell TestNG the exception occurred in
								// beforeInvocation
								IllegalStateException e = new IllegalStateException("The session " + sessionName
										+ " is already closed. It probably timed out.");
								// logger.log(Level.SEVERE, e.getMessage(), e);
								throw e;
							} else {
//								if (logger.isLoggable(Level.FINE)) {
//									// logger.log(Level.FINE, "Thread " + threadId + " switching into " + sessionName
//											+ " = " + session.toString());
//								}
								Grid.switchSession(sessionMap.get(sessionName), webTestConfig);
							}
						} else {
							IllegalStateException e = new IllegalStateException(
									"Unable to find an already existing session with name [" + sessionName + "].");
							// logger.log(Level.SEVERE, e.getMessage(), e);
							throw e;
						}
					}
				} // synchronized block
				// TODO : We need to be able to segregate CatPaw originated
				// Runtime Exceptions from other runtime
				// exceptions raised by Selenium or JSON. Including a marker for
				// this task here.
			} catch (RuntimeException e) {
				// We are looking for any additional unchecked exceptions that
				// Grid may have thrown
				String errorMsg = "An error occured while setting up the test environment. \nRoot cause: ";
				Reporter.log(errorMsg + e.getMessage(), true);
				// Tell TestNG the exception occurred in beforeInvocation
				RuntimeException runTimeException = new RuntimeException(errorMsg, e);
				testResult.setThrowable(runTimeException);
				// Time to raise an Exception to let TestNG know that the
				// configuration method failed
				// so that it doesn't start executing the test methods.
				// logger.log(Level.SEVERE, e.getMessage(), e);
				throw runTimeException;
			}
		}
		// logger.exiting();
	}
	
	/**
	 * Executes when test case is finished<br>
	 * 
	 * Identify if webtest wants to have session open, otherwise close session<br>
	 * <b>sample</b><br>
	 * &#064;webtest(browser="*firefox", <b>keepSessionOpen = true</b>)<br>
	 * Analyzes failure if any
	 * 
	 * @see org.testng.IInvokedMethodListener#afterInvocation(org.testng.IInvokedMethod,
	 *      org.testng.ITestResult)
	 * 
	 */
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// logger.entering(new Object[] { method, testResult });
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}

		boolean webTest = method.isTestMethod()
				&& method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(WebTest.class) != null;

//		TestCaseCalData.setTime(false, method, testResult);

		if (webTest == false) {
			// logger.exiting();
			return;
		}

		// Acquire @WebTest annotation parameter values specified by user
		WebTestConfig webTestConfig = Grid.getWebTestConfig();
		//WebTestConfig would be a Null only under the following conditions (ofcourse because of us throwing an exception)
		//a.	If the test-case with same session name already exists.
		//b.	If we are unable to find an already existing session with name provided
		//c.	Session with the session name is already closed.
		if (webTestConfig == null){
			return;
		}

		if (!webTestConfig.getKeepSessionOpen()) {
			closeSession(webTestConfig.getSessionName(), true);
		}
		// logger.exiting();
	}

	/*
	 * go through and close all open sessions
	 */
	private synchronized void closeAllSessions() {
		// logger.entering();
		// Close all open sessions
		for (String key : sessionMap.keySet()) {
			closeSession(key, false);
			sessionMap.put(key, null);
		}
		sessionMap.clear();
		// logger.exiting();
	}

	/*
	 * close a sessions by name. optionally, also remove it from the sessionMap
	 */
	private synchronized void closeSession(String name, boolean removeSessionFromMap) {
		// logger.entering(new Object[] { name, removeSessionFromMap });
		try {
			if ((sessionMap.get(name) != null) && (sessionMap.get(name).getWebDriver() != null)) {
				Grid.closeSession(sessionMap.get(name));
			}
		} catch (Throwable e) {
			// Ignore ... Grid.closeSession seems to ALWAYS throws something.
		} finally {
			sessionMap.put(name, null);
		}

		if (removeSessionFromMap) {
			sessionMap.remove(name);
		}
		// logger.exiting();
	}

	/**
	 * Initiate config on suite start
	 * 
	 * @see org.testng.ISuiteListener#onStart(org.testng.ISuite)
	 */
	public void onStart(ISuite suite) {
		// Look at public void onStart(ITestContext context) from other Listener
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}

		CatPawConfig.initConfig(suite);
	}

	/**
	 * Generates and returns output directory string path
	 * 
	 * @param base
	 *            String shows path to the suiteName
	 * @param suiteName
	 *            String suiteName specified by config file
	 * @return String - path to output directory for that particular suite
	 */
	public static String filterOutputDirectory(String base, String suiteName) {
		// logger.entering(new Object[] { base, suiteName });
		int index = base.lastIndexOf(suiteName);
		String outputFolderWithoutName = base.substring(0, index);
		// logger.exiting(outputFolderWithoutName + File.separator);
		return outputFolderWithoutName + File.separator;
	}

	/**
	 * Closes selenium session when suite finished to run
	 * 
	 * @see org.testng.ISuiteListener#onFinish(org.testng.ISuite)
	 */
	public void onFinish(ISuite suite) {
		// logger.entering(suite);
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}

//		if (logger.isLoggable(Level.FINE)) {
//			// logger.log(Level.FINE, "Thread " + Thread.currentThread().getId() + " finished Suite, Open Sessions = "
//					+ sessionMap.toString());
//		}
		closeAllSessions();
		LocalGridManager.shutDownHub();
		// logger.exiting();
	}

	/**
	 * 
	 * @see org.testng.ISuiteListener#onFinish(org.testng.ISuite)
	 */
	public void onFinish(ITestContext context) {
		//Below conditional check needs to be invoked in all TestNG Listener interface implementation.
		//Failing to do so can have un-predictable results.
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		return;
	}

	/**
	 * On start each suite initialize config object and report object
	 */

	public void onStart(ITestContext context) {
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}

		sessionMap = new HashMap<String, CatPawWebSession>();

		ISuite suite = context.getSuite();
		CatPawConfig.initConfig(context);

		String base = suite.getOutputDirectory();
		String suiteName = suite.getName();
		String rootFolder = filterOutputDirectory(base, suiteName);
		WebReporter.setTestNGOutputFolder(rootFolder);
		WebReporter.init();
	}


	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		//Below conditional check needs to be invoked in all TestNG Listener interface implementation.
		//Failing to do so can have un-predictable results.
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		return;

	}


	public void onTestFailure(ITestResult result) {
		//Below conditional check needs to be invoked in all TestNG Listener interface implementation.
		//Failing to do so can have un-predictable results.
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		return;
	}


	public void onTestSkipped(ITestResult result) {
		//Below conditional check needs to be invoked in all TestNG Listener interface implementation.
		//Failing to do so can have un-predictable results.
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		return;

	}


	public void onTestStart(ITestResult result) {
		//Below conditional check needs to be invoked in all TestNG Listener interface implementation.
		//Failing to do so can have un-predictable results.
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		return;
	}


	public void onTestSuccess(ITestResult result) {
		//Below conditional check needs to be invoked in all TestNG Listener interface implementation.
		//Failing to do so can have un-predictable results.
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		return;

	}
	
}
