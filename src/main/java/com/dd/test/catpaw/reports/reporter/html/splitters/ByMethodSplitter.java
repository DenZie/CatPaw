package com.dd.test.catpaw.reports.reporter.html.splitters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;



public class ByMethodSplitter extends CollectionSplitter {

	private Map<String, ITestNGMethod> methodByName = new HashMap<String, ITestNGMethod>();

	public ITestNGMethod getAssociatedMethod(String key) {
		return methodByName.get(key);
	}

	@Override
	public List<String> getKeys(ITestResult result) {
//		CatPawLogger.getLogger().entering(result);
		String name = result.getMethod().getRealClass().getName() + "."
				+ result.getMethod().getMethodName();
		methodByName.put(name, result.getMethod());
		List<String> res = new ArrayList<String>();
		res.add(name);
//		CatPawLogger.getLogger().exiting(res);
		return res;
	}

}
