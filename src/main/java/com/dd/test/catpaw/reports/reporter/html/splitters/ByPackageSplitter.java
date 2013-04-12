package com.dd.test.catpaw.reports.reporter.html.splitters;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;



public class ByPackageSplitter extends CollectionSplitter {

	@Override
	public List<String> getKeys(ITestResult result) {
//		CatPawLogger.getLogger().entering(result);
		List<String> res = new ArrayList<String>();

		Package pack = result.getMethod().getRealClass().getPackage();
		if (pack == null) {
			res.add("default package");
		} else {
			res.add(pack.getName());
		}
//		CatPawLogger.getLogger().exiting(res);
		return res;
	}

}
