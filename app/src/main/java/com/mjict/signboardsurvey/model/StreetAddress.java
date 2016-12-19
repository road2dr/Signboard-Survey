package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName="streetAddress")
public class StreetAddress implements Serializable {
	
	@DatabaseField
	private String initialConsonant;
	
	@DatabaseField
	private String street;
	
	@DatabaseField
	private String town;
	
	public StreetAddress() {
		
	}
	
	public StreetAddress(String initialConsonant, String street, String town) {
		super();
		this.initialConsonant = initialConsonant;
		this.street = street;
		this.town = town;
	}
	
	public String getInitialConsonant() {
		return initialConsonant;
	}

	public void setInitialConsonant(String initialConsonant) {
		this.initialConsonant = initialConsonant;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}
}
