package com.dd.test.catpaw.reports.reporter.html.filter;

import org.testng.ITestResult;

public class BlankFilter implements Filter {

	@Override
	public boolean isValid(ITestResult result) {
		return true;
	}

}
