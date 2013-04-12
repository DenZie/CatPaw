package com.dd.test.catpaw.reports.reporter.html;

import java.util.List;

import org.testng.ISuite;

public interface View {

	public void setData(List<ISuite> suites);

	public String getContent();

	public String getId();

	public String getTitle();
}
