package com.dd.test.catpaw.reports.reporter.excelreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;




/**
 * Generates a map of report to be generated. <br>
 * Maps Report sheet name to list of reports on the sheet to the data it contains </br> Each object represents one
 * sheet.
 * 
 * @param <V>
 */
@SuppressWarnings(value = { "unchecked" })
public class ReportMap<V> {
	// private static SimpleLogger logger = CatPawLogger.getLogger();

	String sReportSheetName;
	Map<String, List<V>> reportData;
	int iType = 0;
	List<BaseReport<V>> generatedReport;

	public ReportMap(String sSheetName, Map<String, List<V>> mpRep, int iTypeOfReport) {

		this.sReportSheetName = sSheetName;
		this.iType = iTypeOfReport;
		reportData = mpRep;

		this.generatedReport = constructReport(iTypeOfReport);
	}

	public ReportMap(String sSheetName, List<BaseReport<V>> lsReports) {

		this.sReportSheetName = sSheetName;
		this.generatedReport = lsReports;

	}

	public String getName() {
		return sReportSheetName;
	}

	public void setName(String sName) {
		this.sReportSheetName = sName;
	}

	public Map<String, List<V>> getReportData() {
		return reportData;
	}

	public void setReportData(Map<String, List<V>> reportData) {
		this.reportData = reportData;
	}

	public List<BaseReport<V>> getGeneratedReport() {
		return generatedReport;
	}

	public void setGeneratedReport(List<BaseReport<V>> generatedReport) {
		this.generatedReport = generatedReport;
	}

	public void addToGeneratedReport(List<BaseReport<V>> lstCustomReport) {
		this.generatedReport.addAll(lstCustomReport);
	}

	private List<BaseReport<V>> constructReport(int iTypeOfReport) {
		// logger.entering(iTypeOfReport);
		List<BaseReport<V>> lb = new ArrayList<BaseReport<V>>();
		BaseReport b;
		for (String indReport : this.reportData.keySet()) {
			if (iTypeOfReport == 0)
				b = new SummaryReport(indReport);
			else
				b = new DetailsReport(indReport);

			b.setLstEntities(this.reportData.get(indReport));
			lb.add(b);
		}
		// logger.exiting(lb);
		return lb;
	}

	public boolean equals(ReportMap<V> o) {

		return this.sReportSheetName.equals(o.getName());
	}

}
