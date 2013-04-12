package com.dd.test.catpaw.platform.grid;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.html.AbstractElement;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

/**
 * DefaultSelenium is enough for 90% of the commands. If you have some custom
 * command in your user-extension, here is the place to bind them.
 * 
 */


/**
 * CatPawSelenium will be deprecated in the next few versions of CatPaw release.
 * For Selenium 2 support we will go with WebDriver and thus many CatPawSelenium 
 * functions will be ported to WebDriver object.
 * 
 * Currently, we are using CatPawSelenium for:<br>
 *
 * 1. Backward compatible with Selenium 1 <br>
 * 2. To support many methods which are not exists in WebDriver. 
 * 
 * @deprecated as of CatPaw 0.1.0
 */
public class CatPawSelenium extends WebDriverBackedSelenium {

    final static Logger logger = Logger.getLogger(Grid.class);

    /**
     * A constructor that returns an object of type {@link CatPawSelenium}
     * @param driver - A Web driver instance of type {@link WebDriver} to be passed on to the super class {@link WebDriverBackedSelenium}
     * @param baseUrl - A base URL to be launched
     */
    public CatPawSelenium(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated.
     * Please use <br>
     * 
     * <b>{@link Grid#getNewTimeOut()} instead</b>
     *
     */
    
    public long getNewTimeOut() {
        String stringTimeOut = CatPawConfig.getConfigProperty(CatPawConfigProperty.EXECUTION_TIMEOUT);
        return Long.parseLong(stringTimeOut.trim());
    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated. <br>
     * Please use:  <b> {@link AbstractElement#locateElements(String)}  instead</b> <br>
     * <br>
     * 
     * see getXPathCount from selenium Core. This returns the values.
     * 
     * @param xpath
     *            location of element should be found
     * @return list of elements share similar xPath
     */
    public List<String> getElements(String xpath) {
        try {
            String jsonString = commandProcessor.getString("getErrors", new String[] { xpath, });
            JSONObject obj = new JSONObject(jsonString);
            JSONArray jarray = obj.getJSONArray("errorsDetails");
            ArrayList<String> elements = new ArrayList<String>();
            for (int i = 0; i < jarray.length(); i++) {
                if (!"".trim().equals(jarray.getString(i))) {
                    elements.add(jarray.getString(i));
                }
            }
            return elements;
        } catch (JSONException e) {
            throw new RuntimeException("Error with JSON parsing.", e);
        }
    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated.
     * <br><br>
     * send some extra information that will stored by the hub for debug
     * purposes. Sets attribute <b>name</b> with <b>value</b>
     * 
     * @param name
     *            attribute name
     * @param value
     *            attribute value
     * @return result/output setAttribute command
     */
    public String setAttribute(String name, String value) {
        if (Grid.getRunOnLocalRC() == true) {
            return "";
        } else {
            return commandProcessor.getString("setAttribute", new String[] { name, value, });
        }

    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated. Please use <br>
     * <b> Grid.driver().waitUntilElementPresent(locator)</b>
     *  instead
     * <br><br>

     * Waits until element with locator elementLocator appears at the browser
     * 
     * @param elementLocator
     *            identifier of element to be found
     */
    public void waitUntilElementPresent(final String elementLocator) {
        String message = "Wait for element to appear " + elementLocator;
        new Wait() {
            @Override
            public boolean until() {
                return Grid.selenium().isElementPresent(elementLocator);
            }
        }.wait(message, getNewTimeOut());
    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated.
     * Please use 
     * <br><b>Grid.driver().waitUntilElementDisapear(locator) </b><br>
     * instead.
     * <br><br>

     * Waits until element with locator elementLocator disappears at the browser
     * 
     * @param elementLocator
     *            identifier of element to be found
     */    
    public void waitUntilElementDisapear(final String elementLocator) {
        String message = "Wait for element to appear " + elementLocator;
        new Wait() {
            @Override
            public boolean until() {
                return !Grid.selenium().isElementPresent(elementLocator);
            }
        }.wait(message, getNewTimeOut());
    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated.
     * Please use 
     * <br><b>Grid.driver().waitUntilTextPresent(locator) </b><br>
     * instead.
     * <br><br>

     * Waits until text appears at the browser
     * 
     * @param searchString
     *            text will be waited for
     */
    public void waitUntilTextPresent(final String searchString) {
        String message = "Wait for text to appear " + searchString;
        new Wait() {
            @Override
            public boolean until() {
                return Grid.selenium().isTextPresent(searchString);
            }
        }.wait(message, getNewTimeOut());

    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated.
     * Please use 
     * <br><b>Grid.driver().waitUntilTwoElementsPresent(locator1, locator2) </b><br>
     * instead.
     * <br><br>

     * Waits until both two elements appear at the page
     * 
     * @param elementLocator1
     *            identifier of first element expected to appear
     * @param elementLocator2
     *            identifier of second element expected to appear
     */
    public void waitUntilTwoElementsPresent(final String elementLocator1, final String elementLocator2) {
        for (int second = 0;; second++) {
            if (second >= 60)
                throw new SeleniumException("timedout");
            try {
                if (Grid.selenium().isElementPresent(elementLocator1)
                        && Grid.selenium().isElementPresent(elementLocator2))
                    break;
            } catch (Exception e) {
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated.
     * Please use 
     * <br><b>Grid.driver().waitUntilElementVisible(locator) </b><br>
     * instead.
     * <br><br>

     * Waits until element with locator elemLocator becomes visible
     * 
     * @param elementLocator
     *            identifier of element to be visible
     */
    public void waitUntilElementVisible(final String elementLocator) {
        String message = "Wait for element to get visible " + elementLocator;
        new Wait() {
            @Override
            public boolean until() {
                return Grid.selenium().isVisible(elementLocator);
            }
        }.wait(message, getNewTimeOut());
    }

    /**
     * @deprecated as of CatPaw 0.1.0 this method is deprecated.
     * Please use 
     * <br><b>Grid.driver().waitUntilPageTitlePresent(pageTitle) </b><br>
     * instead.
     * <br><br>

     * Waits until Page expected page title appears
     * 
     * @param pageTitle
     *            title of page expected to appear
     */
    public void waitUntilPageTitlePresent(final String pageTitle) {
        String message = "Wait for title to appear " + pageTitle;
        new Wait() {
            @Override
            public boolean until() {
                return pageTitle.indexOf(Grid.selenium().getTitle()) >= 0;
            }
        }.wait(message, getNewTimeOut());
    }
}
