package com.dd.test.catpaw.reports.reporter.services;

import org.testng.ITestResult;

import com.dd.test.catpaw.reports.reporter.html.HtmlReporterListener;

/**
 * This class captures the result of a test method execution along with the Error code and Error Description as returned
 * from EZTracker, post update. This class is used by {@link HtmlReporterListener} class internally.
 * 
 */
public class TestCaseInformation {

	private ITestResult result;
	private int errorCode;
	private String errorDesc;

	public TestCaseInformation(ITestResult result, int errorCode, String errorDesc) {
		this.result = result;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public ITestResult getResult() {
		return result;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

}
