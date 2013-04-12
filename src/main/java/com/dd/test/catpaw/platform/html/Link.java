package com.dd.test.catpaw.platform.html;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.Grid;

/**
 * This class is the web element Link wrapper for handling infrastructure testing technology.
 * <p>
 * In this class, the method 'click' is encapsulated and invoke claws session to do the click against the specified
 * element.
 * </p>
 * 
 */
public class Link extends AbstractElement {
	/**
	 * Link Construction method<br>
	 * <b>Usage:</b><br>
	 * {@code private Link lnkSYI = new Link("//a[contains(@href,'?eBayCommand')]"}
	 * 
	 * @param locator
	 *            the element locator
	 */
	public Link(String locator) {
		super(locator);
	}

	public Link(String locator, String controlName) {
		super(locator, controlName);
	}

	/**
	 * This Link click function just performs the click operation specially designed for links that on click gives a new
	 * page.
     * @deprecated  As of CatPaw <> this method stands deprecated.
     * Please use {@link Link#click()} instead.
     */
    //TODO: Include CatPaw version here.
	public void clickonly() {
	    click();
	}

	/**
	 * The Link click function and wait for page to load
	 */
    public void click() {
        click(new Object[] {});
    }

	/**
	 * The link click function and wait for expected {@link Object} items to load.
	 * 
	 * @param expected
	 *            - parameters in the form of an XPath element locator {@link String}, a {@link WebPage}, a
	 *            {@link Button}, a {@link TextField}, an {@link Image}, a {@link Form}, a {@link Label}, a
	 *            {@link Table}, a {@link SelectList}, a {@link CheckBox}, or a {@link RadioButton}.
	 */
    public void click(Object... expected) {
        getElement().click();
        if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING) == true) {
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
            if (expect instanceof AbstractElement) {
                AbstractElement a = (AbstractElement) expect;
                Grid.driver().waitUntilElementPresent(a.getLocator());
            }
            if (expect instanceof String) {
                String s = (String) expect;
                Grid.driver().waitUntilElementPresent(s);
            }
            if (expect instanceof WebPage) {
                WebPage w = (WebPage) expect;
                w.waitForPage();
            }
        }
        processScreenShot();
    }
}
