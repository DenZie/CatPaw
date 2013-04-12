package com.dd.test.catpaw.platform.grid;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.ExtendedConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;


/**
 * A class for loading and representing the {@link WebTest} annotation
 * parameters. Also performs sanity checks.
 */
public class WebTestConfig {
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();
    private String sessionName;
    private String browser;
    private Boolean keepSessionOpen;
    private Boolean openNewSession;
    private String[] dependsOnMethods;
    private String methodName;
    private String className;
    private DesiredCapabilities additionalCapabilities;
    private String parameters;

    WebTestConfig() {
        sessionName = "";
        browser = "";
        keepSessionOpen = false;
        openNewSession = true;
        dependsOnMethods = new String[] {};
        methodName = "";
        className = "";
        additionalCapabilities = new DesiredCapabilities();
    }
    
	private String getParamsInfo(ITestResult testResult){
		String parameters = null;
		for (Object temp : testResult.getParameters()) {
			if (parameters == null)
				parameters = temp.toString();
			else
				parameters = parameters + "," + temp.toString();
		}
		return parameters;
	}
	
	/**
	 * @return - The test name for the current method which is formed by concatenating the Class name, Method name
	 * and Method parameters if any.
	 */
	public String getTestName(){
		String testName = getDeclaringClassName() + ":" + getMethodName()+"()";
		if (parameters != null){
			parameters = "["+parameters+"]";
			testName = testName + ":" + parameters;
		}
		return testName;
		
	}


    /**
     * Call this to initialize the {@link WebTestConfig} object from the TestNG
     * {@link IInvokedMethod}
     * 
     * @param method
     *            - the TestNG {@link IInvokedMethod}
     * @param sessionMap
     *            - the {@link HashMap} used to track sessions in {@link SeleniumGridListener}
     *            
     * @return - An object of type {@link WebTestConfig}
     */
    public synchronized WebTestConfig initWebTestConfig(IInvokedMethod method,
            HashMap<String, CatPawWebSession> sessionMap, ITestResult testResult) {
//    	// logger.entering(new Object[]{method, sessionMap, testResult});
        ITestNGMethod iTestNGMethodObject = method.getTestMethod();
        Method methodObject = iTestNGMethodObject.getConstructorOrMethod().getMethod();
        WebTest webTestAnnotation = methodObject.getAnnotation(WebTest.class);
        Test testAnnotation = methodObject.getAnnotation(Test.class);

        this.className = methodObject.getDeclaringClass().getName();
        this.methodName = iTestNGMethodObject.getMethodName();
        this.parameters = getParamsInfo(testResult);
        String sessionName = "";

		if (webTestAnnotation != null) {
			this.browser = webTestAnnotation.browser();
			this.openNewSession = webTestAnnotation.openNewSession();
			this.keepSessionOpen = webTestAnnotation.keepSessionOpen();
			sessionName = webTestAnnotation.sessionName();
			if (webTestAnnotation.additionalCapabilities().length !=0){
				HashMap<String, String> capabilityMap = parseIntoCapabilities(webTestAnnotation.additionalCapabilities());
				//We found some capabilities. Lets merge them.
				this.additionalCapabilities.merge(new DesiredCapabilities(capabilityMap));
			}
			Object additionalCapabilities = testResult.getAttribute(ExtendedConfig.CAPABILITIES.getConfig()); 
			if ( additionalCapabilities != null && additionalCapabilities instanceof DesiredCapabilities){
				this.additionalCapabilities.merge((DesiredCapabilities)additionalCapabilities);
			}

		}
		if (testAnnotation != null) {
			this.dependsOnMethods = testAnnotation.dependsOnMethods();
		}

        setSessionName(sessionName, sessionMap, testResult);
//        // logger.exiting(this);
        return this;
    }
    
    private final HashMap<String, String> parseIntoCapabilities(String[] capabilities){
    	HashMap<String, String> capabilityMap = new HashMap<String, String>();
    	for (String eachCapability : capabilities){
    		String[] keyValuePair = eachCapability.split(":");
    		if (keyValuePair.length == 2){
    			capabilityMap.put(keyValuePair[0], keyValuePair[1]);
    		}else{
    			StringBuffer errMsg = new StringBuffer();
    			errMsg.append("Capabilities are to be provided as name value pair separated by colons. ");
    			errMsg.append("For e.g., capabilityName:capabilityValue"); 
    			throw new IllegalArgumentException(errMsg.toString());
    		}
    	}
    	return capabilityMap;
    }

    /**
     * Performs checks on and sets the sessionName properly
     * 
     * @param name
     * @param sessionMap
     */
    private final void setSessionName(String name, HashMap<String, CatPawWebSession> sessionMap, ITestResult testResult) {
//    	// logger.entering(new Object[]{name, sessionMap, testResult});
        this.sessionName = name;

        // Don't let sessionName == null
        if (this.sessionName == null) {
            this.sessionName = "";
        }

        // for named sessions we need to append the package and class info to
        // guarantee uniqueness
        if (!this.sessionName.equals("")) {
            this.sessionName = this.className + "." + this.sessionName;
            return;
        }

        // for un-named sessions that may want to stay open or connect to an
        // existing session
        if (this.sessionName.equals("")) {
            // default to this session name in most cases
            this.sessionName = "unamed-sesssion-on-thread" + Thread.currentThread().getId();

            // dynamically generate a session name, if the user wants a new
            // session and wants to keep it open.
            // session name will be "pacakge.class.method" name
            if ((this.keepSessionOpen) && (this.openNewSession)) {
                this.sessionName = this.className + "." + this.methodName;
                return;
            }

            // catch openNewSession=false when there are no dependent methods
            // specified
            if ((!this.openNewSession) && (this.dependsOnMethods.length == 0)) {
                testResult.setThrowable(new IllegalArgumentException(
                        "Can not have an unamed session without dependent methods and use"
                                + " an existing session. Error in " + this.className + "." + this.methodName));
    //    //        // logger.exiting();
                return;
            }

            // attempt to map existing session based on the dependsOnMethods
            // specification, when openNewSession = false
            if ((!this.openNewSession) && (this.dependsOnMethods.length > 0)) {
                String[] methods = this.dependsOnMethods;

                // go through the dependsOnMethods looking for open sessions
                // which map to the method names
                List<String> foundSessions = new ArrayList<String>();
                List<String> considered = new ArrayList<String>();
                for (String m : methods) {
                    String searchKey = this.className + "." + m;
                    if (sessionMap.containsKey(searchKey)) {
                        foundSessions.add(searchKey);
                    }
                    considered.add(searchKey);
                }

                // catch unsupported state errors
                // no matches from the dependOnMethods specified
                if (foundSessions.size() == 0) {
                    testResult.setThrowable(new IllegalStateException(
                            "Unable to find a session that matched selection criteria. " + "Considered "
                                    + considered.toString()));
                }
                // multiple in-flight sessions and dependent methods specified
                // that matched...
                if (foundSessions.size() > 1) {
                    testResult.setThrowable(new IllegalStateException(
                            "Ambiguos match. Found more than one session that " + "matched selection criteria "
                                    + foundSessions.toString()));
                }

                // set the sessionName to the matched name
                this.sessionName = foundSessions.get(0);
    //    //        // logger.exiting();
                return;
            }
        }
//        // logger.exiting();
    }

    /**
     * @return the session name for the test method
     */
    public final String getSessionName() {
        return sessionName;
    }
    
    public final DesiredCapabilities getAdditionalCapabilities(){
    	return this.additionalCapabilities;
    }

    /**
     * @return the browser configured for the test method
     */
    public final String getBrowser() {
//    	// logger.entering();
        // default the browser, if not specified
        if ((this.browser == null) || (this.browser.equals(""))) {
            this.browser = CatPawConfig.getConfigProperty(CatPawConfigProperty.BROWSER);
        }
//        // logger.exiting(this.browser);
        return this.browser;
    }

    /**
     * @return whether the test method requested the session stay open
     */
    public final Boolean getKeepSessionOpen() {
        return this.keepSessionOpen;
    }

    /**
     * @return whether the test method requested a new session be opened
     */
    public final Boolean getOpenNewSession() {
        return this.openNewSession;
    }

    /**
     * @return array of dependent methods specified by the test
     */
    public final String[] getDependsOnMethods() {
        return this.dependsOnMethods;
    }

    /**
     * @return the method name for the test
     */
    public final String getMethodName() {
        return this.methodName;
    }

    /**
     * @return the declaring class name for the test in the form
     *         <b>package.classname</b>
     */
    public final String getDeclaringClassName() {
        return this.className;
    }
}
