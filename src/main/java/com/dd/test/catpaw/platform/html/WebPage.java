package com.dd.test.catpaw.platform.html;

/**
 * A generic interface for Web Page Objects in CatPaw.
 *
 */
public interface WebPage {

	/**
	 * Provides the mechanism to wait for this page to load
	 */
	public void waitForPage();
	
	/**
	 * Initialize the page by it's name and page path
	 * @param pagePath
	 * @param pageClassName
	 */
	public void initPage(String pagePath, String pageClassName);
	
	/**
	 * Initialize the page by it's name, page path, and site locale
	 * @param pagePath
	 * @param pageClassName
	 * @param siteLocale
	 */
	public void initPage(String pagePath, String pageClassName, String siteLocale);
	
	/**
	 * Return initialization state
	 */
	public boolean isInitialized();
	
	/**
	 * Return the expected page title for this page
	 */
	public String getExpectedPageTitle();
	
	/**
	 * Return the current siteLocale setting for this page
	 */
	public String getSiteLocale();
	
	/**
	 * Return the WebPage object
	 * @return 
	 * 		a {@link WebPage}
	 */
	public abstract WebPage getPage();
		
}
