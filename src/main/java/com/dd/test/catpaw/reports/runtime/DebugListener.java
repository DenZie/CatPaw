package com.dd.test.catpaw.reports.runtime;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;

public class DebugListener implements ITestListener {


	public void onFinish(ITestContext arg0) {
		return;
	}


	public void onStart(ITestContext arg0) {
		Reporter.log("hub = " + CatPawConfig.getConfigProperty(CatPawConfigProperty.SELENIUM_HOST),true);
		Reporter.log("port = " + CatPawConfig.getIntConfigProperty(CatPawConfigProperty.SELENIUM_PORT),true);
	}


	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		return;
	}


	public void onTestFailure(ITestResult arg0) {
		
		Reporter.log(arg0.getTestClass().getName() + "." + arg0.getMethod().getMethodName() + " failed",true);
		arg0.getThrowable().printStackTrace();

	}


	public void onTestSkipped(ITestResult arg0) {
		return;
	}


	public void onTestStart(ITestResult arg0) {
		Reporter.log("about to start test " + arg0.getTestClass().getName() + "." + arg0.getMethod().getMethodName(),true);

	}


	public void onTestSuccess(ITestResult arg0) {
		Reporter.log(arg0.getTestClass().getName() + "." + arg0.getMethod().getMethodName() + " passed",true);
	}

}
