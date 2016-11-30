package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Mobile {

	@DatabaseField(id=true)
	int id;
	
	@DatabaseField
	String model;
	
	@DatabaseField
	String date;
	
	public Mobile() {
		
	}
	
	public Mobile(int id, String model, String date) {
		this.id = id;
		this.model = model;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
