package com.dd.test.claws.platform.html;

/**
 * This class return the HTML object location and 
 * the HTML page URL.
 */
public enum TestObjectRepository {
	TEXTFIELD_LOCATOR("normal_text"),
	TEXTFIELD_DISABLED_LOCATOR("disabled_text"),
	SELECTLIST_LOCATOR("normal_select"),
	BUTTON_SUBMIT_LOCATOR("id=submitButton"),
	CHROME_BUTTON_SUBMIT_LOCATOR("id=navigateButton"),
	LINK_LOCATOR("confirmAndLeave"),
	CHROME_LINK_LOCATOR("ChromeconfirmAndLeave"),
	CHECKBOX_BEANS_LOCATOR("option-beans"),
	CHECKBOX_CHILLI_LOCATOR("option-chilli"),
	RADIOBUTTON_SPUD_LOCATOR("base-spud"),
	RADIOBUTTON_RICE_LOCATOR("base-rice"),
	FORM_SEARCH("searchForm"),
	LABEL_EDITABLE("//th[contains(text(),'Editable text-field')]"),
	IMAGE_TEST("Earth"),
	CHROME_IMAGE_TEST("id=ChromeEarth"),
	SUCCESS_PAGE_TEXT("//h1[contains(text(),'Success Page')]"),
	TEXT_AREA_LOCATOR("myTextarea"),
	TABLE_LOCATOR("id=TestTable"), 
	COMPLETED_LINK_LOCATOR("id=Complted|id=completed1"),
	COMPLETED_LINK_LOCATOR_NEG("id=Complted|id=CompletedTest"),
	CHECKBOX_LOCATOR("selection1"),
	URL("http://clawslvs24.qa.paypal.com/UnitTest/test_editable.html");

	private String locator;

	private TestObjectRepository(String locator) {
		this.locator = locator;
	}

	public String getValueToUse() {
		return locator;
	}
}
