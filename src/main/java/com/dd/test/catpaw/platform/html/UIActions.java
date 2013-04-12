package com.dd.test.catpaw.platform.html;

/**
 * This enum class represents all the "Actions" that can be done by a user on a given HTML Element.
 * This enum class is referred to while performing UI Logging.
 * 
 */
enum UIActions {
	CLICKED("Clicked "),
	CHECKED("Checked "),
	UNCHECKED("Un-checked "),
	SELECTED("Selected "),
	ENTERED("Entered "),
	CLEARED("Cleared text ");
	private UIActions(String action){
		this.action = action;
	}
	private String action;
	
	public String getAction(){
		return this.action;
	}
	
}
