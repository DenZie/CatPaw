package com.dd.test.catpaw.reports.runtime;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.OutputType;
import org.testng.Reporter;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.Grid;
import com.dd.test.catpaw.platform.grid.ScreenShotRemoteWebDriver;
import com.dd.test.catpaw.reports.model.Screenshot;
import com.dd.test.catpaw.reports.model.Source;
import com.dd.test.catpaw.reports.model.WebLog;


/**
 * Static log method allow you to create a more meaningful piece of log for a
 * web test. It will log a message, but also take a screenshot , get the URL of
 * the page and capture the source of the page.
 * 
 * @see Reporter
 */
public class WebReporter {

//    final static SimpleLogger logger = CatPawLogger.getLogger();

    private static ThreadLocal<WebReporter> threadLocalWebReporter = new ThreadLocal<WebReporter>();
    private static String output;
    private static DataSaver saver = null;
    private WebLog currentLog = null;
    private int index = 0;
    private UUID uuid;

    private WebReporter() {
        uuid = UUID.randomUUID();
    }

    private static WebReporter getCurrentTestWeblog() {
//    	// logger.entering();
        WebReporter current = threadLocalWebReporter.get();
        if (current == null) {
            current = new WebReporter();
        }
//        // logger.exiting(current);
        return current;
    }

    /**
     * Sets string path to the output
     * 
     * @param rootFolder
     *            path to the output folder
     */
    public static void setTestNGOutputFolder(String rootFolder) {
        output = rootFolder;
    }

    /**
     * <ol>
     * <li>Provides saver with path to output information.
     * <li>Initializes saver.<br>
     * <li>Creates if missing output directories.<br>
     * </ol>
     */
    public static void init() {
//    	// logger.entering();
        saver = new SaverFileSystem(output);
        saver.init();
//        // logger.exiting();
    }

    /**
     * Generates log entry with message provided
     * 
     * @param message
     *            Entry description
     * @param takeScreenshot
     *            <b>true/false</b> take or not and save screenshot
     * @param saveSrc
     *            <b>true/false</b> save or not page source
     */
    public static void log(String message, boolean takeScreenshot, boolean saveSrc) {
//    	// logger.entering(new Object[]{message, takeScreenshot, saveSrc});
        try {
            // creating the log object.
            WebReporter current = getCurrentTestWeblog();
            current.currentLog = new WebLog();
            current.currentLog.setMsg(message);
            current.currentLog.setType("WEB");
            current.currentLog.setLocation(saveGetLocation());

            current.index++;
            String screenshotPath = null;

            if (takeScreenshot) {
                // screenshot
                Screenshot screen = new Screenshot(takeScreenshot(), current.getBaseFileName());
                screenshotPath = saver.saveScreenshot(screen);
                current.currentLog.setScreen(screenshotPath);
            } else {
                current.currentLog.setScreen(null);
            }

            if (saveSrc) {
                // source
                String source = null;
                if (Grid.driver() != null)
                    source = Grid.driver().getPageSource();
                Source src = new Source(source, current.getBaseFileName());
                saver.saveSources(src);
            }

            String href = null;
            /**
             * Changed html file extension to txt
             */
            if (saver instanceof SaverFileSystem) {
                if (saveSrc) {
                    href = "sources" + File.separator + current.getBaseFileName() + ".source.txt";
                } else if (takeScreenshot) {
                    href = screenshotPath;
                } else {
                    href = null;
                }

            } else {
                throw new RuntimeException("NI");
            }
            current.currentLog.setHref(href);

            // Added CAL id to the WebLog
            // This information would be read at time report preparation and
            // send to CAL service
            String cal = null;
            String source = null;
            current.currentLog.setCal(cal);

            // creating a string from all the info for the report to deserialize
            // System.out.println("log ---> " + current.currentLog.toString());
            Reporter.log(current.currentLog.toString());
            current.currentLog = null;
        } catch (Throwable e) {
//    //        // logger.log(Level.SEVERE,"error in the logging feature of claws " + e.getMessage(),e);
        }
//        // logger.exiting();
    }

    /**
     * This function will store the correlation ID to the WebLog object. The
     * call to this function will be made by the users after they get the
     * response back from the API calls. Since API calls can be made at many
     * location, it is not wise to intercept the returning responses and extract
     * the correlation ID automatically.
     * 
     * Screen captures and HTML capture are not necessary since API calls may
     * not show any web page.
     * 
     * @param id
     *            The correlcation ID as String
     */
    public static void setCorrelationId(String id) {
//    	// logger.entering(id);
        try {
            WebReporter current = getCurrentTestWeblog();
            current.currentLog = new WebLog();
            current.currentLog.setMsg("Correlation ID: ");
            current.currentLog.setType("API");
           	current.currentLog.setLocation(saveGetLocation());

            String cal = null;
            current.currentLog.setHref(null);
            current.currentLog.setCal(cal);
            Reporter.log(current.currentLog.toString());
            current.currentLog = null;
        } catch (Throwable e) {
//    //        // logger.log(Level.SEVERE,"error in the logging feature of claws " + e.getMessage(),e);
        }
//        // logger.exiting();
    }

    private static String saveGetLocation() {
//    	// logger.entering();
        String location = "n/a";
        try {
            location = Grid.driver().getCurrentUrl();
        } catch (NullPointerException exception) {
//    //        // logger.log(Level.WARNING,"Current location couldn't be retrieved by getCurrentUrl().", exception);
        }
//        // logger.exiting(location);
        return location;
    }

    private static byte[] takeScreenshot() {
//    	// logger.entering();
        try {
            byte[] decodeBuffer = null;

            if (Grid.driver() != null) {
                ScreenShotRemoteWebDriver ssWebDriver = (ScreenShotRemoteWebDriver)Grid.driver();
                // Comment out the call to maximize to avoid
                // crashes in Grid 2. We will find a permanent 
                // solution for this in a near future.
                // ssWebDriver.maximize();
                String ss = ssWebDriver.getScreenshotAs(OutputType.BASE64);
                decodeBuffer = Base64.decodeBase64(ss.getBytes());
            }
//    //        // logger.exiting(decodeBuffer);
            return decodeBuffer;
        } catch (Exception exception) {
//    //        // logger.log(Level.WARNING,"Screenshot couldn't be retrieved by getScreenshotAs().", exception);
            return null;
        }
    }

    private String getBaseFileName() {
        return uuid + "-" + index;
    }

}

enum Nav {
    PREV, CURRENT, NEXT;
}
