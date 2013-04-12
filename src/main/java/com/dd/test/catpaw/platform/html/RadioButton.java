package com.dd.test.catpaw.platform.html;

import org.openqa.selenium.remote.RemoteWebElement;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.Grid;

/**
 * This class is the web element RadioButton wrapper for handling infrastructure
 * testing technology.
 * <p>
 * In this class, the method 'check' is encapsulated and invoke CatPaw session
 * to do the check against the specified element. The method 'isChecked' is to
 * verify whether this element is checked.
 * </p>
 * 
 */
public class RadioButton extends AbstractElement {

	/**
	 * RadioButton Construction method<br>
	 * <b>Usage:</b><br>{@code private RadioButton rbnElement = new
	 * RadioButton("//Radio[@id='dummyElement']"}
	 * 
	 * @param locator
	 *            the element locator
	 */
	public RadioButton(String locator) {
		super(locator);
	}
	public RadioButton(String locator, String controlName) {
		super(locator, controlName);
	}
	/**
	 * The RadioButton check function
	 * 
	 * It invokes CatPaw session to handle the check action against the
	 * element.
	 */
	public void check() {
		if (!isChecked()) {
			this.click();
		}
	}
	/**
	 * The RadioButton click function and wait for page to load
	 */
	public void click() {
		getElement().click();
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING)){
			logUIAction(UIActions.CLICKED);
		}
	}

	/**
	 * The RadioButton click function and wait for object to load
	 */
	public void click(String locator) {
		getElement().click();
		validatePresenceOfAlert();
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING)){
			logUIAction(UIActions.CLICKED);
		}
		Grid.driver().waitUntilElementPresent(locator);
	}

	/**
	 * The RadioButton isChecked function
	 * 
	 * It invokes CatPaw session to handle the isChecked function against the
	 * element.
	 */
	public boolean isChecked() {
		return  ((RemoteWebElement) getElement()).isSelected();
	}
}
