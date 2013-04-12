package com.dd.test.catpaw.platform.grid;



/**
 * An enum class that represents the browser flavors supported by CatPaw
 */
public enum BrowserFlavors {
    FIREFOX ("*firefox"),
    INTERNET_EXPLORER("*iexplore"), 
    HTMLUNIT("*htmlunit"), 
    CHROME("*chrome"), 
    IPHONE("*iphone"), 
    IPAD("*ipad"),
    SAFARI("*safari"),
    OPERA("*opera"),
    ANDROID("*android");
    private String browser;

    private BrowserFlavors(String browser) {
        this.browser = browser;
    }

    /**
     * Returns the browser flavor as a string
     * 
     * @return - A string that represents the browser flavor in question
     */
    public String getBrowser() {
        return this.browser;
    }
    
    /**
     * This method returns all the browser flavors that are supported by the CatPaw framework
     * as a String with each value delimited by a comma.
     * @return - A comma separated string that represents all supported browser flavors.
     */
    public static String getSupportedBrowsersAsCSV(){
    	StringBuffer buffer = new StringBuffer();
    	String delimiter = ",";
    	for (BrowserFlavors flavor : BrowserFlavors.values()){
    		buffer.append(flavor.getBrowser() + delimiter);
    	}
    	buffer.deleteCharAt(buffer.length()-1);
    	return buffer.toString();
    }

}
