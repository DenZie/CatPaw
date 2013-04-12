package com.dd.test.catpaw.platform.asserts;

import java.util.Arrays;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

import com.dd.test.catpaw.platform.config.ServiceLoaderManager;

/**
 * SoftAssertCapabilities
 * 
 * Has logic to handle soft assertions along with hard asserts and report the results.
 */

public class SoftAssertsListener implements IInvokedMethodListener{


	/**
	 * This includes user specified names for each of the soft assertions and successful tests with verification
	 * failures are turned into failing test.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IInvokedMethodListener#afterInvocation(org.testng.IInvokedMethod, org.testng.ITestResult)
	 */
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
//		// logger.entering(new Object[]{method, testResult});
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
	//		// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}

		logSoftAssertsPassed(testResult);
		logSoftAssertsFailures(testResult);
//		// logger.exiting();
	}

	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		//Below check needs to be implemented for any new Listener adoption by this class.
		//Failing to do so can cause unpredictable results within the framework.
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
	//		// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}
		return;
	}

	private void logSoftAssertsFailures(ITestResult testResult) {
//		// logger.entering(testResult);
		CatPawTestResult temporaryTestResult = null;
		Throwable eachFailure = null, lastFailure = null;
		Throwable consolidatedFailures = null;
		String msg = null;
		StringBuffer failureMessage = null;
		String fullStackTrace = null;
		Reporter.setCurrentTestResult(testResult);
		List<CatPawTestResult> verificationFailures = (List<CatPawTestResult>) CatPawAsserts
				.getVerificationFailures();

		// if there are verification failures...
		if (verificationFailures.size() > 0) {
			// set the test to failed
			testResult.setStatus(ITestResult.FAILURE);
			if (!(Arrays.asList(testResult.getThrowable().getStackTrace()).equals(Arrays.asList(verificationFailures
					.get(verificationFailures.size() - 1).getTestresult().getStackTrace())))) {
				verificationFailures.add(new CatPawTestResult(testResult.getThrowable(), testResult.getThrowable()
						.toString()));

			}
			// finding the total number of failures that were encountered
			int size = verificationFailures.size();

			// Here the case of having just one failure is being handled
			if (size == 1) {
				temporaryTestResult = verificationFailures.get(0);
				eachFailure = temporaryTestResult.getTestresult();
				msg = temporaryTestResult.getMsg();
				failureMessage = new StringBuffer();
				failureMessage.append("1 FAILURE IN THE TEST CASE\n\n");
				failureMessage.append("  *****").append(msg).append("*****  ");
				consolidatedFailures = new Throwable(failureMessage.toString());
				consolidatedFailures.setStackTrace(eachFailure.getStackTrace());
				testResult.setThrowable(consolidatedFailures);
			} else {
				// In case of more than one failures
				// create a failure message with all failures and stack traces
				// (except last failure)
				failureMessage = new StringBuffer(size + " FAILURES IN THE TEST CASE").append(":\n\n");
				for (int i = 0; i < size - 1; i++) {
					failureMessage.append("Failure ").append(i + 1).append(" of ").append(size).append(":\n");
					temporaryTestResult = verificationFailures.get(i);
					eachFailure = temporaryTestResult.getTestresult();
					msg = temporaryTestResult.getMsg();
					fullStackTrace = Utils.stackTrace(eachFailure, false)[1];
					failureMessage.append("  *****").append(msg).append("*****  ");
					failureMessage.append(fullStackTrace).append("\n\n");
				}
				// This logic helps to check if a failure was an assertion
				// failure.
				// Assertion failures are to be handled separately to avoid
				// ClassCastExceptions,
				// since the custom class CatPawTestResult created cannot be
				// cast to a Throwable type.
				String classtype = verificationFailures.get(size - 1).getClass().getSimpleName();
				// Check to see if the verification failure was of type
				// AssertionError
				// for verify assertions, the class type would always be
				// CatPawTestResult
				if (classtype.equalsIgnoreCase("AssertionError")) {
					eachFailure = (verificationFailures.get(size - 1)).getTestresult();
					fullStackTrace = Utils.stackTrace(eachFailure, false)[1];
					failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":\n");
					failureMessage.append(fullStackTrace);
					consolidatedFailures = new Throwable(failureMessage.toString());
					consolidatedFailures.setStackTrace(eachFailure.getStackTrace());
					testResult.setThrowable(consolidatedFailures);
				} else {
					// The logic to handle the nth verification failure resides
					// here.
					temporaryTestResult = (verificationFailures.get(size - 1));
					lastFailure = temporaryTestResult.getTestresult();
					msg = temporaryTestResult.getMsg();
					failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":\n");
					failureMessage.append("  *****").append(msg).append("*****  ");
					failureMessage.append(lastFailure.toString());

					// Consolidating all failures into one failure and throwing
					// it back to the testResult
					consolidatedFailures = new Throwable(failureMessage.toString());
					consolidatedFailures.setStackTrace(lastFailure.getStackTrace());
					testResult.setThrowable(consolidatedFailures);
				}

			}
		}
//		// logger.exiting();
	}

	private void logSoftAssertsPassed(ITestResult testResult) {
//		// logger.entering(testResult);
		CatPawTestResult temporaryTestResult = null;
		String msg = null, methodName = testResult.getMethod().getMethodName();
		Reporter.setCurrentTestResult(testResult);
		List<CatPawTestResult> verificationPassed = (List<CatPawTestResult>) CatPawAsserts.getVerificationPassed();
		int size = verificationPassed.size();

		// Here the case of having just one pass is being handled
		if (size == 1) {
			temporaryTestResult = verificationPassed.get(0);
			msg = temporaryTestResult.getMsg();
			String eachPassMsg = "1 PASSED IN THE TEST CASE: " + methodName + "\n";
			Reporter.log(eachPassMsg + " *****" + msg + "***** ", true);

		} else {
			// In case of more than one pass create a passed message with all
			// passes (except last pass)

			if (size > 1) {
				String successMessage = size + " PASSED IN THE TEST CASE:" + methodName + "\n";
				Reporter.log(successMessage, true);
			}

			for (int i = 0; i < size - 1; i++) {
				String eachPassMsg = "Passed " + (i + 1) + " of " + size + ":";
				temporaryTestResult = verificationPassed.get(i);
				msg = temporaryTestResult.getMsg();
				Reporter.log(eachPassMsg + "  *****" + msg + "*****  ", true);
			}
			// The logic to handle the nth verification pass resides here.
			if (size > 0) {

				temporaryTestResult = (verificationPassed.get(size - 1));
				msg = temporaryTestResult.getMsg();
				Reporter.log("Passed " + size + " of " + size + ":" + "  *****" + msg + "***** \n", true);

			}
		}
//		// logger.exiting();
	}

}
