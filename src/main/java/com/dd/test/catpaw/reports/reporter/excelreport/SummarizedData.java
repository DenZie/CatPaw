package com.dd.test.catpaw.reports.reporter.excelreport;

import com.dd.test.catpaw.reports.reporter.excelreport.SummarizedData;

/**
 * 
 * <br>Represents summary counts like total passed, failed, skipped</br>
 */

public class SummarizedData implements Comparable<SummarizedData>{
	
	public static final int PASS = 1;
	public static final int FAIL = 2;
	public static final int SKIP = 3;
	
	String sName;
	private int iPassedCount, iFailedCount, iSkippedCount, iTotal;
	private long lRuntime;
	
	/**
	 * @return the sName
	 */
	public String getsName() {
		return sName;
	}
	/**
	 * @param sName the sName to set
	 */
	public void setsName(String sName) {
		this.sName = sName;
	}
	/**
	 * @return the iPassedCount
	 */
	public int getiPassedCount() {
		return iPassedCount;
	}
	/**
	 * @param iPassedCount the iPassedCount to set
	 */
	public void setiPassedCount(int iPassedCount) {
		this.iPassedCount = iPassedCount;
	}
	/**
	 * @return the iFailedCount
	 */
	public int getiFailedCount() {
		return iFailedCount;
	}
	/**
	 * @param iFailedCount the iFailedCount to set
	 */
	public void setiFailedCount(int iFailedCount) {
		this.iFailedCount = iFailedCount;
	}
	/**
	 * @return the iSkippedCount
	 */
	public int getiSkippedCount() {
		return iSkippedCount;
	}
	/**
	 * @param iSkippedCount the iSkippedCount to set
	 */
	public void setiSkippedCount(int iSkippedCount) {
		this.iSkippedCount = iSkippedCount;
	}
	/**
	 * @return the iTotal
	 */
	public int getiTotal() {
		return iTotal;
	}
	/**
	 * @param iTotal the iTotal to set
	 */
	public void setiTotal(int iTotal) {
		this.iTotal = iTotal;
	}
	
	public void incrementiTotal() {
		this.iTotal++;
	}
	
	public void incrementiTotal(int iAddToTotal) {
		this.setiTotal(iAddToTotal + this.getiTotal());
	}
	/**
	 * @return the iRuntime
	 */
	public long getlRuntime() {
		return lRuntime;
	}
	
	public void incrementDuration(long testRunTime){
		this.setlRuntime(this.getlRuntime()+ testRunTime);
	}
	/**
	 * @param iRuntime -  the iRuntime to set
	 */
	public void setlRuntime(long iRuntime) {
		this.lRuntime = iRuntime;
	}
	
	public void incrementCount(int toIncrement){
		switch(toIncrement){
		case SummarizedData.PASS:{
			this.iPassedCount++;
			break;
		}
		case SummarizedData.FAIL:{
			this.iFailedCount++;
			break;
		}
		case SummarizedData.SKIP:{
			this.iSkippedCount++;
			break;
		}
		}
		
	}
	public int compareTo(SummarizedData o) {
			
		return this.getsName().compareTo(((SummarizedData) o).getsName());

	}
	

}




