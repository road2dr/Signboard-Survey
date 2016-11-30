package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TownForInspection {
	
	@DatabaseField
	private int id;
	
	@DatabaseField
	private String province;
	
	@DatabaseField
	private String county;
	
	@DatabaseField
	private String town;
	
	public TownForInspection() {

	}
	
	public TownForInspection(int id, String province, String county, String town) {
		super();
		this.id = id;
		this.province = province;
		this.county = county;
		this.town = town;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}
	
}
