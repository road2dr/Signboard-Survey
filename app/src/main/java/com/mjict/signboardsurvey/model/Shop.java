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

	@DatabaseField(id=true)
	private String id; 	// id	// TODO 얘는 long 타입을 되는지 물어 봐
	
	@DatabaseField
	private String businessLicenseNumber;
	
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
	private String inputor;
	
	@DatabaseField
	private String inputDate;
	
	@DatabaseField(columnName="tblNum")
	private int tblNumber;
	
	@DatabaseField
	private int addressId;

	@DatabaseField
	private boolean isDeleted;

//	@DatabaseField
//	private int placedFloor;

//	@DatabaseField
//	private int totalFloor;


	public Shop() {
		
	}

	public Shop(String id, String businessLicenseNumber, String ssn, String name, String representative,
			String phoneNumber, int businessCondition, int category, long buildingId, String inputor,
			String inputDate, int tblNumber, int addressId, boolean isDeleted/*, int placedFloor, int totalFloor*/) {
		super();
		this.id = id;
		this.businessLicenseNumber = businessLicenseNumber;
		this.ssn = ssn;
		this.name = name;
		this.representative = representative;
		this.phoneNumber = phoneNumber;
		this.businessCondition = businessCondition;
		this.category = category;
		this.buildingId = buildingId;
		this.inputor = inputor;
		this.inputDate = inputDate;
		this.tblNumber = tblNumber;
		this.addressId = addressId;
		this.isDeleted = isDeleted;
//		this.placedFloor = placedFloor;
//		this.totalFloor = totalFloor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessLicenseNumber() {
		return businessLicenseNumber;
	}

	public void setBusinessLicenseNumber(String businessLicenseNumber) {
		this.businessLicenseNumber = businessLicenseNumber;
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

	public String getInputor() {
		return inputor;
	}

	public void setInputor(String inputor) {
		this.inputor = inputor;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public void setTblNumber(int tblNumber) {
		this.tblNumber = tblNumber;
	}
	
	public int getTblNumber() {
		return tblNumber;
	}
	
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	
	public int getAddressId() {
		return addressId;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

//	public int getPlacedFloor() {
//		return placedFloor;
//	}
//
//	public void setPlacedFloor(int placedFloor) {
//		this.placedFloor = placedFloor;
//	}
//
//	public int getTotalFloor() {
//		return totalFloor;
//	}
//
//	public void setTotalFloor(int totalFloor) {
//		this.totalFloor = totalFloor;
//	}
}