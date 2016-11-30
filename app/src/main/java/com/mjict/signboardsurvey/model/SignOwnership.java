package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class SignOwnership {

	@DatabaseField(generatedId=true)
	private long id;
	
	@DatabaseField
	private String shopId;
	
	@DatabaseField
	private long signId;
	
	public SignOwnership() {
		
	}

	public SignOwnership(long id, String shopId, long signId) {
		super();
		this.id = id;
		this.shopId = shopId;
		this.signId = signId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public long getSignId() {
		return signId;
	}

	public void setSignId(long signId) {
		this.signId = signId;
	}	
	
}
