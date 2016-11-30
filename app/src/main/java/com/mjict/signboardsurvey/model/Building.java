package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Building implements Serializable {

	private static final long serialVersionUID = -9186891251830017806L;

	@DatabaseField(canBeNull=true, id=true)
	private long id;
	
	@DatabaseField
	private String province;
	
	@DatabaseField
	private String county;
	
	@DatabaseField
	private String town;
	
	@DatabaseField
	private String village;
	
	@DatabaseField
	private String additionalAddress;
	
	@DatabaseField
	private String houseNumber;
	
	@DatabaseField
	private String streetName;
	
	@DatabaseField
	private String firstBuildingNumber;
	
	@DatabaseField
	private String secondBuildingNumber;
	
	@DatabaseField
	private String name;
	
	@DatabaseField(columnName="IsMountain")
	private boolean isMountain;
	
	@DatabaseField
	private String lotNumber;
	
	@DatabaseField
	private int floorCount;
	
	@DatabaseField
	private String mapNumber;
	
	@DatabaseField
	private String plcd;
	
	@DatabaseField
	private int addressId;

	@DatabaseField
	private int areaType;

	@DatabaseField
	private double latitude;

	@DatabaseField
	private double longitude;

	
	public Building() {
		
	}

	public Building(long id, String province, String county, String town, String village, String additionalAddress, String houseNumber,
			String streetName, String firstBuildingNumber, String secondBuildingNumber, String name, boolean isMountain,
			String lotNumber, int floorCount, String mapNumber, String plcd, int addressId, int areaType, double latitude, double longitude) {
		super();
		this.id = id;
		this.province = province;
		this.county = county;
		this.town = town;
		this.village = village;
		this.additionalAddress = additionalAddress;
		this.houseNumber = houseNumber;
		this.streetName = streetName;
		this.firstBuildingNumber = firstBuildingNumber;
		this.secondBuildingNumber = secondBuildingNumber;
		this.name = name;
		this.isMountain = isMountain;
		this.lotNumber = lotNumber;
		this.floorCount = floorCount;
		this.mapNumber = mapNumber;
		this.plcd = plcd;
		this.addressId = addressId;
		this.areaType = areaType;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	
	public String getVillage() {
		return village;
	}
	
	public void setVillage(String village) {
		this.village = village;
	}

	public String getAdditionalAddress() {
		return additionalAddress;
	}

	public void setAdditionalAddress(String additionalAddress) {
		this.additionalAddress = additionalAddress;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getFirstBuildingNumber() {
		return firstBuildingNumber;
	}

	public void setFirstBuildingNumber(String firstBuildingNumber) {
		this.firstBuildingNumber = firstBuildingNumber;
	}

	public String getSecondBuildingNumber() {
		return secondBuildingNumber;
	}

	public void setSecondBuildingNumber(String secondBuildingNumber) {
		this.secondBuildingNumber = secondBuildingNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMountain() {
		return isMountain;
	}

	public void setMountain(boolean isMountain) {
		this.isMountain = isMountain;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public int getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(int floorCount) {
		this.floorCount = floorCount;
	}

	public String getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(String mapNumber) {
		this.mapNumber = mapNumber;
	}

	public String getPlcd() {
		return plcd;
	}

	public void setPlcd(String plcd) {
		this.plcd = plcd;
	}

	public int getAddressId() {
		return addressId;
	}
	
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public int getAreaType() {
		return areaType;
	}

	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
