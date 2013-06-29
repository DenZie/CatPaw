package com.dd.test.catpaw.reports.reporter.html.filter;

import org.testng.ITestResult;

public class OrFilter implements Filter {

	private Filter[] filters;

	public OrFilter(Filter... filters) {
		this.filters = filters;
	}

	public boolean isValid(ITestResult result) {
		for (int i = 0; i < filters.length; i++) {
			if (filters[i].isValid(result)) {
				return true;
			}
		}
		return false;
	}
}
