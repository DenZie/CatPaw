package com.dd.test.catpaw.reports.reporter.excelreport;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Hyperlink;




/**
 * Extends BaseReport <br>
 * Any generic report that gives data as List of String
 */
public class DetailsReport extends BaseReport<List<String>> {
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();

	/**
	 * Initializes report with TestCase details column titles
	 */
	public DetailsReport() {
		super();
		String[] sTitles = new String[]{"Class Name","Method/Testcase id","Test Description","Group[s]", "Time taken","Link"};

		List<String> colTitles = new ArrayList<String>();
		for (String title : sTitles){
			colTitles.add(title);
		}
		super.setColTitles(colTitles);
	}

	public DetailsReport(String sReportName) {
		this();
		this.setsReportName(sReportName);
	}
	
	public List<List<String>> getLstEntities() {
	
		return (List<List<String>>)super.getLstEntities();
	}

		
	int fillData(HSSFSheet sheet, int rowNum, HSSFCellStyle style){
		// logger.entering(new Object[]{sheet, rowNum, style});
		HSSFRow row;  
		
		for (List<String> dataString : this.getLstEntities()){
			row = sheet.createRow(rowNum);
			int iColNum = iStartColNum;
			for (int i = 0 ; i < this.getColTitles().size(); i++){
				row.createCell(iColNum);
				row.getCell(iColNum).setCellStyle(Styles.thinBorderStyle);
				
				//Displaying time after converting to minutes
				if (this.getColTitles().get(i).toString().contains("Time")){
					Long timeInMilli = Long.parseLong(dataString.get(i));
					row.getCell(iColNum).setCellValue(formatMilliSecondTime(timeInMilli));
				}
				//Change style and datatype for link columns to make data appear and behave
				//as links 
				else if(this.getColTitles().get(i).toString().contains("Link")){
						
							Hyperlink link = new HSSFHyperlink(Hyperlink.LINK_URL);
							link.setAddress(dataString.get(i));
							row.getCell(iColNum).setCellStyle(Styles.hyperLinkStyle);
							row.getCell(iColNum).setCellValue("Link to details");
							row.getCell(iColNum).setHyperlink(link);
						
				}
				else{
					row.getCell(iColNum).setCellValue(dataString.get(i));
				}
				sheet.autoSizeColumn(iColNum++);
			}
			
			rowNum++;
			
		}
		// logger.exiting(rowNum);
		return rowNum;
		
	}
}
