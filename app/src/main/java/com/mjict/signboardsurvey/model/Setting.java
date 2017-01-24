package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Setting {
	
	@DatabaseField
	private int category;
	
	@DatabaseField
	private String code;
	
	@DatabaseField(columnName="Name")
	private String name;
	
	@DatabaseField
	private int ord;

//	@DatabaseField
//	private int group;
	
	public Setting() {
		
	}
	
	public Setting(int category, String code, String name, int ord) {
		this.category = category;
		this.code = code;
		this.name = name;
		this.ord = ord;
//		this.group = group;
	}
	
	public int getCategory() {
		return category;
	}
	
	public void setCategory(int category) {
		this.category = category;
	}
	
	public int getCode() {
		int value = 0;
		try {
			value = Integer.valueOf(code); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getCodeString() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getOrd() {
		return ord;
	}
	
	public void setOrd(int ord) {
		this.ord = ord;
	}
	
	@Override
	public String toString() {
		return name;
	}

//	public int getGroup() {
//		return group;
//	}
//
//	public void setGroup(int group) {
//		this.group = group;
//	}
}
