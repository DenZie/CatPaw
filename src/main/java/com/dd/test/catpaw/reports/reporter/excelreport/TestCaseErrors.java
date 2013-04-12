package com.dd.test.catpaw.reports.reporter.excelreport;

import java.util.HashMap;
/**
 * 
 * Contains Error to Possible cause mapping
 *
 */
public class TestCaseErrors {

	private static final String WAIT_EXCEPTION =  "Wait Timed Out.  Load issue?";
	private static final String ELMNOTFOUND_EXCEPTION =  "Element not found. Locator issue? Page fully loaded? bug?";
	private static final String XHR_ERROR_EXCEPTION =  "Cert Error. Certificate added to profile?";
	private static final String BIND_EXCEPTION =  "Bind Exception. Kill all javaw.exe and retry.";
	private static final String ACC_CREATION_FAILURE =  "Acc Creation failure.  Passwordless setup? Stage load?";
	
	private static TestCaseErrors tcErrors = null;
	private HashMap<String, String> mpErrorsInfo = new HashMap<String, String>();
	
	private TestCaseErrors()
	{
		mpErrorsInfo.put("(?s).*WaitTimedOutException(?s).*", WAIT_EXCEPTION);
		mpErrorsInfo.put("(?s).*SeleniumException: ERROR: Element.*not found(?s).*", ELMNOTFOUND_EXCEPTION);
		mpErrorsInfo.put("(?s).*XHR ERROR: URL(?s).*",XHR_ERROR_EXCEPTION);
		mpErrorsInfo.put("(?s).*java.net.BindException(?s).*Address already in use(?s).*",BIND_EXCEPTION);
		mpErrorsInfo.put("(?s).*Account creation failed(?s).*",ACC_CREATION_FAILURE);
		mpErrorsInfo.put("(?s).*NoSuchElementException(?s).*",ELMNOTFOUND_EXCEPTION);
	}
	
	public static TestCaseErrors getInstance(){
		if (tcErrors == null)
			tcErrors = new TestCaseErrors();
		return tcErrors;
	}
	
	String debugError(Throwable defect){
		
		for (String errPattern : mpErrorsInfo.keySet()){
			if (defect.toString().matches(errPattern)){
				return mpErrorsInfo.get(errPattern);
			}
		}
		return null;
	}
	
	public void addError(String sErrorPattern, String sMsgToDisplay){
		this.mpErrorsInfo.put(sErrorPattern, sMsgToDisplay);
	}
}
