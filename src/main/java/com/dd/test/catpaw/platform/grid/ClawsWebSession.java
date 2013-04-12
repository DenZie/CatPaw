package com.dd.test.catpaw.platform.grid;

import org.openqa.selenium.WebDriverException;





/**
 * A utility class to represent CatPaw web sessions
 */
final class CatPawWebSession {
    private ScreenShotRemoteWebDriver webDriver;
    private CatPawSelenium selenium;
//    // private static SimpleLogger logger = CatPawLogger.getLogger();

    protected CatPawWebSession(ScreenShotRemoteWebDriver webDriver, CatPawSelenium selenium) {
        this.webDriver = webDriver;
        this.selenium = selenium;
    }

    protected ScreenShotRemoteWebDriver getWebDriver() {
        return this.webDriver;
    }

    /** @deprecated as of CatPaw 0.1.0. This method/member variable will no longer be required 
    * when {@link Grid#selenium()} and {@link CatPawSelenium} go away. In other words, when we no longer
    * have a selenium and driver pointing to the same web session.
    */
    protected CatPawSelenium getSelenium() {
        return this.selenium;
    }
    
    public String toString() {
//    	// logger.entering();
    	String returnValue = null;
        if (this.webDriver == null) {
        	returnValue = "CatPawWebSession {null}";
    //    	// logger.exiting(returnValue);
            return returnValue;
        }
        
        try {
            returnValue= "CatPawWebSession {sessionId=" + this.webDriver.getSessionId() + ", title="+
                this.webDriver.getTitle() + "}";
//    //        // logger.exiting(returnValue);
            return returnValue;
        }
        catch (WebDriverException e) {
            if (e.getLocalizedMessage().contains("Session not available")) {
                //session was probably killed by the HUB
                this.webDriver = null;
                this.selenium = null;
                returnValue = "CatPawWebSession {null}";
    //    //        // logger.exiting(returnValue);
                return returnValue;
            }
        }
        returnValue = super.toString();
//        // logger.exiting(returnValue);
        return returnValue;
    }
}
