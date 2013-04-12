package com.dd.test.catpaw.reports.reporter.excelreport;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;



/**
 * Extends BaseReport
 * <br>Displays data which is of the type {@link SummarizedData}</br>
 * 
 */
public class SummaryReport extends BaseReport<SummarizedData> {
	// private static SimpleLogger logger = CatPawLogger.getLogger();
	
	public SummaryReport() {
		super();
		
		String[] sTitles = new String[]{"Name", "Total TC", "Passed", "Failed", "Skipped", "Time taken"};
		List<String> colTitles = new ArrayList<String>();
		
		for (String title : sTitles){
			colTitles.add(title);
		}
		setColTitles(colTitles);
	}
	
	public SummaryReport(String sReportName) {
		this();
		this.setsReportName(sReportName);
		
	}

	public List<SummarizedData> getLstEntities() {
		
		return (List<SummarizedData>)super.getLstEntities();
	}
	
	int fillData(HSSFSheet sheet, int rowNum, HSSFCellStyle style){
		// logger.entering(new Object[]{sheet, rowNum, style});
		
		HSSFRow row;  
		
		for (SummarizedData ps : this.getLstEntities()){
			row = sheet.createRow(rowNum);
			int iColNum = iStartColNum;
			//Setting styles for each column first
			for (int i = 0 ; i < this.getColTitles().size(); i++){
				row.createCell(iColNum);
				row.getCell(iColNum++).setCellStyle(style);
				
			}
			int iSetDataCol = iStartColNum;
			//Filling in data - (CI)
			row.getCell(iSetDataCol).setCellValue(ps.getsName());
			row.getCell(++iSetDataCol).setCellValue(ps.getiTotal());
			row.getCell(++iSetDataCol).setCellValue(ps.getiPassedCount());
			row.getCell(++iSetDataCol).setCellValue(ps.getiFailedCount());
			row.getCell(++iSetDataCol).setCellValue(ps.getiSkippedCount());
			row.getCell(++iSetDataCol).setCellValue(formatMilliSecondTime(ps.getlRuntime()));
			rowNum++;
			
			for (int i = --iColNum ; i >= iStartColNum; i--){
				sheet.autoSizeColumn(i);
			}
			
		}
		// logger.exiting(rowNum);
		return rowNum;
	}
}
