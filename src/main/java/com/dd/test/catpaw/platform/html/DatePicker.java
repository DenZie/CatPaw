package com.dd.test.catpaw.platform.html;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.dd.test.catpaw.platform.grid.Grid;
import com.dd.test.catpaw.platform.grid.ScreenShotRemoteWebDriver;

/**
 * DatePicker is a widget which shows up a monthly calendar
 * when clicked. It comes with "previous month" and "next month" 
 * navigators, and a tablet of clickable days in a month. Users
 * can navigate by clicking these item to arrive at a desired date.
 * 
 * How to make DatePicker work in excel page data declaration?
 * Since the DatePicker needs other three object to operate properly
 * (Orevious Button, Next Button, and Date Label) these object also
 * need to declare and initialize before the DatePicker can work. 
 * To initialize this DatePicker a user needs to call datePickerInit
 * before perform any actions on the DatePicker.
 *  
 */
public class DatePicker extends AbstractElement{
	private String 		prevMonthLocator;
	private String 		nextMonthLocator;
	private String 		dateTextLocator;
	private Calendar	calendar = Calendar.getInstance();
	
	/**
	 * This constructor assumess the "prev month" and "next month"
	 * locators and assign them to the variables as defaults.
	 * 
	 * @param datePickerLoc -  the calendar locator
	 */
	public DatePicker (String datePickerLoc) {
		super(datePickerLoc);
		prevMonthLocator	= datePickerLoc + "/div/div/a[1]/span";
		nextMonthLocator	= datePickerLoc + "/div/div/a[2]/span";
		dateTextLocator		= "//div[contains(@class,'ui-datepicker-title')]";
	}
	
	/**
	 * This constructor provides a way for users to initialize
	 * their calendar by specifying all calendar's parameters.
	 * 
	 * @param datePickerLoc
	 */
	public DatePicker (String datePickerLoc, String name) {
		super(datePickerLoc, name);
	}
	
	public DatePicker (String datePickerLoc, String prevMonthLoc, String nextMonthLoc, String dateLabelLoc) {
		super(datePickerLoc);
		prevMonthLocator = prevMonthLoc;
		nextMonthLocator = nextMonthLoc;
		dateTextLocator = dateLabelLoc;
		
	}
	
	/**
	 * Advances one month to the future.
	 */
	public void clickNextMonth () {
		AbstractElement.locateElement(nextMonthLocator).click();
	}
	
	/**
	 * Goes back one month in history.
	 */
	public void clickPrevMonth () {
		AbstractElement.locateElement(prevMonthLocator).click();
	}
	
	/**
	 * Click on the day-of-month tablet to select
	 * the desired day of the month to set date.
	 * 
	 * @param dayOfMonth
	 */
	public void clickDay (int dayOfMonth) {
		String dayLocator;
		
		dayLocator = "//a[contains(text(),'" + dayOfMonth + "')]";
		AbstractElement.locateElement(dayLocator).click();
	}
	
	/**
	 * Gets the current setting month and year from the 
	 * calendar header.
	 * 
	 * @return current setting month and year
	 */
	public String getDateText () {
		String value = null;
		value = AbstractElement.locateElement(dateTextLocator).getText();
		return value;
	}
	
	private void navigateMonth (Calendar from, Calendar to){
		int 	monthNav;
		int 	yearNav;
		int 	monthCount;

		monthNav = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
		yearNav	= to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
		
		monthCount = (12 * yearNav) + monthNav - 1;
		
		// if the month count is negative, the "to" date
		// is earlier than the "from" date, we have to go
		// back in time to arrive at the "to" date.
		if(monthCount < 0){
			monthCount = Math.abs(monthCount);
			for (int i=0; i<monthCount; i++){
				clickPrevMonth();
				((ScreenShotRemoteWebDriver)Grid.driver()).waitUntilElementVisible(this.prevMonthLocator);
			}
		}
		else
		{
			for (int i=0; i<monthCount; i++){
				clickNextMonth();
				((ScreenShotRemoteWebDriver)Grid.driver()).waitUntilElementVisible(this.nextMonthLocator);
			}
		}
	}
	
	/**
	 * This is the main function of the DatePicker object 
	 * to select the date specified by the input parameter.
	 * It will calculate how many time needed to click on
	 * the "next month" or "previous month" button to arrive
	 * at the correct month and year in the input parameters.
	 * It then click on the day-of-month tablet to select the
	 * correct day-of-month as specified in the input param.
	 * 
	 * @param to destination date as Java Calendar format.
	 */
	public void setDate (Calendar to) {		
		
		// Navigate from the current date
		// to the new date
		navigateMonth(calendar, to);
		
		// Select the day-of-month.
		clickDay(to.get(Calendar.DATE));
		
		calendar.set(Calendar.MONTH, to.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, to.get(Calendar.DATE));
		
	}
	
	/**
	 * This function set the date on the DatePicker 
	 * using the input paramter in the format of
	 * "MM.dd.yyyy" string
	 * 
	 * @param date string input date format of "MM/dd/yyyy"
	 */
	public void setDate (String date) {
		String[] dateSplit = new String[3];
		dateSplit = date.split("/");
		
		Calendar to = Calendar.getInstance();
		
		to.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));
		
		setDate(to);
		
	}
	
	public void setDate (int year, int month, int day) {
		Calendar to = Calendar.getInstance();
		to.set(year, month, day);
		
		setDate(to);
	}
	
	/**
	 * This function return the current date of 
	 * a date picker in the format of "MM/dd/yyyy"
	 * 
	 * @return - the current date
	 */
	public String getDate () {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String date = formatter.format(calendar.getTime());
		
		return date;
		
	}
	
	public String getMonth () {
		return String.valueOf(calendar.get(Calendar.MONTH));
		
	}
	
	public String getYear () {
		return String.valueOf(calendar.get(Calendar.YEAR));
		
	}
	
	public String getDayOfMonth () {
		return String.valueOf(calendar.get(Calendar.DATE));
		
	}

	public void datePickerInit ( String prevMonthLoc, String nextMonthLoc, String dateTextLoc){
		prevMonthLocator	= prevMonthLoc;
		nextMonthLocator	= nextMonthLoc;
		dateTextLocator		= dateTextLoc;
	}
}
