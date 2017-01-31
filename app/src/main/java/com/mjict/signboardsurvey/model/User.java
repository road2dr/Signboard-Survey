package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class User {

	@DatabaseField(id=true, columnName="id")
	private String userId;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String password;	// password
	
	@DatabaseField
	private String province;
	
	@DatabaseField
	private String county;

	@DatabaseField
	private int mobileId;
	
	public User() {
	}
	
	public User(String userId, String name, String password, String province, String county, int mobileId) {
		super();
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.province = province;
		this.county = county;
		this.mobileId = mobileId;
	}
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public int getMobileId() {
		return mobileId;
	}

	public void setMobileId(int mobileId) {
		this.mobileId = mobileId;
	}
}
