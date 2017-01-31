package com.mjict.signboardsurvey.util;

//import com.mjict.signboardagency.database.DatabaseManager;
//import com.mjict.signboardagency.model.Setting;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Junseo
 *
 */
public class SettingDataManager {

	public static final int SHOP_CONDITON = 90;	// 업소, 간판 상태
	public static final int SHOP_CATEGORY = 60;	// 업종
	public static final int SIGN_TYPE = 40;		// 간판 종류
	public static final int LIGHT_TYPE = 35;		// 조명 종류
	public static final int RESULT = 50;			// 전수조사 결과

	public static final int SIGN_STATUS = 90;	// 업소, 간판 상태
	public static final int REVIEW_CODE = 10;	// 재조사 코드
	public static final int AREA_TYPE = 15;		// 지역 타입
	public static final int INSTALL_SIDE = 13;  // 설치 면
	public static final int UNIQUENESS = 14;	// 특이 사항

//	public static final int BUILDING_LOCATION =
	
//	public static final String SHOP_CONDITON = "90";	//
//	public static final String SHOP_CATEGORY = "60";	//
//	public static final String SIGN_TYPE = "40";		//
//	public static final String LIGHT_TYPE = "35";		//
//	public static final String RESULT = "50";			//

//	public static final int SIGN_CATEGORY = 51;
//	public static final int ATTACHED_LOCATION = 52;
//	public static final int CAB = 53;
//	public static final int SIDE_COUNT = 54;
//	public static final int FRAME = 55;
//	public static final int PANEL = 56;
//	public static final int DESIGN = 57;
//	public static final int FONT = 58;
//	public static final int SHAPE = 59;
//	public static final int

	
	private List<Setting> settings = null;
	private static SettingDataManager instance;

	private SettingDataManager() {
	}

	public static SettingDataManager getInstance() {
		if(instance == null)
			instance = new SettingDataManager();
		return instance;
	}

	public void load(Context context) {
		DatabaseManager dmgr = DatabaseManager.getInstance(context);
		settings = dmgr.getAllSetting();
	}


	public Setting getShopCondition(int code) {
		return searchSetting(SHOP_CONDITON, code);
	}

	public Setting getShopCategory(int code) {
		return searchSetting(SHOP_CATEGORY, code);
	}

	public Setting getSignType(int code) {
		return searchSetting(SIGN_TYPE, code);
	}

	public Setting getLightType(int code) {
		return searchSetting(LIGHT_TYPE, code);
	}

	public String getDefaultShopCategoty() {
		return "기타";
	}

	public String getDefaultLightTypeName() {
		return "비조명";		// TODO 나중에 상수로 묶어 놓든가 해
	}

	public String getDefaultResultName() {
		return "결과 모름";		// TODO 얘도 나중에 상의 해봐야 겠지
	}

	public String getDefaultSignTypeName() {
		return "가로형 간판";
	}

	public String getDefaultSignStatus() {
		return "지정된 값 없음";
	}

	public String getDefaultReviewName() {
		return "해당 사항 없음";
	}

	public String getDefaultShopStatusName() {
		return "지정되지 않음";
	}

	public Setting getResult(int code) {
		return searchSetting(RESULT, code);
	}

	public Setting[] getShopConditions() {
		return searchSetting(SHOP_CONDITON);
	}

	public Setting[] getShopCategories() {
		return searchSetting(SHOP_CATEGORY);
	}

	public Setting[] getSignTypes() {
		return searchSetting(SIGN_TYPE);
	}

//	public Setting[] get

	public Setting[] getLightTypes() {
		return searchSetting(LIGHT_TYPE);
	}

	public Setting[] getResults() {
		return searchSetting(RESULT);
	}

	public Setting[] getSignStatus() {
		return searchSetting(SIGN_STATUS);
	}

	public Setting getSignStatus(int code) {
		return searchSetting(SIGN_STATUS, code);
	}

	public Setting[] getReviewCodes() {
		return searchSetting(REVIEW_CODE);
	}

	public Setting getReviewCode(int code) {
		return searchSetting(REVIEW_CODE, code);
	}

	public Setting[] getAreaTypeCodes() {
		return searchSetting(AREA_TYPE);
	}

	public Setting getAreaTypeCode(int code) {
		return searchSetting(AREA_TYPE, code);
	}

	public Setting[] getInstallSides() {
		return searchSetting(INSTALL_SIDE);
	}

	public Setting getInstallSide(int code) {
		return searchSetting(INSTALL_SIDE, code);
	}

	public Setting[] getUniqueness() {
		return searchSetting(UNIQUENESS);
	}

	public Setting getUniqueness(int code) {
		return searchSetting(UNIQUENESS, code);
	}

//	public Setting[] getSignCategories() {
//		return searchSetting(SIGN_CATEGORY);
//	}
//
//	public Setting getSignCategory(int code) {
//		return searchSetting(SIGN_CATEGORY, code);
//	}
//
//	public Setting[] getAttachedLocations() {
//		return searchSetting(ATTACHED_LOCATION);
//	}
//
//	public Setting getAttachedLocation(int code) {
//		return searchSetting(ATTACHED_LOCATION, code);
//	}

//	public Setting[] getCabs() {
//		return searchSetting(CAB);
//	}
//
//	public Setting getCab(int code) {
//		return searchSetting(CAB, code);
//	}

//	public static final int CAB = 53;
//	public static final int SIDE_COUNT = 54;
//	public static final int FRAME = 55;
//	public static final int PANEL = 56;
//	public static final int DESIGN = 57;
//	public static final int FONT = 58;
//	public static final int SHAPE = 59;

	private Setting[] searchSetting(int category) {
		if(settings == null)
			return null;

		List<Setting> list = new ArrayList<>();
		for(int i=0; i<settings.size(); i++) {
			Setting s = settings.get(i);
			if(s.getCategory() == category)
				list.add(s);
		}

		Setting[] settings = new Setting[list.size()];
		return list.toArray(settings);
	}

	private Setting searchSetting(int category, int code) {
		if(settings == null)
			return null;

		for(int i=0; i<settings.size(); i++) {
			Setting s = settings.get(i);
			if(s.getCategory() == category && s.getCode() == code )
				return s;
		}

		return null;
	}
	
}
