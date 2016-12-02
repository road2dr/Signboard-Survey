package com.mjict.signboardsurvey.database;

import android.content.Context;

import com.mjict.signboardsurvey.model.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Junseo
 * TODO 나중에 MJContext로 이동 시켜야 할 듯
 */
public class SettingDataManager {

	// TODO 나중에 간판 상태/업소 상태 변경되면 수정 해야함
	// 
	public static final int SHOP_CONDITON = 95;	//
	public static final int SHOP_CATEGORY = 60;	//
	public static final int SIGN_TYPE = 40;		//
	public static final int LIGHT_TYPE = 35;		//
	public static final int RESULT = 50;			//

	public static final int SIGN_STATUS = 105;
	public static final int REINSPECTION_CODE = 115;
	public static final int AREA_TYPE = 15;
	
//	public static final String SHOP_CONDITON = "90";	//
//	public static final String SHOP_CATEGORY = "60";	//
//	public static final String SIGN_TYPE = "40";		//
//	public static final String LIGHT_TYPE = "35";		//
//	public static final String RESULT = "50";			//

	
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

	public String getDefaultLightTypeName() {
		return "비조명";		// TODO 나중에 상수로 묶어 놓든가 해
	}

	public String getDefaultResultName() {
		return "결과 모름";		// TODO 얘도 나중에 상의 해봐야 겠지
	}

	public String getDefaultSignTypeName() {
		return "가로형 간판";
	}

	public String getDefaultReviewName() {
		return "해당 사항 없음";
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
		return searchSetting(REINSPECTION_CODE);
	}

	public Setting getReviewCode(int code) {
		return searchSetting(REINSPECTION_CODE, code);
	}

	public Setting[] getAreaTypeCodes() {
		return searchSetting(AREA_TYPE);
	}

	public Setting getAreaTypeCode(int code) {
		return searchSetting(AREA_TYPE, code);
	}


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
