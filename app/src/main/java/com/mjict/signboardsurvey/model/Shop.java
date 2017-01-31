package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Shop implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1989796112959429983L;

	@DatabaseField(generatedId = true)
	private long id;
	
	@DatabaseField
	private String licenseNumber;
	
	@DatabaseField
	private String ssn;
	
	@DatabaseField(uniqueCombo=true)
	private String name;
	
	@DatabaseField
	private String representative;
	
	@DatabaseField
	private String phoneNumber;
	
	@DatabaseField
	private int businessCondition;	// 정상, 폐업
	
	@DatabaseField
	private int category;
	
	@DatabaseField
	private long buildingId;

	@DatabaseField
	private String inputter;
	
	@DatabaseField
	private String inputDate;
	
	@DatabaseField(columnName="tblNumer")
	private int tblNumber;
	
	@DatabaseField
	private int addressId;

	@DatabaseField
	private boolean isDeleted;

	@DatabaseField(columnName = "sgcode")
	private String sgCode;

	@DatabaseField(columnName = "isSync")
	private boolean isSynchronized;

	@DatabaseField
	private String syncDate;


	public Shop() {
		
	}


	public Shop(long id, String licenseNumber, String ssn, String name, String representative,
				String phoneNumber, int businessCondition, int category, long buildingId,
				String inputter, String inputDate, int tblNumber, int addressId, boolean isDeleted,
				String sgCode, boolean isSynchronized, String syncDate) {
		this.id = id;
		this.licenseNumber = licenseNumber;
		this.ssn = ssn;
		this.name = name;
		this.representative = representative;
		this.phoneNumber = phoneNumber;
		this.businessCondition = businessCondition;
		this.category = category;
		this.buildingId = buildingId;
		this.inputter = inputter;
		this.inputDate = inputDate;
		this.tblNumber = tblNumber;
		this.addressId = addressId;
		this.isDeleted = isDeleted;
		this.sgCode = sgCode;
		this.isSynchronized = isSynchronized;
		this.syncDate = syncDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getBusinessCondition() {
		return businessCondition;
	}

	public void setBusinessCondition(int businessCondition) {
		this.businessCondition = businessCondition;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(long buildingId) {
		this.buildingId = buildingId;
	}

	public String getInputter() {
		return inputter;
	}

	public void setInputter(String inputter) {
		this.inputter = inputter;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public int getTblNumber() {
		return tblNumber;
	}

	public void setTblNumber(int tblNumber) {
		this.tblNumber = tblNumber;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public String getSgCode() {
		return sgCode;
	}

	public void setSgCode(String sgCode) {
		this.sgCode = sgCode;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public void setSynchronized(boolean aSynchronized) {
		isSynchronized = aSynchronized;
	}

	public String getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}
}