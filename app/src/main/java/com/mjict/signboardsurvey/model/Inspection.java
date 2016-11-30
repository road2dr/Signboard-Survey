package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * @author Junseo
 *
 */
@DatabaseTable
public class Inspection {
	
	@DatabaseField(generatedId=true)
	private long id;		// id
	
	@DatabaseField(columnName="josano")
	private String no;
	
	@DatabaseField
	private String date;
	
	@DatabaseField
	private int mobileId;
	
	@DatabaseField
	private long signId;
	
	@DatabaseField
	private String userId;
	
	@DatabaseField
	private String inputDate;
		
	@DatabaseField
	private String syncDate;
	
	@DatabaseField(columnName="modId")
	private String modifyerId;
	
	@DatabaseField(columnName="modifyDate")
	private String modifyDate;
	
//	@DatabaseField
//	private String picNumber;
	
	public Inspection() {
		
	}

	public Inspection(long id, String no, String date, int mobileId, long signId, String userId, String inputDate,
					  String syncDate, String modifyerId, String modifyDate/*, String picNumber*/) {
		super();
		this.id = id;
		this.no = no;
		this.date = date;
		this.mobileId = mobileId;
		this.signId = signId;
		this.userId = userId;
		this.inputDate = inputDate;
		this.syncDate = syncDate;
		this.modifyerId = modifyerId;
		this.modifyDate = modifyDate;
//		this.picNumber = picNumber;
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getMobileId() {
		return mobileId;
	}


	public void setMobileId(int mobileId) {
		this.mobileId = mobileId;
	}


	public long getSignId() {
		return signId;
	}


	public void setSignId(long signId) {
		this.signId = signId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getInputDate() {
		return inputDate;
	}


	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}


	public String getSyncDate() {
		return syncDate;
	}


	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}

	public String getModifyerId() {
		return modifyerId;
	}

	public void setModifyerId(String modifyerId) {
		this.modifyerId = modifyerId;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
//	public String getPicNumber() {
//		return picNumber;
//	}
//	
//	public void setPicNumber(String picNumber) {
//		this.picNumber = picNumber;
//	}
		
}
