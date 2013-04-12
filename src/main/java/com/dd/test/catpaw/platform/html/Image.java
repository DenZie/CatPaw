package com.dd.test.catpaw.platform.html;

import org.openqa.selenium.remote.RemoteWebElement;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.Grid;

/**
 * This class is the web element Image wrapper for handling infrastructure testing technology.
 * <p>
 * In this class, the method 'click' are encapsulated and invoke claws session to do click against the specified
 * element.
 * </p>
 * 
 */
public class Image extends AbstractElement {
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	String currentPage;

	/**
	 * Button Construction method<br>
	 * <b>Usage:</b><br>
	 * {@code private Image imgeBayIcon = new Image("//Image[@id='eBayIcon']"}
	 * 
	 * @param locator
	 *            the element locator
	 */
	public Image(String locator) {
		super(locator);
	}

	public Image(String locator, String controlName) {
		super(locator, controlName);
	}

	/**
	 * This function is to get image's width.
	 * 
	 * @return the image's width
	 * 
	 * @throws RuntimeException
	 */
	public int getWidth() {
		try {
			return ((RemoteWebElement) getElement()).getSize().width;
		} catch (NumberFormatException e) {
			throw new RuntimeException("Attribute " + WIDTH + " not found for Image " + getLocator());
		}
	}

	/**
	 * This function is to get image's height.
	 * 
	 * @return the image's height
	 * 
	 * @throws RuntimeException
	 */
	public int getHeight() {
		try {
			return ((RemoteWebElement) getElement()).getSize().height;
		} catch (NumberFormatException e) {
			throw new RuntimeException("Attribute " + HEIGHT + " not found for Image " + getLocator());
		}
	}

	/**
	 * This click function just performs the click operation on HtmlImage.
	 * @deprecated  As of CatPaw <> this method stands deprecated.
     * Please use {@link Image#click()} instead.
	 */
	//TODO: Include CatPaw version here.
	public void clickonly() {
	    click();
	}

	/**
	 * The Image click function and wait for page to load
	 */
	public void click() {
	    click(new Object[]{});
	}

	/**
	 * The Image click function and wait for expected {@link Object} items to load.
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
