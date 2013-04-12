package com.dd.test.catpaw.reports.reporter.html.splitters;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;



public class ByGroupSplitter extends CollectionSplitter {

	List<String> groups;

	public ByGroupSplitter(List<String> groups) {
		this.groups = groups;
	}

	@Override
	public List<String> getKeys(ITestResult result) {
//		CatPawLogger.getLogger().entering(result);
		List<String> res = new ArrayList<String>();

		String[] resultGroups = result.getMethod().getGroups();

		for (String group : resultGroups){
			res.add(group);
		}
		if (res.size() == 0) {
			res.add("misc");
		}
//		CatPawLogger.getLogger().exiting(res);
		return res;
	}

}
