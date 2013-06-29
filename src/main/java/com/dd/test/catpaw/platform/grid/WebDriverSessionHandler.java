package com.dd.test.catpaw.platform.grid;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;




/**
 * <P>
 * By default the WebDriver Session will get timed out based on the value of
 * "timeout" configuration variable in localnode.json file. In some cases,
 * testcase need more time to do their non ui task before coming back to UI
 * related operation. If the non UI task takes more time than the configuration
 * timeout value, we will get RuntimeException with message "Session timed out".
 * <P>
 * 
 * 
 * {@link WebDriverSessionHandler} is used to hold WebDriver session while
 * performing non UI operations.
 * 
 * <P>Code Sample : </P>
 *  <pre>
 *       Grid.driver().get(CatPawConfig.getConfigProperty(CatPawConfigProperty.catpaw_URL));
 *       
 *       Grid.driver().findElementById("login_email").sendKeys("beamdaddy@catpaw.com");
 *       Grid.driver().findElementById("login_password").sendKeys("11111111");
 *       Grid.driver().findElementByName("submit.x").click();
 *       
 *       WebDriverSessionHandler m = new WebDriverSessionHandler(Grid.driver());
 *       m.start();
 *       
 *       //<<include non-ui operation here>>
 *       
 *       m.stop();
 *       Grid.driver().findElementByLinkText("Send Money").click();
 *        
 *       assertTrue(Grid.driver().getTitle().equals("Send Money - "));
 * </pre>
 * 
 */
public class WebDriverSessionHandler {

//    final static SimpleLogger logger = CatPawLogger.getLogger();

    private ScreenShotRemoteWebDriver driver;
    private Future<String> result;
    private WebDriverCaller webDriverCaller;

    volatile boolean bStartSession = false;

    private class WebDriverCaller implements Callable<String> {

        public String call() throws Exception {
    //    	// logger.entering();
            try {
                while (bStartSession) {
                    driver.findElementByTagName("*");
                    Thread.sleep(1000 * 10);
                }
            } catch (InterruptedException e) {
        //    	// logger.exiting(null);
                return null;
            }
//    //        // logger.exiting(null);
            return null;
        }
    }

    /**
     * This constructor creates WebDriverSessionHandler instance.
     * 
     * @param driver
     *            need to pass ScreenShotRemoteWebDriver instance
     *            [Grid.driver()]
     */
    public WebDriverSessionHandler(ScreenShotRemoteWebDriver driver) {

        if (driver == null) {
            throw new RuntimeException("ScreenShotRemoteWebDriver instance is null");
        }
        this.driver = driver;
        webDriverCaller = new WebDriverCaller();

    }

    /**
     * {@link WebDriverSessionHandler#start()} will start a child thread
     * that will keep pooling the title of the page so the the web session will
     * not get timeout.
     */
    public void start() {

//    	// logger.entering();

        if (bStartSession == true) {
            throw new RuntimeException("WebDriverSessionHandler is already started");
        }

        bStartSession = true;

        result = Executors.newSingleThreadExecutor().submit(webDriverCaller);
//        // logger.exiting();

    }

    /**
     * {@link WebDriverSessionHandler#stop()} will stop the polling child
     * thread.
     * 
     * @throws ExecutionException
     *             thrown when exceptions occur in the child thread while
     *             polling.
     */
    public void stop() throws ExecutionException {

//    	// logger.exiting();

        if (bStartSession == false) {
            throw new RuntimeException("Please call startSession() before calling endSession()");
        }
        bStartSession = false;

        try {
            result.get();
        } catch (InterruptedException e) {

//    //        // logger.log(Level.SEVERE, "InterruptedException occured while pinging the WebDriver", e);
        }

//        // logger.exiting();
    }
}
