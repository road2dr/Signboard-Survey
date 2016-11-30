package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class BuildingPicture implements Serializable {

	@DatabaseField(generatedId=true)
	private long id;
	
	@DatabaseField(canBeNull=true)
	private long buildingId;
	
	@DatabaseField
	private String path;
	
	@DatabaseField
	private String direction;

	// TODO 아마 추가 될 필드
	private boolean isDeleted;

	public BuildingPicture() {
		
	}
	
	public BuildingPicture(long id, long buildingId, String path, String direction) {
		super();
		this.id = id;
		this.buildingId = buildingId;
		this.path = path;
		this.direction = direction;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	
}
