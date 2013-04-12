package com.dd.test.catpaw.reports.reporter.html.splitters;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;



public class ByClassSplitter extends CollectionSplitter {

	@Override
	public List<String> getKeys(ITestResult result) {
//		CatPawLogger.getLogger().entering(result);
		List<String> res = new ArrayList<String>();
		res.add(result.getMethod().getRealClass().getName());
//		CatPawLogger.getLogger().exiting(res);
		return res;
	}

}
