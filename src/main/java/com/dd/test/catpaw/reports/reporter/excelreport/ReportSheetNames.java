package com.dd.test.catpaw.reports.reporter.excelreport;
/**
 * Enum defining the major reports.
 * Contains list of sub reports
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ReportSheetNames{
	
		TESTSUMMARYREPORT("Test Summary",Arrays.asList(new String[]{"Full Suite Summary","Test Summary", "Classwise Summary","Groupwise Summary"})),
		GROUPSUMMARYREPORT("Detailed Groupwise Summary", new ArrayList<String>()),
		TESTCASEREPORT("TestCasewise Report",Arrays.asList(new String[]{"Failed TC List","Passed TC List", "Skipped TC List"})),
		DEFECTREPORT("Failure List", Arrays.asList(new String[]{"Defect Summary"}));
		
		private String sRepName;
		private List<String> lsSubReportNames;
		
		ReportSheetNames(String sReportName, List<String> lsSubReports){
			this.sRepName = sReportName;
			this.lsSubReportNames = lsSubReports;
		}
		
		public String getName(){
			return this.sRepName;
		}
		
		public List<String> getSubReportNames(){
			return this.lsSubReportNames;
		}
		
		public void setReport(List<String> lsReports){
			this.lsSubReportNames = lsReports;
			
		}
		
		public void addReport(String sSubReportName){
			this.lsSubReportNames.add(sSubReportName);
		}
}
