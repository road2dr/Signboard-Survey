package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Sign implements Serializable {
		
	private static final long serialVersionUID = 4767262872991666003L;

	@DatabaseField(generatedId=true)
	private long id;		// id
	
	@DatabaseField
	private int type;
	
	@DatabaseField
	private float width;
	
	@DatabaseField
	private float length;
	
	@DatabaseField
	private float height;

	// TODO 나중에 이 쓸모 없는 필드 빼자
	@DatabaseField
	private String area;
	
	@DatabaseField
	private float extraSize;

	// TODO 이것도 물어보고 빼든가 해
	@DatabaseField
	private int quantity;
	
	@DatabaseField
	private String content;
	
	@DatabaseField
	private int placedFloor;

	// TODO 나중에 필드 정할때 boolean 타입으로 되는지 물어봐
	@DatabaseField
	private String placedSide;
	
	@DatabaseField
	private int lightType;
	
	@DatabaseField
	private String placement;

	// TODO 이 필드도 필요하나..
	@DatabaseField
	private boolean streetInfringement;

	// TODO 나중에 이름 바꿔
	@DatabaseField(columnName = "streetInfringementWidth")
	private float collisionWidth;
	
	@DatabaseField(columnName = "streetInfringementLength")
	private float collisionLength;

	@DatabaseField
	private int inspectionResult;
	
	@DatabaseField
	private String permissionNumber;
	
	@DatabaseField
	private String inputor;		// TODO 철자 틀림 inputter
	
	@DatabaseField
	private String inputDate;
	
	@DatabaseField(columnName="needReInspection")
	private String needReinspection;

	// TODO 얘도 상태(철거/정상) 값인데 네이밍 바꾸자
	@DatabaseField(columnName="checkCode")
	private int statusCode;
	
	@DatabaseField
	private String picNumber;
	
	@DatabaseField
	private String modifier;
	
	@DatabaseField
	private String modifyDate;
	
	@DatabaseField
	private int totalFloor;	

	// TODO 나중에 필드 정할때 boolean 타입으로 되는지 물어봐
	@DatabaseField
	private String isIntersection;
	
	@DatabaseField(columnName="tblNum")
	private int tblNumber;

	@DatabaseField
	private int addressId;		// TODO 삭제 하는게 좋지 않나..

	@DatabaseField
	private boolean isDeleted;

	@DatabaseField(columnName = "reInspectionCode")
	private int reviewCode;		// TODO 코드 내용에 대해선 서버 & 명진하고 얘기 해봐야 함

	@DatabaseField(columnName = "demolishPicPath")
	private String demolitionPicPath;

	@DatabaseField(columnName = "demolishDate")
	private String demolishedDate;

	@DatabaseField(columnName = "frontBackRoad")
	private boolean isFrontBackRoad;

	// TODO "정면" 을 나타낼 필드가 없다


	public Sign() {
		
	}
	public Sign(long id, int type, float width, float length, float height, String area, float extraSize, int quantity,
			String content, int placedFloor, String placedSide, int lightType, String placement,
			boolean streetInfringement, float collisionWidth, float collisionLength,
			int inspectionResult, String permissionNumber, String inputor, String inputDate, String needReinspection,
			int statusCode, String picNumber, String modifier, String modifyDate, int totalFloor,
			String isIntersection, int tblNumber, int addressId, String demolitionPicPath, String demolishedDate,
				boolean isDeleted, int reviewCode, boolean isFrontBackRoad) {
		super();
		this.id = id;
		this.type = type;
		this.width = width;
		this.length = length;
		this.height = height;
		this.area = area;
		this.extraSize = extraSize;
		this.quantity = quantity;
		this.content = content;
		this.placedFloor = placedFloor;
		this.placedSide = placedSide;
		this.lightType = lightType;
		this.placement = placement;
		this.streetInfringement = streetInfringement;
		this.collisionWidth = collisionWidth;
		this.collisionLength = collisionLength;
		this.inspectionResult = inspectionResult;
		this.permissionNumber = permissionNumber;
		this.inputor = inputor;
		this.inputDate = inputDate;
		this.needReinspection = needReinspection;
		this.statusCode = statusCode;
		this.picNumber = picNumber;
		this.modifier = modifier;
		this.modifyDate = modifyDate;
		this.totalFloor = totalFloor;
		this.isIntersection = isIntersection;
		this.tblNumber = tblNumber;
		this.addressId = addressId;
		this.demolitionPicPath = demolitionPicPath;
		this.demolishedDate = demolishedDate;
		this.reviewCode = reviewCode;
		this.isFrontBackRoad = isFrontBackRoad;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public float getWidth() {
		return width;
	}


	public void setWidth(float width) {
		this.width = width;
	}


	public float getLength() {
		return length;
	}


	public void setLength(float length) {
		this.length = length;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public float getExtraSize() {
		return extraSize;
	}


	public void setExtraSize(float extraSize) {
		this.extraSize = extraSize;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public int getPlacedFloor() {
		return placedFloor;
	}


	public void setPlacedFloor(int placedFloor) {
		this.placedFloor = placedFloor;
	}


	public String getPlacedSide() {
		return placedSide;
	}


	public void setPlacedSide(String placedSide) {
		this.placedSide = placedSide;
	}


	public int getLightType() {
		return lightType;
	}


	public void setLightType(int lightType) {
		this.lightType = lightType;
	}


	public String getPlacement() {
		return placement;
	}


	public void setPlacement(String placement) {
		this.placement = placement;
	}


	public boolean isStreetInfringement() {
		return streetInfringement;
	}


	public void setStreetInfringement(boolean streetInfringement) {
		this.streetInfringement = streetInfringement;
	}


	public float getCollisionWidth() {
		return collisionWidth;
	}

	public float getCollisionLength() {
		return collisionLength;
	}

	public void setCollisionWidth(float collisionWidth) {
		this.collisionWidth = collisionWidth;
	}

	public void setCollisionLength(float collisionLength) {
		this.collisionLength = collisionLength;
	}

	public int getInspectionResult() {
		return inspectionResult;
	}


	public void setInspectionResult(int inspectionResult) {
		this.inspectionResult = inspectionResult;
	}


	public String getPermissionNumber() {
		return permissionNumber;
	}


	public void setPermissionNumber(String permissionNumber) {
		this.permissionNumber = permissionNumber;
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


	public String getNeedReinspection() {
		return needReinspection;
	}


	public void setNeedReinspection(String needReinspection) {
		this.needReinspection = needReinspection;
	}


	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public String getPicNumber() {
		return picNumber;
	}


	public void setPicNumber(String picNumber) {
		this.picNumber = picNumber;
	}


	public String getModifier() {
		return modifier;
	}


	public void setModifier(String modifier) {
		this.modifier = modifier;
	}


	public String getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}


	public int getTotalFloor() {
		return totalFloor;
	}


	public void setTotalFloor(int totalFloor) {
		this.totalFloor = totalFloor;
	}


	public String getIsIntersection() {
		return isIntersection;
	}


	public void setIsIntersection(String isIntersection) {
		this.isIntersection = isIntersection;
	}

	public int getTblNumber() {
		return tblNumber;
	}
	
	public void setTblNumber(int tblNumber){ 
		this.tblNumber = tblNumber;
	}

	public int getAddressId() {
		return addressId;
	}
	
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getDemolitionPicPath() {
		return demolitionPicPath;
	}

	public void setDemolitionPicPath(String demolitionPicPath) {
		this.demolitionPicPath = demolitionPicPath;
	}

	public String getDemolishedDate() {
		return demolishedDate;
	}

	public void setDemolishedDate(String demolishedDate) {
		this.demolishedDate = demolishedDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getReviewCode() {
		return reviewCode;
	}

	public void setReviewCodeCode(int reInspectionCode) {
		this.reviewCode = reInspectionCode;
	}

	public boolean isFrontBackRoad() {
		return isFrontBackRoad;
	}

	public void setFrontBackRoad(boolean isFrontBackRoad) {
		this.isFrontBackRoad = isFrontBackRoad;
	}
}
