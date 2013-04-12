package com.dd.test.catpaw.platform.asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;


/**
 * 
 * This is the Asserts base class for CatPaw, that imports TestNG results and overrides the assert methods so that test
 * classes extending this class are decoupled from TestNG.
 * 
 */
public class CatPawAsserts {

	private static Map<ITestResult, List<CatPawTestResult>> verificationFailuresMap = new HashMap<ITestResult, List<CatPawTestResult>>();

	private static Map<ITestResult, List<CatPawTestResult>> verificationPassedMap = new HashMap<ITestResult, List<CatPawTestResult>>();

	private static String defaultAssertMsg = "Assert passed.";
	

	/**
	 * assertTrue method is used to assert the condition based on boolean input and provide the Pass result for a TRUE
	 * value. assertTrue will Fail for a FALSE value and abort the test case. * <br>
	 * Sample Usage<br>
	 * <code>
	 * CatPawAsserts.assertTrue(true);
	 * </code>
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail
	 */
	public static void assertTrue(boolean condition) {
		Assert.assertTrue(condition);
		addVerificationPassed(defaultAssertMsg);
	}

	/**
	 * assertFalse method is used to assert the condition based on boolean input and provide the Pass result for a FALSE
	 * value.assertFalse will fail for a TRUE value and abort the test case
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail * <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.assertFalse(false);
	 * </code>
	 */
	public static void assertFalse(boolean condition) {
		Assert.assertFalse(condition);
		addVerificationPassed(defaultAssertMsg);
	}

	/**
	 * assertEquals method is used to assert based on actual and expected values and provide a Pass result for a same
	 * match.assertEquals will yield a Fail result for a mismatch and abort the test case.
	 * 
	 * @param actual
	 *            - Actual value obtained from executing a test
	 * @param expected
	 *            - Expected value for the test to pass. <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.assertEquals("OK","OK");
	 * </code>
	 */
	public static void assertEquals(Object actual, Object expected) {
		Assert.assertEquals(actual, expected);
		addVerificationPassed("Actual:" + actual.toString() + " matches Expected:" + expected.toString());
	}

	/**
	 * verifyTrue method is used to assert the condition based on boolean input and provide the Pass result for a TRUE
	 * value.verifyTrue will Fail for a FALSE value and continue to run the test case. <br>
	 * Sample Usage<br>
	 * <code>
	 * CatPawAsserts.verifyTrue(true,"Some Message");
	 * </code>
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail
	 * @param msg
	 *            - A descriptive text narrating a validation being done
	 * 
	 */
	public static void verifyTrue(boolean condition, String msg) {
		try {
			Assert.assertTrue(condition);
			addVerificationPassed(msg);
		} catch (Throwable e) {
			addVerificationFailure(e, msg);
		}
	}

	/**
	 * verifyTrue method is used to assert the condition based on boolean input and provide the Pass result for a TRUE
	 * value.verifyTrue will Fail for a FALSE value and continue to run the test case. <br>
	 * Sample Usage<br>
	 * <code>
	 * CatPawAsserts.verifyTrue(true);
	 * </code>
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail
	 */
	public static void verifyTrue(boolean condition) {
		try {
			Assert.assertTrue(condition);
			addVerificationPassed(defaultAssertMsg);
		} catch (Throwable e) {
			addVerificationFailure(e, "");
		}
	}

	/**
	 * verifyFalse method is used to assert the condition based on boolean input and provide the Pass result for a FALSE
	 * value.verifyFalse will Fail for a TRUE value and continue to run the test case. <br>
	 * Sample Usage<br>
	 * <code>
	 * CatPawAsserts.verifyFalse(false,"Some Message");
	 * </code>
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail
	 * @param msg
	 *            - A descriptive text narrating a validation being done
	 * 
	 */
	public static void verifyFalse(boolean condition, String msg) {
		try {
			Assert.assertFalse(condition);
			addVerificationPassed(msg);
		} catch (Throwable e) {
			addVerificationFailure(e, msg);
		}
	}

	/**
	 * verifyFalse method is used to assert the condition based on boolean input and provide the Pass result for a FALSE
	 * value.verifyFalse will Fail for a TRUE value and continue to run the test case. <br>
	 * Sample Usage<br>
	 * <code>
	 * CatPawAsserts.verifyFalse(false);
	 * </code>
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail
	 */
	public static void verifyFalse(boolean condition) {
		try {
			Assert.assertFalse(condition);
			addVerificationPassed(defaultAssertMsg);
		} catch (Throwable e) {
			addVerificationFailure(e, "");
		}
	}

	/**
	 * verifyEquals method is used to assert based on actual and expected values and provide a Pass result for a same
	 * match.verifyEquals will yield a Fail result for a mismatch and continue to run the test case.
	 * 
	 * @param actual
	 *            - Actual value obtained from executing a test
	 * @param expected
	 *            - Expected value for the test to pass.
	 * @param msg
	 *            - A descriptive text narrating a validation being done. <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.verifyEquals("OK","OK" ,"Some Message");
	 * </code>
	 */
	public static void verifyEquals(Object actual, Object expected, String msg) {
		try {
			Assert.assertEquals(actual, expected);
			addVerificationPassed(msg);
		} catch (Throwable e) {
			addVerificationFailure(e, msg);
		}
	}

	/**
	 * verifyEquals method is used to assert based on actual and expected values and provide a Pass result for a same
	 * match.verifyEquals will yield a Fail result for a mismatch and continue to run the test case.
	 * 
	 * @param actual
	 *            - Actual value obtained from executing a test
	 * @param expected
	 *            - Expected value for the test to pass. <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.verifyEquals("OK","OK");
	 * </code>
	 */
	public static void verifyEquals(Object actual, Object expected) {
		try {
			Assert.assertEquals(actual, expected);
			addVerificationPassed("Actual:" + actual.toString() + " matches Expected:" + expected.toString());
		} catch (Throwable e) {
			addVerificationFailure(e, "");
		}
	}

	/**
	 * assertTrue method is used to assert the condition based on boolean input and provide the Pass result for a TRUE
	 * value. assertTrue will Fail for a FALSE value and abort the test case. * <br>
	 * Sample Usage<br>
	 * <code>
	 * CatPawAsserts.assertTrue(true, "Some Message");
	 * </code>
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail
	 * @param message
	 *            - A descriptive text narrating a validation being done.
	 */
	public static void assertTrue(boolean condition, String message) {
		Assert.assertTrue(condition, message);
		addVerificationPassed(message);
	}

	/**
	 * assertFalse method is used to assert the condition based on boolean input and provide the Pass result for a FALSE
	 * value.assertFalse will fail for a TRUE value and abort the test case
	 * 
	 * @param condition
	 *            - A test condition to be validated for pass/fail
	 * @param message
	 *            - A descriptive text narrating a validation being done. <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.assertFalse(false,"Some Message");
	 * </code>
	 */
	public static void assertFalse(boolean condition, String message) {
		Assert.assertFalse(condition, message);
		addVerificationPassed(message);
	}

	/**
	 * assertEquals method is used to assert based on actual and expected values and provide a Pass result for a same
	 * boolean.assertEquals will yield a Fail result for a mismatch and abort the test case.
	 * 
	 * @param actual
	 *            - Actual boolean value obtained from executing a test
	 * @param expected
	 *            - Expected boolean value for the test to pass. <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.assertEquals(true,true);
	 * </code>
	 * 
	 */
	public static void assertEquals(boolean actual, boolean expected) {
		Assert.assertEquals(actual, expected);
		addVerificationPassed("Actual:" + actual + " matches Expected:" + expected);
	}

	/**
	 * assertEquals method is used to assert based on actual and expected values and provide a Pass result for a same
	 * match.assertEquals will yield a Fail result for a mismatch and abort the test case.
	 * 
	 * @param actual
	 *            - Actual value obtained from executing a test
	 * @param expected
	 *            - Expected value for the test to pass. <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.assertEquals("OK","OK");
	 * </code>
	 */
	public static void assertEquals(Object[] actual, Object[] expected) {
		Assert.assertEquals(actual, expected);
		addVerificationPassed("Actual:" + actual.toString() + " matches Expected:" + expected.toString());
	}

	/**
	 * assertEquals method is used to assert based on actual and expected values and provide a Pass result for a same
	 * match.assertEquals will yield a Fail result for a mismatch and abort the test case.
	 * 
	 * @param actual
	 *            - Actual value obtained from executing a test
	 * @param expected
	 *            - Expected value for the test to pass.
	 * @param message
	 *            - A descriptive text narrating a validation being done. <br>
	 *            Sample Usage<br>
	 *            <code>
	 * CatPawAsserts.assertEquals("OK","OK", "Some Message");
	 * </code>
	 * 
	 */
	public static void assertEquals(Object actual, Object expected, String message) {
		Assert.assertEquals(actual, expected, message);
		addVerificationPassed(message);
	}

	/**
	 * Fail method fails a flow. * <br>
	 * Sample Usage<br>
	 * <code>
	 * CatPawAsserts.fail("Some Message");
	 * </code>
	 * 
	 * @param message
	 *            -- A descriptive text narrating a validation being done.
	 */
	public static void fail(String message) {
		Assert.fail(message);
	}

	public static void fail(Throwable e, String message) {
		addVerificationFailure(e, message);
	}

	/**
	 * Use this method to get the list of failures after a test is run
	 * 
	 * 
	 * @return - returns a List of type {@link CatPawTestResult}
	 */
	public synchronized static List<CatPawTestResult> getVerificationFailures() {
	    List<CatPawTestResult> verificationFailures = null;
	   
	    verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
	  
		verificationFailures =  (verificationFailures == null) ? new ArrayList<CatPawTestResult>() : verificationFailures;
		return verificationFailures;
	}

	private synchronized static void addVerificationFailure(Throwable e, String msg) {
		List<CatPawTestResult> verificationFailures = (List<CatPawTestResult>) getVerificationFailures();
		
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
	
		// Setting to let testng know there was a failure
		ITestResult result = Reporter.getCurrentTestResult(); 
		result.setThrowable(e);
		verificationFailures.add(new CatPawTestResult(e, msg));
	}

	public synchronized static List<CatPawTestResult> getVerificationPassed() {
	    List<CatPawTestResult> verificationPassed= null;
	   
	        verificationPassed = verificationPassedMap.get(Reporter.getCurrentTestResult());
	    
		verificationPassed =  (verificationPassed == null) ? new ArrayList<CatPawTestResult>() : verificationPassed;
		return verificationPassed;
	}

	private synchronized static void addVerificationPassed(String msg) {
		List<CatPawTestResult> verificationPassed = (List<CatPawTestResult>) getVerificationPassed();
		
		 verificationPassedMap.put(Reporter.getCurrentTestResult(), verificationPassed);
		
		verificationPassed.add(new CatPawTestResult(null, msg));
	}

	/**
	 * Use this method to get the map of tests to test failures due to verify failures after a test is run
	 * 
	 * 
	 * @return - returns a map of tests to the verification failrues of type {@link CatPawTestResult}
	 */
	public synchronized static Map<ITestResult, List<CatPawTestResult>> getverificationFailuresMap() {
		return verificationFailuresMap;
	}
}
