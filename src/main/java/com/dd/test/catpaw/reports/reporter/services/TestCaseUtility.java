package com.dd.test.catpaw.reports.reporter.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IResultMap;
import org.testng.ITestResult;
import org.testng.annotations.Test;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;


public class TestCaseUtility {
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();
	
	public static void appendECaseId(IInvokedMethod method, ITestResult testResult) {
		// logger.entering(new Object[]{method, testResult});
	    if (! CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.UPDATE_EZTRACKER)) {
	//    	// logger.exiting();
	        return;
	    }
		if (! method.isTestMethod()) {
			// logger.exiting();
		    return;
		}
        Test testMethod = method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
        String eCaseID = null;
        if (testMethod != null) {
            eCaseID = testMethod.testName();
        }
        // If methodName is not null, it means a Test Method was invoked as
        // @Test(testName="eCaseID")
        if (eCaseID != null && eCaseID.length() > 1) {
            appendAttribute(testResult, eCaseID);
//    //        // logger.exiting();
            return;
        }
        // check if the test method uses CatPaw framework comprehensible
        // data driven approach
        if (isCatPawDataDriven(testResult)) {
            appendAttribute(testResult, null);
//    //        // logger.exiting();
            return;
        }
        //If we reached here, it means the user wanted to update eztracker but we couldn't find the
        // ecase id in any way at all. So lets warn the user.
        StringBuffer errMsg = new StringBuffer("To update EZTracker either add 'testName' attribute to @Test annotation (or)\n");
        errMsg.append("Have your @Test method's data parameter class implement IECase interface (or)\n");
        errMsg.append("Have your test class that is instantiated by your @Factory method implement IECase interface.");
//        logger.warning(errMsg.toString());
//        // logger.exiting();
	}

	/**
	 * This method helps set eCase ID attribute for CatPaw framework comprehensible Data Driven approach test cases or
	 * for tests which are annotated as Test Methods and which pass the eCase ID as a parameter to the attribute
	 * "testName"
	 * 
	 * @param result
	 *            - A result object of type {@link ITestResult}
	 * @param attribute
	 *            - The eCase ID as a string that is to be set as an attribute to the result object
	 */
    private static void appendAttribute(ITestResult result, String attribute) {
//    	// logger.entering(new Object[] { result, attribute });
        if (attribute == null) {
            Object[] parameters = result.getParameters();
            // First check if this is a regular data driven
            if (parameters.length > 0 && (IECase.class.isAssignableFrom(parameters[0].getClass()))) {
                IECase eCaseID1 = (IECase) result.getParameters()[0];
                attribute = eCaseID1.geteCase();
                result.setAttribute("eCaseID", attribute);
    //    //        // logger.exiting();
                return;
            }
            // If we are here, then it means it is a data driven test but
            // factories may be involved. So we need to check if the test class
            // implements IECase interface
            Object testClassInstance = result.getMethod().getInstance();
            if (testClassInstance instanceof IECase) {
                attribute = ((IECase) testClassInstance).geteCase();
            }
        }
        result.setAttribute("eCaseID", attribute);
//        // logger.exiting();
    }

	/**
	 * This method essentially helps the CatPaw Framework identify if a particular method that makes use of
	 * DataProvider is intending to use CatPaw Framework comprehensible Data Driven Approach or not.
	 * 
	 * @param result
	 *            - A result object of type {@link ITestResult}
	 * @return - A boolean that indicates of the result represents a method that is using CatPaw Framework defined Data
	 *         Driven approach or not.
	 */
	private static boolean isCatPawDataDriven(final ITestResult result) {
		// logger.entering(result);
		boolean isDataDriven = false;
		if (result.getParameters().length == 1) {
			if (IECase.class.isAssignableFrom(result.getParameters()[0].getClass())) {
				// logger.exiting(true);
		        return true;
			}
		}
		//Assuming it wasn't a regular data driven, lets check if Factories were used.
		Object testClassInstance = result.getMethod().getInstance();
		if (testClassInstance instanceof IECase){
		    isDataDriven = true;
		}
		// logger.exiting(isDataDriven);
		return isDataDriven;
	}

	/**
	 * This method iterates through a {@link IResultMap} and returns a list of {@link ITestResult}
	 * 
	 * @param results
	 *            - an object of type {@link IResultMap}
	 * @return - a list of test results of type {@link ITestResult}
	 */
	public static List<ITestResult> fetchTests(IResultMap results) {
		// logger.entering(results);
		List<ITestResult> consolidatedResults = new ArrayList<ITestResult>();
		Iterator<ITestResult> i = results.getAllResults().iterator();
		while (i.hasNext()){
			consolidatedResults.add(i.next());
		}
		// logger.exiting(consolidatedResults);
		return consolidatedResults;
	}

}
