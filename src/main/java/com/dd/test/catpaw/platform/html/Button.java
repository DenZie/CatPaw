package com.dd.test.catpaw.platform.html;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.Grid;
import com.dd.test.catpaw.platform.grid.ScreenShotRemoteWebDriver;

/**
 * This class is the web element Button wrapper for handling infrastructure testing technology.
 * <p>
 * In this class, the method 'click' is encapsulated and invoke claws session to do the click against the specified
 * element.
 * </p>
 * 
 */
public class Button extends AbstractElement {

	/**
	 * Button Construction method<br>
	 * <b>Usage:</b> <br> {@code Button btnSignIn = new Button(
	 * "//Button[@id='SignIn']"}
	 * 
	 * @param locator
	 *            the element locator
	 * 
	 */
	public Button(String locator) {
		super(locator);
	}

	public Button(String locator, String controlName) {
		super(locator, controlName);
	}

	/**
	 * The Button click function and wait for page to load
	 */
	public void click() {
	    click(new Object[]{});
	}

	/**
	 * Basic click event on the Button, doesn't wait for page to load.
	 * @deprecated  As of CatPaw <> this method stands deprecated.
	 * Please use {@link Button#click()} instead.
	 */
	//TODO: Include CatPaw version here.
	public void clickonly() {
	    click();
	}

	/**
	 * The button click function and wait for expected {@link Object} items to load.
	 * 
	 * @param expected
	 *            - parameters in the form of an XPath element locator {@link String}, a {@link WebPage}, a
	 *            {@link Button}, a {@link TextField}, an {@link Image}, a {@link Form}, a {@link Label}, a
	 *            {@link Table}, a {@link SelectList}, a {@link CheckBox}, or a {@link RadioButton}.
	 */
    public void click(Object... expected) {
        getElement().click();
        if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING)) {
            logUIAction(UIActions.CLICKED);
        }
        // If there are no expected objects, then it means user wants this method
        // to behave as a clickonly. So lets skip processing of alerts and leave that to the
        // user.
        if (expected == null || expected.length == 0) {
            return;
        }
        validatePresenceOfAlert();
        for (Object expect : expected) {
            ScreenShotRemoteWebDriver driver = (ScreenShotRemoteWebDriver) Grid.driver();
            if (expect instanceof AbstractElement) {
                AbstractElement a = (AbstractElement) expect;
                driver.waitUntilElementPresent(a.getLocator());
            }
            if (expect instanceof String) {
                String s = (String) expect;
                driver.waitUntilElementPresent(s);
            }
            if (expect instanceof WebPage) {
                WebPage w = (WebPage) expect;
                w.waitForPage();
            }
        }
        processScreenShot();
    }
	
	@Override
    protected String fetchClassName(){
		return Thread.currentThread().getStackTrace()[6].getClassName();
    }

}