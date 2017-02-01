package com.mjict.signboardsurvey.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import com.mjict.signboardsurvey.autojudgement.AutoJudgementRule;
import com.mjict.signboardsurvey.autojudgement.AutoJudgementRuleManager;
import com.mjict.signboardsurvey.autojudgement.Condition;
import com.mjict.signboardsurvey.autojudgement.InputType;
import com.mjict.signboardsurvey.autojudgement.Rule;
import com.mjict.signboardsurvey.autojudgement.Type;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.AutoJudgementValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import magick.ImageInfo;
import magick.MagickImage;

public class Utilities {

	private static final String FIRST_TIME_CHECK_FILE = "sign";

	public static boolean checkRootingFiles() {
		final String ROOT_PATH = Environment.getExternalStorageDirectory() + "";
		final String ROOTING_PATH_1 = "/system/bin/su";
		final String ROOTING_PATH_2 = "/system/xbin/su";
		final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
		final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";

		String[] rootFilesPath = new String[]{
				ROOT_PATH + ROOTING_PATH_1 ,
				ROOT_PATH + ROOTING_PATH_2 ,
				ROOT_PATH + ROOTING_PATH_3 ,
				ROOT_PATH + ROOTING_PATH_4
		};

		boolean isRootingFlag = false;

		try {
			Runtime.getRuntime().exec("su");
			isRootingFlag = true;
		} catch ( Exception e) {
			// Exception 나면 루팅 false;
			isRootingFlag = false;
		}

		if(isRootingFlag == false){
			for(int i=0; i<rootFilesPath.length; i++) {
				File file = new File(rootFilesPath[i]);
				if(file.exists() == true) {
					isRootingFlag = true;
					break;
				}
			}
		}

		return isRootingFlag;
	}

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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREAN);
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREAN);
		String date = format.format(time);

		return date;
	}

	public static Date stringToDate(String time) {
		DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREAN);
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
		long hash = 53;

		for(int i=0; i<str.length(); i++)
			hash = ((hash << 5) + hash) + str.charAt(i); /* hash * 33 + c */

		return hash;
	}

//	public static void updateInputValue(JudgementValue... values) {
//		if(values == null)
//			return;
//
//		// 입력값 업데이트
//		for(int i=0; i<values.length; i++) {
//			JudgementValue value = values[i];
//			AutoJudgementRuleManager.putInputValue(value.type.getName(), value.value);
//		}
//	}

//	public static void initInputValue() {
//
//		AutoJudgementRuleManager.initInputValue();
//
//	}

	public static int autoJudgement(int signType, AutoJudgementValue value) {
		AutoJudgementRule signRule = AutoJudgementRuleManager.findAutoJudgementRule(signType);
		if(signRule == null)	// 해당 간판을 위한 룰 정보가 없음
			return -1;

		// 각 값을 룰에 대입 함
		int n = signRule.sizeOfRules();
		for(int i=0; i<n; i++) {
			Rule rule = signRule.getRule(i);
			int soc = rule.sizeOfConditions();
			for(int j=0; j<soc; j++) {
				Condition cond = rule.getCondition(j);
				Type valueType = cond.getValueType();

				int conditionValue = -1;
				InputType inputType = InputType.get(valueType.getName());
				Integer tv = value.getValue(inputType);
				if(tv != null)
					conditionValue = tv;

				cond.setInputValue(conditionValue);
			}
		}

		// 조건에 맞는 룰 찾기
		List<Rule> rules = signRule.getRules();
		Collections.sort(rules, new RuleComparator());
		int judgement = signRule.getDefaultResult();
		for(int i=0; i<rules.size(); i++) {
			Rule rule = rules.get(i);
			if(rule.checkConditions()) {
				judgement = rule.getResult();
				break;
			}
		}

		return judgement;
	}

	public static Bitmap loadImage(String path, int sampleSize) {
		File file = new File(path);
		Bitmap image = null;
		if (file.exists()) {
			try {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = sampleSize;

//				FileInputStream fis = new FileInputStream(path);
//				BufferedInputStream bis = new BufferedInputStream(fis, 1024 * 8);
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				int len=0;
//
//				byte[] buffer = new byte[1024];
//				while((len = bis.read(buffer)) != -1){
//					out.write(buffer, 0, len);
//				}
//				out.close();
//				bis.close();
//
////				image = BitmapFactory.decodeFile(path, opt);
//				byte[] data = out.toByteArray();
//				image = BitmapFactory.decodeByteArray(data, 0, data.length, opt);


//				image = BitmapFactory.decodeStream(new FlushedInputStream(fis));

				ImageInfo i = new ImageInfo(path);
				MagickImage m = new MagickImage(i);


				image = MagickBitmap.ToBitmap(m, sampleSize);

			} catch (Exception e) {
				e.printStackTrace();
				image = null;
			}
		}

		return image;
	}

	// 오름차순 정렬
	public static class RuleComparator implements Comparator<Rule> {
		@Override
		public int compare(Rule lhs, Rule rhs) {
			return lhs.getOrder() < rhs.getOrder() ? -1 : lhs.getOrder() > rhs.getOrder() ? 1:0;
		}
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int read = read();
					if (read < 0) {
						break;  // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
}
