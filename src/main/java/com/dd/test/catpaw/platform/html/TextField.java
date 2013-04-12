package com.dd.test.catpaw.platform.html;

import org.openqa.selenium.remote.RemoteWebElement;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;

/**
 * This class is the web element Input wrapper for handling infrastructure
 * testing technology.
 * <p>
 * In this class, the method 'type' is encapsulated and invoke claws session
 * to type content to the specified element.
 * </p>
 * 
 */

public class TextField extends AbstractElement {

	/**
	 * TextField Construction method<br>
	 * <b>Usage:</b><br>{@code private TextField txtTitle = new TextField(
	 * "//Input[@id='title']"}
	 * 
	 * @param locator
	 *            the element locator
	 */
	public TextField(String locator) {
		super(locator);
	}
	public TextField (String locator, String controlName) {
		super(locator, controlName);
	}
	/**
	 * The TextField type function
	 * 
	 * It invokes CatPaw session to handle the type action against the element.
	 */
	public void type(String value) {
		RemoteWebElement element = getElement();
		element.clear();
		element.sendKeys(value);
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING)){
			logUIActions(UIActions.ENTERED, value);
		}
	}
	/** The TextField type function which allow users
	 * to keep the TextField and append the input text to it.
	 * 
	 * It invokes CatPaw session to handle the type action against the element. 
	 */
	public void type(String value, boolean isKeepExistingText) {
		if(isKeepExistingText == true) {
			getElement().sendKeys(value);
			if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING)){
				logUIActions(UIActions.ENTERED, value);
			}
		}
		else {
			type(value);
		}
	}
	/**
	 * Text TextField clear function
	 * 
	 * To clear the text box.
	 */
	public void clear() {
		getElement().clear();
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.ENABLE_GUI_LOGGING)){
			logUIAction(UIActions.CLEARED);
		}
	}
	/**
	 * The TextField isEditable function
	 * 
	 * It invokes CatPaw session to handle the isEditable function against the
	 * element.
	 */
	public boolean isEditable() {
		return ((RemoteWebElement) getElement()).isEnabled();
	}
	/**
	 * Get the text value from a TextField object.
	 * 
	 * @return text is the text in the TextField box.
	 */
	public String getText() {
		String text = getElement().getText();
		if(text.isEmpty()) {
			text = getValue();
		}
		return text;
	}
}
