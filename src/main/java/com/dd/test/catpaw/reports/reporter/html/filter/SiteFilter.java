package com.dd.test.catpaw.reports.reporter.html.filter;

import java.util.Arrays;

import org.testng.ITestResult;

public class SiteFilter implements Filter {

	String site;

	public SiteFilter(String site) {
		this.site = site;
	}

	@Override
	public boolean isValid(ITestResult result) {
		return Arrays.asList(result.getMethod().getGroups()).contains(site);
	}

}
