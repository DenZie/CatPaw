package com.dd.test.catpaw.platform.asserts;

/**
 * 
 * Custom class for the Testresult with the error message i.e testresult and the message given in the verification.
 * 
 * 
 */

public class CatPawTestResult {
	private Throwable testResult;
	private String customMessage;

	public CatPawTestResult() {
		super();
		testResult = null;
		customMessage = null;
	}

	public CatPawTestResult(Throwable testresult, String msg) {
		this.testResult = testresult;
		this.customMessage = msg;
	}

	public Throwable getTestresult() {
		return testResult;
	}

	public void setTestresult(Throwable testresult) {
		this.testResult = testresult;
	}

	public String getMsg() {
		return customMessage;
	}

	public void setMsg(String msg) {
		this.customMessage = msg;
	}

}
