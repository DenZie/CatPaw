package com.dd.test.catpaw.reports.reporter.html.filter;

import java.util.Arrays;

import org.testng.ITestResult;

public class EnvironementFilter implements Filter {

	public boolean isValid(ITestResult result) {
		return Arrays.asList(result.getMethod().getGroups()).contains("envt");
	}

}
