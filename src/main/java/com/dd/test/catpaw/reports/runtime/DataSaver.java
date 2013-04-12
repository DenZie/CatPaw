package com.dd.test.catpaw.reports.runtime;

import com.dd.test.catpaw.reports.model.Screenshot;
import com.dd.test.catpaw.reports.model.Source;

public interface DataSaver {

	public void init();

	public String saveScreenshot(Screenshot s) throws Exception;

	public String saveSources(Source s);

	public Screenshot getScreenshotByName(String name) throws Exception;
}
