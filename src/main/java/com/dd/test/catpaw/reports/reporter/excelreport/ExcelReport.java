package com.dd.test.catpaw.reports.reporter.excelreport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.reports.reporter.html.ReportDataGenerator;


/**
 * 
 * <br>Add this class as listener to generate an ExcelReport
 *  for a suite run after the SoftAssertCapabilities file. </br>
 * Implements the IReporter interface to fetch data from TestNG.
 * Depends on BFReporter to generate links based data
 */
@SuppressWarnings("unchecked")
public class ExcelReport implements IReporter {
//    final static SimpleLogger logger = CatPawLogger.getLogger();
	
	private HSSFWorkbook wb;
	
	private List<SummarizedData> lSuites = new ArrayList<SummarizedData>();
	private List<SummarizedData> lTests = new ArrayList<SummarizedData>();
	private List<SummarizedData> lGroups = new ArrayList<SummarizedData>();
	private List<SummarizedData> lClasses = new ArrayList<SummarizedData>();
	private ArrayList<TestCaseResult> allTestsResults = new ArrayList<TestCaseResult>();
	
	private List<List<String>> tcFailedData = new ArrayList<List<String>>();
	private List<List<String>> tcPassedData = new ArrayList<List<String>>();
	private List<List<String>> tcSkippedData = new ArrayList<List<String>>();
	private List<List<String>> tcDefectData = new ArrayList<List<String>>();
	
	private Map<String, SummarizedData> mpGroupClassData = new HashMap<String, SummarizedData>();
	
	private static List<ReportMap<?>> fullReportMap = new ArrayList<ReportMap<?>>();
	
	/**
	 * The first method that gets called when generating the report. Generates
	 * data in way the Excel should appear.  Creates the Excel Report and writes it to a file.
	 */
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String sOpDirectory) {
		// logger.entering(new Object[]{xmlSuites, suites,sOpDirectory });
//		if (logger.isLoggable(Level.INFO)){
//			// logger.log(Level.INFO, "Generating ExcelReport");
//		}
		
		ReportDataGenerator.initReportData(suites);
		TestCaseResult.sOutputDir = sOpDirectory;
		//Generate data to suit excel report.
		this.generateSummaryData(suites);
		this.generateTCBasedData(allTestsResults);
		
		//Create the Excel Report
		this.createExcelReport();
		
		//Render the report
		String outputFile = sOpDirectory + "/Excel_Report.xls";
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(new File(outputFile));
			wb.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			// logger.log(Level.SEVERE, e.getMessage(), e);
		}
		catch (IOException e) {
			// logger.log(Level.SEVERE, e.getMessage(), e);
		}
//		if (logger.isLoggable(Level.INFO)){
//			// logger.log(Level.INFO,"Excel File Created @ "	+ sOpDirectory);
//		}
	}

	/**
	 * Initialized styles used in the workbook.  Generates the report related info.
	 * Creates the structure of the Excel Reports
	 */
	private void createExcelReport() {
		// logger.entering();

		wb = new HSSFWorkbook();
		Styles.initStyles(wb);
		
		//Report Details
		this.createReportInfo();

		//Map of sheet names - individual reports  and corresponding data
		this.createReportMap();
		
		//Render reports in the Workbook
		for (ReportMap rm : fullReportMap){
			List<BaseReport<?>> allReports = rm.getGeneratedReport();
			allReports.iterator().next().generateRep(this.wb, rm.getName(), rm.getGeneratedReport());
		}
		
		// logger.exiting();
	}
	
	/**
	 * Create Run details like owner of run, time and stage used.
	 */
	private void createReportInfo(){
		// logger.entering();
		
		HSSFSheet SummarySheet = wb.createSheet(ReportSheetNames.TESTSUMMARYREPORT.getName());
		
		Map<String, String> reportInfo = new LinkedHashMap<String, String>();

		Format formatter = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
		String currentDate = formatter.format(new Date());
		String sReleaseCycle = CatPawConfig.getConfigProperty(CatPawConfigProperty.RELEASE_CYCLE);
		String sPushName = CatPawConfig.getConfigProperty(CatPawConfigProperty.PUSH_NAME);
		
		reportInfo.put("Report generated on :", currentDate);
		reportInfo.put("Report Owner : ", System.getProperty("user.name"));
		reportInfo.put("Stage Used : ", CatPawConfig.getConfigProperty(CatPawConfigProperty.STAGE_NAME));
		reportInfo.put("Browser Used : ", ReportDataGenerator.browser);
		reportInfo.put("Release : ", ReportDataGenerator.releaseVersion);
		reportInfo.put("Build Id : ", ReportDataGenerator.buildId.toString());
		reportInfo.put("Stream : ", ReportDataGenerator.streamName);
		reportInfo.put("DB Version : ", ReportDataGenerator.dbVersion);
		reportInfo.put("Release Cycle : ", (sReleaseCycle.equals("Unknown")?"Not Specified":sReleaseCycle));
		reportInfo.put("Push Name : ", (sPushName.equals("Push")?"Not Specified":sPushName));
		
		
		int rowNum = 0, colNum = 0;
		HSSFCell col;
		HSSFRow row;
		
		for (String sDesc : reportInfo.keySet()){
			colNum = 2;
			row = SummarySheet.createRow(rowNum++);
			
			col = row.createCell(colNum);
			col.setCellStyle(Styles.subHeading2Style);
			col.setCellValue(sDesc);
			
			//Next column holds the values
			col = row.createCell(++colNum);
			col.setCellStyle(Styles.thinBorderStyle);
			col.setCellValue(reportInfo.get(sDesc));
			
		}
		// logger.exiting();
	}
	
	/**
	 * Creates all the report details like which sheet should contain which report and the data
	 * associated with the report 
	 */
	private void createReportMap(){
		// logger.entering();
		
		//Summary Report
		Map<String,List<SummarizedData>> subReportMap = new LinkedHashMap<String, List<SummarizedData>>();
		subReportMap.put("Full Suite Summary", lSuites);
		subReportMap.put("Test Summary", lTests);
		subReportMap.put("Classwise Summary", lClasses);
		subReportMap.put("Groupwise Summary", lGroups);
		
		ReportMap<SummarizedData> TestSummaryReport = new ReportMap<SummarizedData>(ReportSheetNames.TESTSUMMARYREPORT.getName(), subReportMap, 0);
		fullReportMap.add(TestSummaryReport);

		//Group Detailed Report
		List<SummarizedData> groupsClone = new ArrayList<SummarizedData>(lGroups);
		List<SummarizedData> classData;
		
		SummarizedData NAGroupData = new SummarizedData(); 
		NAGroupData.setsName(TestCaseResult.NA); 
		groupsClone.add(NAGroupData);
		subReportMap = new LinkedHashMap<String, List<SummarizedData>>();
		for (SummarizedData group : groupsClone){
			
			String sGroupName = group.getsName();
			classData = new ArrayList<SummarizedData>() ;
			for (String sGroupClassName : mpGroupClassData.keySet()){
				if (sGroupClassName.substring(0, sGroupName.length()).equals(sGroupName))
					classData.add(mpGroupClassData.get(sGroupClassName));
			}
			subReportMap.put(sGroupName, classData);
		}

		ReportMap<SummarizedData> secondReport = new ReportMap<SummarizedData>(ReportSheetNames.GROUPSUMMARYREPORT.getName(), subReportMap, 0);
		fullReportMap.add(secondReport);
		
		//TestCase Status Report
		Map<String, List<List<String>>> subDetailReportMap = new LinkedHashMap<String, List<List<String>>>();
		subDetailReportMap.put("Passed TC List", tcPassedData);
		subDetailReportMap.put("Failed TC List", tcFailedData);
		subDetailReportMap.put("Skipped TC List", tcSkippedData);
		
		ReportMap<List<String>> thirdReport = new ReportMap<List<String>>(ReportSheetNames.TESTCASEREPORT.getName(), subDetailReportMap, 1);
		fullReportMap.add(thirdReport);
		
		//Defect Report
		Map<String, List<List<String>>> lstDefectReports = new LinkedHashMap<String, List<List<String>>>();
		lstDefectReports.put("Defect Summary", tcDefectData);
		ReportMap<List<String>> fourthReport = new ReportMap<List<String>>(ReportSheetNames.DEFECTREPORT.getName(), lstDefectReports, 1);
		fullReportMap.add(fourthReport);
		
		//Changing the titles of the Defect Report
		BaseReport<List<String>> bR = (BaseReport<List<String>>)fullReportMap.get(fullReportMap.size()-1).getGeneratedReport().iterator().next();
		List<String> lsTitles =  Arrays.asList(new String[]{"Class Name","Method/Testcase id","Test Description","Group[s]", "Time taken", "Link", "Error Message", "Error Details"});
		bR.setColTitles(lsTitles);
		// logger.exiting();
	}
	
	/**
	 * Generates all summarized counts for various reports
	 * @param suites
	 */
	private void generateSummaryData(List<ISuite> suites){
		// logger.entering(suites);

		SummarizedData tempSuite = null ;
		SummarizedData tempTest ;
		SummarizedData tempGroups = null;
		this.generateTestCaseResultData(suites);
		
		//Generating Group Summary data
		for (ISuite suite : suites){

			tempSuite = new SummarizedData();
			tempSuite.setsName(suite.getName());
			Map<String, ISuiteResult> allResults = suite.getResults();
			Map<String, Collection<ITestNGMethod>> tempGroupMethodMap = suite.getMethodsByGroups();
			for (String sGroupName : tempGroupMethodMap.keySet()){
				
				tempGroups = new SummarizedData();
				tempGroups.setsName(sGroupName);
				tempGroups.incrementiTotal(tempGroupMethodMap.get(sGroupName).size());
				
				for (TestCaseResult tr : allTestsResults){
					if(tr.getGroup().contains(sGroupName)){
						tempGroups.incrementCount(tr.getStatus());
						tempGroups.incrementDuration(tr.getDurationTaken());
					}
				}
				tempGroups.setiTotal(tempGroups.getiPassedCount()+tempGroups.getiFailedCount()+
						tempGroups.getiSkippedCount());
				lGroups.add(tempGroups);
			}
				
			//Generating Test summary data
			for (ISuiteResult testResult : allResults.values()){
				ITestContext testContext = testResult.getTestContext(); 
				tempTest = new SummarizedData();
				tempTest.setsName(testContext.getName());
				tempTest.setiFailedCount(testContext.getFailedTests().size());
				tempTest.setiPassedCount(testContext.getPassedTests().size());
				tempTest.setiSkippedCount(testContext.getSkippedTests().size());
				tempTest.setiTotal(tempTest.getiPassedCount()+tempTest.getiFailedCount()+tempTest.getiSkippedCount());
				tempTest.setlRuntime(testContext.getEndDate().getTime() - testContext.getStartDate().getTime());
				
				lTests.add(tempTest);
			}


			//Generating Suite Summary data
			for (SummarizedData test : lTests){
				
				tempSuite.setiPassedCount(test.getiPassedCount() + tempSuite.getiPassedCount());
				tempSuite.setiFailedCount(test.getiFailedCount() + tempSuite.getiFailedCount());
				tempSuite.setiSkippedCount(tempSuite.getiSkippedCount() + test.getiSkippedCount());
				tempSuite.setiTotal(tempSuite.getiPassedCount()+tempSuite.getiFailedCount()+tempSuite.getiSkippedCount());
				tempSuite.setlRuntime(test.getlRuntime()+tempSuite.getlRuntime());
			}
			lSuites.add(tempSuite);
			
			
		}	
			
		Collections.sort(lGroups);
		Collections.sort(lTests);
		// logger.exiting();
	}	
	

	/**
	 * Method to generate array of all results of all testcases that were run in a suite
	 * Output : Populates the allTestsResults arraylist with results and info for all test methods.
	 */
	private void generateTestCaseResultData(List<ISuite> suites){
		// logger.entering();
		for (ISuite suite : suites){
			
			Map<String, ISuiteResult> allResults = suite.getResults();
			
			for (ISuiteResult testResult : allResults.values()){
				
				ITestContext testContext = testResult.getTestContext();
				
				IResultMap passedResultMap = testContext.getPassedTests();
				IResultMap failedResultMap = testContext.getFailedTests();
				IResultMap skippedResultMap = testContext.getSkippedTests();
				
				this.allTestsResults.addAll(this.createResultFromMap(passedResultMap));
				this.allTestsResults.addAll(this.createResultFromMap(failedResultMap));
				this.allTestsResults.addAll(this.createResultFromMap(skippedResultMap));
			}
		}	
		// logger.exiting();
	}	
	
	/**
	 * Generates individual TestCase Results based on map of passed, failed and skipped methods
	 * Returns the list of TestCaseResult objects generated.
	 */
	private List<TestCaseResult> createResultFromMap(IResultMap resultMap){
		// logger.entering(resultMap);
		List<TestCaseResult> statusWiseResults = new ArrayList<TestCaseResult>();
		
		for (ITestResult singleMethodResult : resultMap.getAllResults()){
			TestCaseResult tcresult1 = new TestCaseResult();
			tcresult1.setITestResultobj(singleMethodResult);
			statusWiseResults.add(tcresult1);
			
		}
		Collections.sort(statusWiseResults);
		// logger.exiting(statusWiseResults);
		return statusWiseResults;
	}

	/**
	 * Generates class based summary and the basis for Detailed groupwise summary report
	 */
	private void generateTCBasedData(ArrayList<TestCaseResult> allTestsList ){
		// logger.entering(allTestsList);
		SummarizedData tempClass = null, tempGroupClass = null ;
		Map<String, SummarizedData> mpClassData = new HashMap<String, SummarizedData>();
		
		for (TestCaseResult tcResult : allTestsList){
			
			//Segregating for class data
			String sTempClassName = tcResult.getClassName();
			
			//If class not already added to Class data, then create new ClassObject exists
			if (!mpClassData.containsKey(sTempClassName)){
				tempClass = new SummarizedData();
				tempClass.setsName(sTempClassName);
				
			}else{
				tempClass = mpClassData.get(sTempClassName);
			}
			
			//Adding test to total count
			tempClass.incrementiTotal();
			
			//Adding all groups to map
			for (String sGroup : tcResult.getGroup()){
				
			//Forming a key for the GroupClass map which is <GroupName><ClassName>
				String sGroupClassName = sGroup+sTempClassName;
				if (!mpGroupClassData.containsKey(sGroupClassName)){
					tempGroupClass = new SummarizedData();
					tempGroupClass.setsName(sTempClassName);
				
				}else
					tempGroupClass = mpGroupClassData.get(sGroupClassName);
				
				tempGroupClass.incrementiTotal();
				tempGroupClass.incrementCount(tcResult.getStatus());
				tempGroupClass.incrementDuration(tcResult.getDurationTaken());
				mpGroupClassData.put(sGroupClassName, tempGroupClass);
			}
			
			
			//Segregating for detailed Testcase Status wise data
			List<String> str = new ArrayList<String>();
			str.add(tcResult.getClassName());
			str.add(tcResult.getMethodName());
			str.add(tcResult.getTestDesc());
			str.add(tcResult.getGroup().toString());
			str.add(String.valueOf(tcResult.getDurationTaken()));
			str.add(tcResult.getsslink());
			
			//Based on status, incrementing class count and adding str to correct
			//list for TC detailed report
			switch (tcResult.getStatus()){
			case ITestResult.FAILURE : {
				tcFailedData.add(str);
				//For failed cases adding data for defect description sheet
				for (int iErrorCount = 0; iErrorCount < tcResult.getError().size(); iErrorCount++){
					List<String> tmpList = new ArrayList<String>();
					tmpList.addAll(0, str);
					tmpList.add(tcResult.getDefect().get(iErrorCount));
					tmpList.add(tcResult.getError().get(iErrorCount));
					tcDefectData.add(tmpList);
				}
				break;
			}
			case ITestResult.SUCCESS : {
				tcPassedData.add(str);
				break;
			}
			case ITestResult.SKIP : {
				tcSkippedData.add(str);
				break;
			}
			}
			tempClass.incrementCount(tcResult.getStatus());
			//Add to the total runtime of the class
			tempClass.setlRuntime(tempClass.getlRuntime()+tcResult.getDurationTaken());			
			mpClassData.put(sTempClassName, tempClass);
			}	
		SummarizedData[] ps = new SummarizedData[mpClassData.size()];
		lClasses = Arrays.asList(mpClassData.values().toArray(ps));
		Collections.sort(lClasses);
		// logger.exiting();
	}

}
