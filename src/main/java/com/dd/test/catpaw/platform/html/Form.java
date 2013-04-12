package com.dd.test.catpaw.platform.html;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.grid.Grid;

/**
 * This class is the web element Form wrapper for handling infrastructure
 * testing technology.
 * <p>
 * In this class, the method 'submit' are encapsulated and invoke claws
 * session to do submit against the specified element.
 * </p>
 * 
 */
public class Form extends AbstractElement {

	/**
	 * Form Construction method<br>
	 * <b>Usage:</b><br>
	 * {@code private Form frmDummy = new Form("//Form[@name='DummyForm']"}
	 * 
	 * @param locator
	 *            the element locator
	 */
	public Form(String locator) {
		super(locator);
	}
	public Form(String locator, String controlName) {
		super(locator, controlName);
	}
	/**
	 * The Form submit function It invokes CatPaw session to handle the submit
	 * action against the element.
	 */
	public void submit() {
		AbstractElement.locateElement(getLocator()).submit();
		Grid.driver().waitForPageToLoad(CatPawConfig
                                .getConfigProperty(CatPawConfigProperty.EXECUTION_TIMEOUT));
	}
}
