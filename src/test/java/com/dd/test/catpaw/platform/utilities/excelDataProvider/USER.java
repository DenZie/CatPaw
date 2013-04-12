package com.dd.test.claws.platform.utilities.excelDataProvider;

public class USER {
	
	/*
	 * Data structure. Please note that the order is
	 * very important here. If this order is mismatched
	 * with the excel-sheet column, then the data may
	 * not be read correctly, or even fail to read.
	 * 
	 * This is the starting point of our data.
	 */
	private String name;
	private String password;
	private Long accountNumber;
	private Double amount;
	private AREA_CODE[] areaCode;
	private BANK bank;
	private String phoneNumber;
	private int	preintTest;
	private boolean isbooleanGood;
	private double doubleTest;
	private long longTest;
	private float floatTest;
	private byte byteTest;
	
	/*
	 * Get and Set properties
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public AREA_CODE[] getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(AREA_CODE[] areaCode) {
		this.areaCode = areaCode;
	}
	public BANK getBank() {
		return bank;
	}
	public void setBank(BANK bank) {
		this.bank = bank;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getPreintTest() {
		return preintTest;
	}
	
	public boolean getIsBooleanGood ()
	{
		return isbooleanGood;		
	}
	
	public double getDoubleTest () {
		return doubleTest;
	}
	
	public long getLongTest () {
		return longTest;
	}
	public float getFloatTest () {
		return floatTest;
	}
	public long getByteTest () {
		return byteTest;
	}
}
