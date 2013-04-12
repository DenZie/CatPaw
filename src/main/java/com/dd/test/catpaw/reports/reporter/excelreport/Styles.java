package com.dd.test.catpaw.reports.reporter.excelreport;

/**
 * <br>Creates different styles applied to different cells in the ExcelSheet</br>
 */
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class Styles {

	static HSSFWorkbook wb1;
	static HSSFCellStyle headingStyle, subHeading1Style, subHeading2Style, thinBorderStyle, 
						style_borderthin_center,hyperLinkStyle;

	private static HSSFCellStyle createCustomStyle(HSSFFont fontStyle, Short... alignment){
		HSSFCellStyle style = wb1.createCellStyle();
		style.setFont(fontStyle);
		if (alignment.length > 0)
			style.setAlignment(alignment[0]);
		return style;
	}
	
	private static HSSFFont createCustomFont(short colorIndex, Boolean bItalic, Byte underlineWeight){
		HSSFFont font = wb1.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(colorIndex);
		font.setUnderline(underlineWeight);
		return font;
	}
	
	private static HSSFCellStyle setAllBorders(short borderWeight, HSSFCellStyle existingStyle){
		existingStyle.setBorderBottom(borderWeight);
		existingStyle.setBorderTop(borderWeight);
		existingStyle.setBorderLeft(borderWeight);
		existingStyle.setBorderRight(borderWeight);
		return existingStyle;
	}
	
	static public void initStyles(HSSFWorkbook wb){
		wb1 = wb;
		headingStyle = createCustomStyle(createCustomFont(HSSFColor.LEMON_CHIFFON.index, true, HSSFFont.U_NONE), HSSFCellStyle.ALIGN_CENTER);
		headingStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headingStyle.setFillForegroundColor(new HSSFColor.BLUE_GREY().getIndex());
		headingStyle = setAllBorders(HSSFCellStyle.BORDER_DOUBLE, headingStyle);
		
		subHeading1Style = createCustomStyle(createCustomFont(HSSFColor.LIGHT_BLUE.index, true, HSSFFont.U_NONE));
		subHeading1Style = setAllBorders(HSSFCellStyle.BORDER_THIN, subHeading1Style);
		
		subHeading2Style = createCustomStyle(createCustomFont(HSSFColor.BROWN.index, false,HSSFFont.U_NONE),HSSFCellStyle.ALIGN_CENTER );
		subHeading2Style = setAllBorders(HSSFCellStyle.BORDER_MEDIUM, subHeading2Style);
		
		thinBorderStyle = wb.createCellStyle();
		thinBorderStyle = setAllBorders(HSSFCellStyle.BORDER_THIN, thinBorderStyle);

		style_borderthin_center = wb.createCellStyle();
		style_borderthin_center = setAllBorders(HSSFCellStyle.BORDER_THIN, style_borderthin_center);
		style_borderthin_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		hyperLinkStyle = wb.createCellStyle();
		hyperLinkStyle = setAllBorders(HSSFCellStyle.BORDER_THIN, hyperLinkStyle);
		HSSFFont hyperLinkFont = createCustomFont(HSSFColor.BLUE.index, true, HSSFFont.U_SINGLE);
		hyperLinkFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		hyperLinkStyle.setFont(hyperLinkFont);
	}
}