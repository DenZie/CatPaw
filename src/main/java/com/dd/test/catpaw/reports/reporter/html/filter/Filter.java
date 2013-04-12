package com.dd.test.catpaw.reports.reporter.html.filter;

import org.testng.ITestResult;

public interface Filter {
	public boolean isValid(ITestResult result);
}
