package com.dd.test.catpaw.platform.html;


/**
 * This class is the web Text wrapper for handling infrastructure testing
 * technology.
 * <p>
 * It is mainly to verify the Text on the web page.
 * </p>
 * 
 */
public class Label extends AbstractElement {

	/**
	 * Label Construction method<br>
	 * <b>Usage:</b><br>{@code private Label lblWelcome = new Label("//Div[@id='Welcome']"}
	 * @param locator the element locator
	 */
	public Label(String locator) {
		super(locator);
	}
	public Label(String locator, String controlName) {
		super(locator, controlName);
	}
	/**
	 * It is to check whether the element's text matches with the specified
	 * pattern.
	 * 
	 * @param pattern regular expression
	 * 
	 * @return boolean <b>true</b> - the element's text matches with the pattern. <br>
	 * <b>false</b> the element's text doesn't match with the pattern.
	 */
	public boolean isTextPresent(String pattern) {
		String text = getElement().getText();
		boolean textpresent =(text != null && (text.contains(pattern) || text
				.matches(pattern)));
		return textpresent;
	}

}
