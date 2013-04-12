package com.dd.test.catpaw.reports.reporter.html.filter;

import org.testng.ITestResult;

public class StateFilter implements Filter {

	private int state;

	public StateFilter(int state) {
		this.state = state;
	}

	@Override
	public boolean isValid(ITestResult result) {
		return result.getStatus() == this.state;
	}

}
