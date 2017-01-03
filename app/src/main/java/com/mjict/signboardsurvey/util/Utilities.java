package com.mjict.signboardsurvey.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.mjict.signboardsurvey.model.Address;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utilities {

	private static final String FIRST_TIME_CHECK_FILE = "sign";


	public static boolean isFirstTimeLaunch(Context context) throws IOException {
		
		File file = context.getFileStreamPath(FIRST_TIME_CHECK_FILE);
		if(file.exists() == false)
			return true;
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String value = br.readLine();
		br.close();
		
		return Boolean.valueOf(value);
	}
	
	public static void setFalseToFirstTimeLaunch(Context context) {
		File file = context.getFileStreamPath(FIRST_TIME_CHECK_FILE);
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		
			byte[] data = "false".getBytes();
			fos.write(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			file.delete();
		} catch (IOException ex) {
			ex.printStackTrace();
			file.delete();
		}
				
		try {
			if(fos != null)
				fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static boolean isAfterFromLastSyncDate(Sign sign) {
//		if(sign == null)
//			return false;
//
//		String inputDateStr = sign.getInputDate();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss", Locale.KOREAN);
//		Date inputDate = null;
//		try {
//			inputDate = format.parse(inputDateStr);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		if(inputDate == null)
//			return false;
//
//		Date lastSyncDate = Configuration.getLastSynchronizeDate();
//
//		boolean answer = inputDate.after(lastSyncDate);
//		if(answer == true)
//			return true;
//
//		String modiDateStr = sign.getModifyDate();
//		Date modiDate = null;
//		try {
//			modiDate = format.parse(modiDateStr);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		if(modiDate == null)
//			return false;
//
//		return modiDate.after(lastSyncDate);
//	}

	public static Address getAddressFromLocation(Context context, Location location) {
		Geocoder geoCoder = new Geocoder(context);
		double lat = location.getLatitude();
		double lng = location.getLongitude();

		List<android.location.Address> addresses = null;

		try {
			addresses = geoCoder.getFromLocation(lat, lng, 5);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

//		geoCoder.getFromLocationName()

		// TODO 주소 검색하면 후보로 여러개 나오는데 주소가 정확하지 않으므로 이걸 다 써야 할듯

		Address address = null;
		if(addresses.size() > 0) {
			android.location.Address a = addresses.get(0);

			int idx = a.getMaxAddressLineIndex();
			for(int i=0; i<idx; i++) {
				String adl = a.getAddressLine(i);
				Log.d("junseo", "address line: "+i+": "+adl);
			}

			String contry = a.getCountryName();				// 대한민국		// 대한민국
			String adminArea = a.getAdminArea();			// 서울특별시		// 인천광역시		// 인천광역시
			String locality = a.getLocality();				// 강남구		// 남동구		// 남동구
			String subLocality = a.getSubLocality();		// null			// null			// null
			String premise = a.getPremises();				// null							// null
			String thoroughfare = a.getThoroughfare();		// 언주로87길		// 구월3동		// 구월남로
			String subthoroughfare = a.getSubThoroughfare();				// 1091-3		// 258
			String sa = a.getSubAdminArea();				// null
			String featureName = a.getFeatureName();		// 44			// 1091-3		// 258

			// feature name 이 있으면 지번 주소
			// subThroughfare 이 있으면 도로명 주소

			Log.d("junseo", "address: "+contry+" "+adminArea+" "+locality+" "+subLocality+" "+thoroughfare+" "+featureName+" "+premise+" "+subthoroughfare+" "+sa);
//			address = contry+" "+locality+" "+subLocality+" "+thoroughfare+" "+featureName;
//			대한민국 경기도 성남시 수정구 산성동 1700 null 1700 null
//			address = new SimpleAddress(adminArea, locality, thoroughfare, featureName);

//			int type = (featureName != null) ? SimpleAddress.ADDRESS_TYPE_HOUSE_NUMBER : SimpleAddress.ADDRESS_TYPE_STREET;
			String county = (subLocality == null) ? locality : locality+" "+subLocality;
//			String town = (type == SimpleAddress.ADDRESS_TYPE_HOUSE_NUMBER) ? thoroughfare : null;
//			String street = (type == SimpleAddress.ADDRESS_TYPE_STREET) ? thoroughfare : null;
//			String houseNumber = (type == SimpleAddress.ADDRESS_TYPE_HOUSE_NUMBER) ? featureName : null;

//			String firstHouseNumber = null;
//			String secondHouseNumber = null;
//			if(featureName != null && type == SimpleAddress.ADDRESS_TYPE_STREET) {
//				String[] split = featureName.split("-");
//				firstHouseNumber = split[0];
//				if(split.length >= 1)
//					secondHouseNumber = split[1];
//			}


//			int type, String province, String county, String town, String street, String firstBuildingNumber, String secondBuildingNumber, String houseNumber

			address = new Address(adminArea, county, null, null, null, null, null, null);

		}

		return address;
	}

	public static float stringToFloat(String value) throws WrongNumberFormatException {
		float result = -1;
		try {
			result = Float.valueOf(value);
		}catch(NumberFormatException e) {
			throw new WrongNumberFormatException(value);
		}

		return result;
	}

	public static int stringToInt(String value) throws WrongNumberFormatException {
		int result = -1;
		try {
			result = Integer.valueOf(value);
		}catch(NumberFormatException e) {
			throw new WrongNumberFormatException(value);
		}

		return result;
	}

//	Calendar cal = new GregorianCalendar(Locale.KOREA);
//	cal.setTime(new Date());
//	cal.add(Calendar.YEAR, 1); // 1년을 더한다.
//	cal.add(Calendar.MONTH, 1); // 한달을 더한다.
//	cal.add(Calendar.DAY_OF_YEAR, 1); // 하루를 더한다.
//	cal.add(Calendar.HOUR, 1); // 시간을 더한다
//
//	SimpleDateFormat fm = new SimpleDateFormat(
//			"yyyy-MM-dd HH시mm분ss초");
//	String strDate = fm.format(cal.getTime());
//
	public static String getCurrentTimeAsString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss", Locale.KOREAN);
		String date = format.format(Calendar.getInstance().getTime());

		return date;
	}

	/**
	 * 오늘 날짜 기준으로 day 일 이후(혹은 이전)의 시작 시간(0시 0분 0초) 값을 구한다.
	 *
	 * @param day
	 * @return
     */
	public static Calendar getStartTimeBeforeDays(int day) {
		Calendar cal1 = Calendar.getInstance();

		cal1.add(Calendar.DATE, day);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);

		return cal1;
	};

	public static Calendar getStartTimeBeforeMonths(int month) {
		Calendar cal1 = Calendar.getInstance();

		cal1.add(Calendar.MONTH, month);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);

		return cal1;
	};

	public static String toTimeString(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss", Locale.KOREAN);
		String date = format.format(time);

		return date;
	}

	public static Date stringToDate(String time) {
		DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss", Locale.KOREAN);
		Date date = null;
		try {
			date = sdFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			date = null;
		}

		return date;
	}

	public static long hash(String str) {
		long hash = 5381;

		for(int i=0; i<str.length(); i++)
			hash = ((hash << 5) + hash) + str.charAt(i); /* hash * 33 + c */

		return hash;
	}

//	int areaType, int areaCode, int signType, int signStatus, float width,
//	float length, float height, boolean isFront, boolean isIntersection,
//	int lightType, int placedFloor, int totalFloor, int signCount

//	SparseArray<AutoInspectionRule> aa;
//	private static AutoInspectionRule autoInspectionRule = null;

//	ArrayList<AutoInspectionRule> inspectionRules = null;


//	private static SparseArray<List<AutoInspectionRule>> inspectionRules;
//	private static List<AutoInspectionRule> defaultInspectionRules;
//
//	public static void loadInspectionRules() {		// TODO 초기화 할때 로드 해둬
//
//		inspectionRules = new SparseArray<>();
//
//		SettingDataManager sdm = SettingDataManager.getInstance();
//		Setting[] allSignTypes = sdm.getSignTypes();
//		ArrayList<Setting> fixedSignTypes = new ArrayList<>();
//		for(int i=0; i<allSignTypes.length; i++) {
//			Setting s = allSignTypes[i];
//			if(s.getGroup() == 1)	// 고정 광고물만 사용
//				fixedSignTypes.add(s);
//		}
//
//		for(int i=0; i<fixedSignTypes.size(); i++) {
//			Setting s = fixedSignTypes.get(i);
//			String xmlFileName = String.format(Configuration.getDirectoryForInspectionRule()+"inspection_rule_%d.xml", s.getCode());
//			List<AutoInspectionRule> rule = AutoInspectionRule.fromXml(xmlFileName);
//			inspectionRules.put(s.getCode(), rule);
//		}
//
//		String defaultRuleFileName = String.format(Configuration.getDirectoryForInspectionRule()+"inspection_rule_%d.xml", 0);
//		defaultInspectionRules = AutoInspectionRule.fromXml(defaultRuleFileName);
//	}
//
//
//	public static int autoInspection(int areaType, int signType, int signCount, int placedFloor, float width, float length,
//									 boolean isIntersection, boolean isFrontBackRoad, int lightType) {
//
//		if(inspectionRules == null && defaultInspectionRules == null)	// 자동 판단 파일 없음
//			return 3;	// TODO 자동 판단 파일이 없을 시 디폴트 값을 설정 해야 함.
//
//		List<AutoInspectionRule> rules = inspectionRules.get(signType);
//		if(rules == null)
//			rules = defaultInspectionRules;
//
//		if(rules == null)
//			return 3;
//
//		AutoInspectionRule rule = null;
//		AutoInspectionRule defaultRule = null;
//		for(int i=0; i<rules.size(); i++) {
//			AutoInspectionRule r = rules.get(i);
//
//			if(r.getAreaType() == areaType)
//				rule = r;
//
//			if(r.getAreaType() == 99)	// 99 => 지정 되지 않은 구역. 디폴트 지역
//				defaultRule = r;		// 그러니깐 모든 파일은 area_type 이 99 인 룰을 기본적으로 포함하고 있어야 한다.
//		}
//
//		if(rule == null)
//			rule = defaultRule;
//
//		if(rule == null)
//			return 3;
//
//		int defaultResult = rule.getDefaultInspectionResult();		// 기본 전수조사 결과. xml 에 명시된 값에 해당하지 않으면 이걸로 결과를 리턴
//
//		// 갯수 검사
//		int availableCount = rule.getBasicAvailableCount();
//		if(isIntersection && isFrontBackRoad )
//			availableCount = availableCount + rule.getBothAdditionCount();
//		else {
//			if(isIntersection)
//				availableCount = availableCount + rule.getIntersectionAdditionCount();
//			else if(isFrontBackRoad)
//				availableCount = availableCount + rule.getFrontBackRoadAdditionCount();
//		}
//
//		if(signCount > availableCount)
//			return 8;	// 요건불비(수량초과)
//
//		// 조명에 따른 위치 및 규격 검사
//		// 조명 종류에 해당하는 룰을 찾아야 한다.
//		AutoInspectionRule.LightRule lightRule = null;
//		AutoInspectionRule.LightRule defaultLightRule = null;
//		for(int i=0; i<rule.getLightRules().size(); i++) {
//			AutoInspectionRule.LightRule l = rule.getLightRules().get(i);
//			if(l.code == lightType)
//				lightRule = l;
//			if(l.code == 0)				// 0 => 지정 되지 않은 조명 타입. 디폴트 조명
//				defaultLightRule = l;	// light code 가 0인 룰은 기본적으로 포함하고 있어야 한다.
//		}
//
//		if(lightRule == null)
//			lightRule = defaultLightRule;
//
//		if(lightRule == null)
//			return defaultResult;
//
//		// 설치 장소 검사
//		if(placedFloor > lightRule.maxAvailablePlacedFloor || placedFloor < lightRule.minAvailablePlacedFloor)
//			return 9;	// 요건불비(위치장소위반)
//
//		// 규격 - 면적 검사
//		float area = length * width;
//		for(int i=0; i<lightRule.areaRules.size(); i++) {
//			AutoInspectionRule.MinMaxRule r = lightRule.areaRules.get(i);
//			if(area > r.min && area <= r.max)
//				return r.result;
//		}
//
//		// 규격 - 세로 검사
//		for(int i=0; i<lightRule.lengthRules.size(); i++) {
//			AutoInspectionRule.MinMaxRule r = lightRule.lengthRules.get(i);
//			if(length > r.min && length <= r.max)
//				return r.result;
//		}
//
//		// 규격 - 가로 검사
//		for(int i=0; i<lightRule.widthRules.size(); i++) {
//			AutoInspectionRule.MinMaxRule r = lightRule.widthRules.get(i);
//			if(width > r.min && width <= r.max)
//				return r.result;
//		}
//
//		// 위 조건에 모두 해당 사항이 없으면 디폴드 결과 갑으소
//		return defaultResult;
//	}

	public static Bitmap loadImage(String path, int sampleSize) {
		File file = new File(path);
		Bitmap image = null;
		if (file.exists()) {
			try {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = sampleSize;
				image = BitmapFactory.decodeFile(path, opt);
			} catch (Exception e) {
				e.printStackTrace();
				image = null;
			}
		}

		return image;
	}
}
