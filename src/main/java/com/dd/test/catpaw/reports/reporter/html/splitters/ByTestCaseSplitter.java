package com.dd.test.catpaw.reports.reporter.html.splitters;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;



/**
 * This class is responsible by the Velocity engine to render the "By Test case view" 
 *  
 */
public class ByTestCaseSplitter extends CollectionSplitter {

	@Override
	public List<String> getKeys(ITestResult result) {
//		CatPawLogger.getLogger().entering(result);

		String eCaseID = null;
		if (result.getAttribute("eCaseID") != null)
			eCaseID = result.getAttribute("eCaseID").toString();
		List<String> res = new ArrayList<String>();
		// We are to add a testcase to our ArrayList of Strings
		// only if it has an associated eCase ID to it.
		if (eCaseID != null && (!eCaseID.trim().isEmpty())) {			
			res.add(eCaseID);
		}
//		CatPawLogger.getLogger().exiting(res);
		return res;
	}
}
