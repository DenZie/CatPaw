package com.dd.test.catpaw.reports.reporter.excelreport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;


import com.dd.test.catpaw.platform.asserts.CatPawAsserts;
import com.dd.test.catpaw.platform.asserts.CatPawTestResult;


/**
 * 
 * Contains all Result data related to each testcase
 */
public class TestCaseResult implements Comparable<TestCaseResult>
{
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();
	private ITestResult iTestResult;
	private String sClass_name ;
	private String sMethod_name ;
	private List<String> lstDefectMsg ;
	private List<String> lstError ;
	private String sTest_Desc;
	private List<String> sGroup ;
	private long sDuration_taken;
	private ArrayList<String> sSSMsg;
	private String sSSLink;
	private int iStatus ;
	
	static final String NA = "NA";
	static String sOutputDir;
	
	public TestCaseResult() {
		this.iTestResult =null;
		this.sClass_name = null;
		this.sMethod_name = null;
		this.lstDefectMsg = new ArrayList<String>();
		this.lstError = new ArrayList<String>();
		this.sTest_Desc = null;
		this.sGroup = new ArrayList<String>();
		this.sSSLink = null;
		this.sSSMsg = new ArrayList<String>();
		
	}
	
	public void setITestResultobj(ITestResult iTestResult) {
		// logger.entering(iTestResult);
		this.iTestResult = iTestResult;
		this.sDuration_taken = iTestResult.getEndMillis()
				- iTestResult.getStartMillis();
		this.iStatus = iTestResult.getStatus();
		
		ITestNGMethod singleMethod = iTestResult.getMethod();
		//Generating method name for cases based on DataProvider
		//These would appear as 'metName [param1] [param2] ...
		
		String sMethodName = this.iTestResult.getName();
		Object[] metParamArray = this.iTestResult.getParameters();
		if (metParamArray.length > 0){
				for (int i = 0 ; i < metParamArray.length; i++){
					sMethodName+=" ["+metParamArray[i]+"]";
				}
		}
		this.setMethodName(sMethodName);
		this.setClassName(singleMethod.getTestClass().getName());
		this.setTestDesc(singleMethod.getDescription());
		if (singleMethod.getGroups().length == 0){
			this.getGroup().add(NA);
			this.setGroup(this.getGroup());
		}else
			this.setGroup(Arrays.asList(singleMethod.getGroups()));
		
		this.sSSLink = sOutputDir + "\\html\\" + singleMethod.getId()+".html";
		
		//if failed, then add error details
		if ( iStatus == ITestResult.FAILURE)
		{
			 ArrayList<CatPawTestResult> errorList = new ArrayList<CatPawTestResult>();
			 errorList = (ArrayList<CatPawTestResult>)CatPawAsserts.getverificationFailuresMap().get(iTestResult)  ;
			 
			 if (errorList == null){
				 lstError.add(iTestResult.getThrowable().toString());
				 String defectMsg = TestCaseErrors.getInstance().debugError(iTestResult.getThrowable());
				 lstDefectMsg.add( defectMsg == null? "Assert Failed or Script error"  : defectMsg);
			 }else{
				 for (CatPawTestResult singleResult : errorList){
					 lstError.add(singleResult.getTestresult().toString());
					 String defectMsg = TestCaseErrors.getInstance().debugError(singleResult.getTestresult()); 
					 lstDefectMsg.add( defectMsg == null?  singleResult.getMsg() : defectMsg);
				 }
			 }
		}
		// logger.exiting();
	}
	
	public void setClassName(String sClass_name)
	{
		this.sClass_name = sClass_name;
	}
	
	public void setMethodName(String sMethod_name)
	{
		this.sMethod_name = sMethod_name;
	}

	public void setTestDesc(String sTest_Desc)
	{
		this.sTest_Desc = sTest_Desc;
	}
	
	public void setError(ArrayList<String> sError)
	{
		this.lstError = sError;
	}
	
	public void setDefect(ArrayList<String> sDefect)
	{
		this.lstDefectMsg = sDefect;
	}
	
	public void setGroup(List<String> sGroup)
	{
		this.sGroup = sGroup;
	}
	
	public ITestResult getITestResultobj()
	{
		return this.iTestResult ;
	}
	
	public String getClassName()
	{
		return this.sClass_name;
	}
	
	public String getMethodName()
	{
		return this.sMethod_name;
	}

	public List<String> getDefect()
	{
		return this.lstDefectMsg;
	}
	
	public List<String> getError()
	{
		return this.lstError;
	}
	
	public String getTestDesc()
	{
		return this.sTest_Desc;
	}
	
	public List<String> getGroup()
	{
		return this.sGroup;
	}
	
	public long getDurationTaken()
	{
		return this.sDuration_taken;
	}
	
	public ArrayList<String> getssmsg()
	{
		return this.sSSMsg;
	}
	
	public String getsslink()
	{
		return this.sSSLink;
	}
	
	public int getStatus()
	{
		return this.iStatus;
	}
	
	public int compareTo(TestCaseResult o) {
		
		if(this.getClassName().compareTo(o.getClassName()) == 0){
			return this.getMethodName().compareTo(o.getMethodName());
		}else
			return this.getClassName().compareTo(o.getClassName());
	}
}