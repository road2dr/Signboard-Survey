package com.mjict.signboardsurvey.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Sign implements Serializable {
		
	private static final long serialVersionUID = 4767262872991666003L;

	@DatabaseField(generatedId=true)
	private long id;		// id

	@DatabaseField(columnName="josaNo")
	private String inspectionNumber;

	@DatabaseField(columnName="josaDate")
	private String inspectionDate;		// yyyymmdd

	@DatabaseField
	private String mobileId;

	@DatabaseField(columnName="isSync")
	private boolean isSynchronized;

	@DatabaseField
	private String syncDate;
	
	@DatabaseField
	private String type;
	
	@DatabaseField
	private float width;
	
	@DatabaseField
	private float length;
	
	@DatabaseField
	private float height;

	@DatabaseField
	private float area;
	
	@DatabaseField
	private float extraSize;

	// TODO 이것도 물어보고 빼든가 해
	@DatabaseField
	private int quantity;
	
	@DatabaseField
	private String content;
	
	@DatabaseField
	private int placedFloor;	// 설치 층

	// TODO 나중에 필드 정할때 boolean 타입으로 되는지 물어봐
	@DatabaseField(columnName = "placedSide")
	private boolean isFront;
	
	@DatabaseField
	private String lightType;
	
	@DatabaseField
	private String placement;		// TODO 얘 무슨 필드 인지 확인 해봐 자꾸 헷갈린다

	// TODO 이 필드도 필요하나..
	@DatabaseField(columnName = "streetInfringement")
	private boolean isCollision;

	// TODO 나중에 이름 바꿔
	@DatabaseField(columnName = "collisionWidth")
	private float collisionWidth;
	
	@DatabaseField(columnName = "collisionLength")
	private float collisionLength;

	@DatabaseField
	private String inspectionResult;
	
	@DatabaseField
	private String permissionNumber;

//	@DatabaseField(columnName="needReInspection")
//	private String needReinspection;	// TODO 필요 없어 보이는데

	@DatabaseField(columnName = "inputor")
	private String inputter;
	
	@DatabaseField
	private String inputDate;

	@DatabaseField(columnName="checkCode")
	private String statsCode;
	
	@DatabaseField
	private String picNumber;
	
	@DatabaseField
	private String modifier;
	
	@DatabaseField
	private String modifyDate;
	
	@DatabaseField
	private int totalFloor;	

	@DatabaseField
	private boolean isIntersection;
	
	@DatabaseField(columnName="tblNumber")
	private int tblNumber;

//	@DatabaseField
//	private boolean isDeleted;

	@DatabaseField(columnName = "frontBackRoad")
	private boolean isFrontBackRoad;

	@DatabaseField(columnName = "demolishedPicPath")
	private String demolitionPicPath;

	@DatabaseField(columnName = "demolishedDate")
	private String demolishedDate;

	@DatabaseField(columnName = "reInspectionCode")
	private String reviewCode;

	@DatabaseField
	private long shopId;

	@DatabaseField
	private int addressId;

	@DatabaseField(columnName = "sgcode")
	private String sgCode;	// 지역 코드 인듯? 서버에서 넣어주길 원함. 없으면 빌딩의 값을 넣어주자

	@DatabaseField(columnName = "placedCode")
	private String installSide;

	@DatabaseField(columnName = "placedExt")
	private String uniqueness;

	@DatabaseField
	private String memo;

	@DatabaseField
	private boolean modified;



	public Sign() {
		
	}

	public Sign(Sign s) {	// 복사 생성자
		this.id = s.id;
		this.inspectionNumber = s.inspectionNumber;
		this.inspectionDate = s.inspectionDate;
		this.mobileId = s.mobileId;
		this.isSynchronized = s.isSynchronized;
		this.syncDate = s.syncDate;
		this.type = s.type;
		this.width = s.width;
		this.length = s.length;
		this.height = s.height;
		this.area = s.area;
		this.extraSize = s.extraSize;
		this.quantity = s.quantity;
		this.content = s.content;
		this.placedFloor = s.placedFloor;
		this.isFront = s.isFront;
		this.lightType = s.lightType;
		this.placement = s.placement;
		this.isCollision = s.isCollision;
		this.collisionWidth = s.collisionWidth;
		this.collisionLength = s.collisionLength;
		this.inspectionResult = s.inspectionResult;
		this.permissionNumber = s.permissionNumber;
//		this.needReinspection = needReinspection;
		this.inputter = s.inputter;
		this.inputDate = s.inputDate;
		this.statsCode = s.statsCode;
		this.picNumber = s.picNumber;
		this.modifier = s.modifier;
		this.modifyDate = s.modifyDate;
		this.totalFloor = s.totalFloor;
		this.isIntersection = s.isIntersection;
		this.tblNumber = s.tblNumber;
//		this.isDeleted = isDeleted;
		this.isFrontBackRoad = s.isFrontBackRoad;
		this.demolitionPicPath = s.demolitionPicPath;
		this.demolishedDate = s.demolishedDate;
		this.reviewCode = s.reviewCode;
		this.shopId = s.shopId;
		this.addressId = s.addressId;
		this.sgCode = s.sgCode;
		this.installSide = s.installSide;
		this.uniqueness = s.uniqueness;
		this.memo = s.memo;
		this.mobileId = s.mobileId;
	}

	public Sign(long id, String inspectionNumber, String inspectionDate, String mobileId,
				boolean isSynchronized, String syncDate, String type, float width, float length,
				float height, float area, float extraSize, int quantity, String content,
				int placedFloor, boolean isFront, String lightType, String placement, boolean isCollision,
				float collisionWidth, float collisionLength, String inspectionResult, String permissionNumber,
				/*String needReinspection, */String inputter, String inputDate, String statsCode, String picNumber,
				String modifier, String modifyDate, int totalFloor, boolean isIntersection, int tblNumber,/* boolean isDeleted,*/
				boolean isFrontBackRoad, String demolitionPicPath, String demolishedDate, String reviewCode,
				long shopId, int addressId, String sgCode, String installSide, String uniqueness, String memo, boolean modified) {
		this.id = id;
		this.inspectionNumber = inspectionNumber;
		this.inspectionDate = inspectionDate;
		this.mobileId = mobileId;
		this.isSynchronized = isSynchronized;
		this.syncDate = syncDate;
		this.type = type;
		this.width = width;
		this.length = length;
		this.height = height;
		this.area = area;
		this.extraSize = extraSize;
		this.quantity = quantity;
		this.content = content;
		this.placedFloor = placedFloor;
		this.isFront = isFront;
		this.lightType = lightType;
		this.placement = placement;
		this.isCollision = isCollision;
		this.collisionWidth = collisionWidth;
		this.collisionLength = collisionLength;
		this.inspectionResult = inspectionResult;
		this.permissionNumber = permissionNumber;
//		this.needReinspection = needReinspection;
		this.inputter = inputter;
		this.inputDate = inputDate;
		this.statsCode = statsCode;
		this.picNumber = picNumber;
		this.modifier = modifier;
		this.modifyDate = modifyDate;
		this.totalFloor = totalFloor;
		this.isIntersection = isIntersection;
		this.tblNumber = tblNumber;
//		this.isDeleted = isDeleted;
		this.isFrontBackRoad = isFrontBackRoad;
		this.demolitionPicPath = demolitionPicPath;
		this.demolishedDate = demolishedDate;
		this.reviewCode = reviewCode;
		this.shopId = shopId;
		this.addressId = addressId;
		this.sgCode = sgCode;
		this.installSide = installSide;
		this.uniqueness = uniqueness;
		this.memo = memo;
		this.modified = modified;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInspectionNumber() {
		return inspectionNumber;
	}

	public void setInspectionNumber(String inspectionNumber) {
		this.inspectionNumber = inspectionNumber;
	}

	public String getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public void setSynchronized(boolean aSynchronized) {
		isSynchronized = aSynchronized;
	}

	public String getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
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

	public boolean isFront() {
		return isFront;
	}

	public void setFront(boolean front) {
		isFront = front;
	}

	public String getLightType() {
		return lightType;
	}

	public void setLightType(String lightType) {
		this.lightType = lightType;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	public boolean isCollision() {
		return isCollision;
	}

	public void setCollision(boolean collision) {
		isCollision = collision;
	}

	public float getCollisionWidth() {
		return collisionWidth;
	}

	public void setCollisionWidth(float collisionWidth) {
		this.collisionWidth = collisionWidth;
	}

	public float getCollisionLength() {
		return collisionLength;
	}

	public void setCollisionLength(float collisionLength) {
		this.collisionLength = collisionLength;
	}

	public String getInspectionResult() {
		return inspectionResult;
	}

	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}

	public String getPermissionNumber() {
		return permissionNumber;
	}

	public void setPermissionNumber(String permissionNumber) {
		this.permissionNumber = permissionNumber;
	}

//	public String getNeedReinspection() {
//		return needReinspection;
//	}
//
//	public void setNeedReinspection(String needReinspection) {
//		this.needReinspection = needReinspection;
//	}

	public String getInputter() {
		return inputter;
	}

	public void setInputter(String inputter) {
		this.inputter = inputter;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getStatsCode() {
		return statsCode;
	}

	public void setStatsCode(String statsCode) {
		this.statsCode = statsCode;
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

	public boolean isIntersection() {
		return isIntersection;
	}

	public void setIntersection(boolean intersection) {
		isIntersection = intersection;
	}

	public int getTblNumber() {
		return tblNumber;
	}

	public void setTblNumber(int tblNumber) {
		this.tblNumber = tblNumber;
	}

//	public boolean isDeleted() {
//		return isDeleted;
//	}
//
//	public void setDeleted(boolean deleted) {
//		isDeleted = deleted;
//	}

	public boolean isFrontBackRoad() {
		return isFrontBackRoad;
	}

	public void setFrontBackRoad(boolean frontBackRoad) {
		isFrontBackRoad = frontBackRoad;
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

	public String getReviewCode() {
		return reviewCode;
	}

	public void setReviewCode(String reviewCode) {
		this.reviewCode = reviewCode;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getSgCode() {
		return sgCode;
	}

	public void setSgCode(String sgCode) {
		this.sgCode = sgCode;
	}

	public String getInstallSide() {
		return installSide;
	}

	public void setInstallSide(String installSide) {
		this.installSide = installSide;
	}

	public String getUniqueness() {
		return uniqueness;
	}

	public void setUniqueness(String uniqueness) {
		this.uniqueness = uniqueness;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
