package com.dd.test.catpaw.reports.reporter.html.filter;

import org.testng.ITestResult;

public class AndFilter implements Filter {

	private Filter[] filters;

	public AndFilter(Filter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean isValid(ITestResult result) {
		for (int i = 0; i < filters.length; i++) {
			if (!filters[i].isValid(result)) {
				return false;
			}
		}
		return true;
	}

}
