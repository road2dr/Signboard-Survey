package com.mjict.signboardsurvey.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;

public class SyncConfiguration {

	private static String version = null;
	private static String baseDir = null;
	private static boolean dataChanged = false;

	private static String signPictureDir = null;
	private static String buildingPictureDir = null;

	private static String databaseFileName = null;
	private static String databaseFileForSync = null;
	private static String mapFileDir = null;
	private static float laserMeasurementErrorFactor = -1;
	private static int mobileNo = -1;
	private static long lastInspectionNo = -1;
	private static String tempDir = null;
	private static String inspectionRuleDir = null;
	private static Date lastSyncDate = null;
	private static String provinceForSync = null;
	private static String countyForSync = null;
	private static String townForSync = null;
	private static boolean dataModified = false;
	
//	database_file_name=MROKGTotal.db3
//			database_file_for_sync=database/MROKGTotal.db3
//			temp_dir=temp/
//			sign_picture_dir=pic/sign/
//			building_pciture_dir=pic/building/
//			map_dir=mapFiles/database_file_name=MROKGTotal.db3
//					database_file_for_sync=database/MROKGTotal.db3
//					temp_dir=temp/
//					sign_picture_dir=pic/sign/
//					building_pciture_dir=pic/building/
//					map_dir=mapFiles/
	
	public static final String PROPERTY_FILE_NAME="signboard.properties";
	
	// default values
	public static final String DEFAULT_BASE_DIRECTORY="mjict/signboard/";
	public static final String DEFAULT_DATABASE_FILE_NAME="MROKGTotal.db3";
	public static final String DEFAULT_DATABASE_FILE_FOR_SYNC="database/MROKGTotal.db3";
	public static final String DEFAULT_SERVER_SIGN_PIC_DIRECTORY="pic/server/sign/";
	public static final String DEFAULT_SERVER_BUILDING_PIC_DIRECTORY="pic/server/building/";
	public static final String DEFAULT_DEVICE_SIGN_PIC_DIRECTORY="pic/device/sign/";
	public static final String DEFAULT_DEVICE_BUILDING_PIC_DIRECTORY="pic/device/building/";
	public static final String DEFAULT_MAP_FILE_DIRECTORY="mapFiles/";
	public static final String DEFAULT_TEMPORARY_DIRECTORY="temp/";
	
	public static final String DEFAULT_VERSION="1.0.0";
	
	public static final String DEFAULT_MOBILE_NO="1";
	public static final String DEFAULT_LASER_DISTANR_ERROR_FACTOR="0.9096";
	
//	public static final String DEfAULT_BASE_DIRECTORY="mjict/signboard/";
	
	
	private static final String VERSION = "version";
	private static final String DATA_CHANGED = "data_changed";
	private static final String DIRECTORY_FOR_BASE = "base_dir";
	private static final String DIRECTORY_FOR_SIGN_PICTURE_DEVICE = "device_sign_picture_dir";
	private static final String DIRECTORY_FOR_BUILDING_PICTURE_DEVICE = "device_building_pciture_dir";
	private static final String DIRECTORY_FOR_SIGN_PICTURE_SERVER = "server_sign_picture_dir";
	private static final String DIRECTORY_FOR_BUILDING_PICTURE_SERVER = "server_building_pciture_dir";
	private static final String DATABASE_FILE_NAME = "database_file_name";
	private static final String DATABASE_FILE_NAME_FOR_SYNC = "database_file_for_sync";
	private static final String DIRECTORY_FOR_MAP_FILES="map_dir";
	private static final String MOBILE_NO="mobile_no";
	private static final String LAST_INSPECTION_NO = "last_inspection_no";
	private static final String TEMP_DIR = "temp_dir";
	private static final String INSPECTION_RULE_DIR = "inspection_rule_dir";
	private static final String LAST_SYNC_DATE = "last_sync_date";
	private static final String PROVINCE_FOR_SYNC = "province_for_sync";
	private static final String COUNTY_FOR_SYNC = "county_for_sync";
	private static final String TOWN_FOR_SYNC = "town_for_sync";
	private static final String DATA_MODIFIED = "database_modified";

	private static final String LAZER_DISTANCE_MEASUREMENT_ERROR_FACOTR = "laser_distance_measurement_error_factor";
	
	private static Properties properties = new Properties();
	
//	private static User currentUser;
	
	public static boolean makePropertyFile() {
		if(properties.contains(DIRECTORY_FOR_BASE) == false)
			properties.put(DIRECTORY_FOR_BASE, DEFAULT_BASE_DIRECTORY);
		if(properties.contains(DATABASE_FILE_NAME) == false)
			properties.put(DATABASE_FILE_NAME, DEFAULT_DATABASE_FILE_NAME);
		if(properties.contains(DATABASE_FILE_NAME_FOR_SYNC) == false)
			properties.put(DATABASE_FILE_NAME_FOR_SYNC, DEFAULT_DATABASE_FILE_FOR_SYNC);
		if(properties.contains(DIRECTORY_FOR_SIGN_PICTURE_DEVICE) == false)
			properties.put(DIRECTORY_FOR_SIGN_PICTURE_DEVICE, DEFAULT_DEVICE_SIGN_PIC_DIRECTORY);
		if(properties.contains(DIRECTORY_FOR_BUILDING_PICTURE_DEVICE) == false)
			properties.put(DIRECTORY_FOR_BUILDING_PICTURE_DEVICE, DEFAULT_DEVICE_BUILDING_PIC_DIRECTORY);
		if(properties.contains(DIRECTORY_FOR_SIGN_PICTURE_SERVER) == false)
			properties.put(DIRECTORY_FOR_SIGN_PICTURE_SERVER, DEFAULT_SERVER_SIGN_PIC_DIRECTORY);
		if(properties.contains(DIRECTORY_FOR_BUILDING_PICTURE_SERVER) == false)
			properties.put(DIRECTORY_FOR_BUILDING_PICTURE_SERVER, DEFAULT_SERVER_BUILDING_PIC_DIRECTORY);
		if(properties.contains(DIRECTORY_FOR_MAP_FILES) == false)
			properties.put(DIRECTORY_FOR_MAP_FILES, DEFAULT_MAP_FILE_DIRECTORY);
		if(properties.contains(TEMP_DIR) == false)
			properties.put(TEMP_DIR, DEFAULT_TEMPORARY_DIRECTORY);

		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public static boolean makeDirectories() {

		String base = FileManager.getExternalSDPath(DEFAULT_BASE_DIRECTORY);
		File baseFile = new File(base);
		boolean answer = false;
		if(baseFile.exists() == false)
			answer = baseFile.mkdirs();
		else
			answer = true;
		
		if(answer == false)
			return false;
		
		String[] dirs = {
			getDirectoryForBuildingPicture(true),
			getDirectoryForBuildingPicture(false),
			getDirectoryForSingPicture(true),
			getDirectoryForSingPicture(false),
			getDirectoryMapFiles(),
			getDirectoryForTemp(),
		};
		for(int i=0; i<dirs.length; i++) {
			File dir = new File(dirs[i]);

			if(dir.exists() == false)
				answer = dir.mkdirs();
			else
				answer = true;
			
			if(answer == false)
				return false;
		}		
		return true;
	}
	
	public static boolean hasPropertyFile() {
		String base = FileManager.getExternalSDPath(DEFAULT_BASE_DIRECTORY);
		String propertyFilePath = base+PROPERTY_FILE_NAME;
		File file = new File(propertyFilePath);
		
		return file.exists();
	}
	
	public static boolean hasDatabaseFileFoySync() {
		String fname = getDatabaseFileNameForSync();
		File file = new File(fname);
		
		return file.exists();
	}
	
	public static boolean load() {
		String base = FileManager.getExternalSDPath(DEFAULT_BASE_DIRECTORY);
		String propertyFilePath = base+PROPERTY_FILE_NAME;
		File file = new File(propertyFilePath);
		
		if(file.exists() == false)
			return false;

		try {
			FileInputStream fis = new FileInputStream(file); 
			properties.load(fis);
			fis.close();
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void save() throws IOException {
		String base = getBaseDirectory();
		String propertyFilePath = base+PROPERTY_FILE_NAME;
		FileOutputStream fos = new FileOutputStream(propertyFilePath);
		
		properties.store(fos, null);
	}
	
	public static String getVersion() {
		if(version == null)
			version = properties.getProperty(VERSION);
		
		return version;
	}
	
	public static String getBaseDirectory() {
		if(baseDir == null)
			baseDir = properties.getProperty(DIRECTORY_FOR_BASE);
		
		String base = FileManager.getExternalSDPath(baseDir);
		
		return base;
	}
	
	public static boolean getDataChaged() {
		dataChanged = Boolean.valueOf(properties.getProperty(DATA_CHANGED));
		
		return dataChanged;
	}
	
	public static void setDataChanged(boolean value) {
		properties.put(DATA_CHANGED, String.valueOf(value));
	}

	public static String getDirectoryForInspectionRule() {
		if(inspectionRuleDir == null)
			inspectionRuleDir = properties.getProperty(INSPECTION_RULE_DIR);

		String base = getBaseDirectory();

		return base+inspectionRuleDir;
	}
	
	public static String getDirectoryForTemp() {
		if(tempDir == null)
			tempDir = properties.getProperty(TEMP_DIR);
		
		String base = getBaseDirectory();
		
		return base+tempDir;
	}
	
	public static String getDirectoryForSingPicture(boolean isSync) {
		String property = isSync ? DIRECTORY_FOR_SIGN_PICTURE_SERVER : DIRECTORY_FOR_SIGN_PICTURE_DEVICE;

		signPictureDir = properties.getProperty(property);
		
		String base = getBaseDirectory();
		
		return base+signPictureDir;
	}
	
	public static String getDirectoryForBuildingPicture(boolean isSync) {
		String property = isSync ? DIRECTORY_FOR_BUILDING_PICTURE_SERVER : DIRECTORY_FOR_BUILDING_PICTURE_DEVICE;

		buildingPictureDir = properties.getProperty(property);
			
		String base = getBaseDirectory();
		
		return base+buildingPictureDir;
	}
	
	public static String getDatabaseFileName() {
		if(databaseFileName == null)
			databaseFileName = properties.getProperty(DATABASE_FILE_NAME);
		
		return databaseFileName;
	}
	
	public static String getDatabaseFileNameForSync() {
		if(databaseFileForSync == null)
			databaseFileForSync = properties.getProperty(DATABASE_FILE_NAME_FOR_SYNC);
		
		String base = getBaseDirectory();
		
		return base+databaseFileForSync;
	}
	
	public static String getDirectoryMapFiles() {
		if(mapFileDir == null)
			mapFileDir = properties.getProperty(DIRECTORY_FOR_MAP_FILES);
		
		String base = getBaseDirectory();
		
		return base+mapFileDir;
	}

	
	public static float getLazerDistanceMeasurementErrorFactor() {
		if(laserMeasurementErrorFactor == -1) {
			try{
				laserMeasurementErrorFactor = Float.valueOf(properties.getProperty(LAZER_DISTANCE_MEASUREMENT_ERROR_FACOTR));
			}catch(Exception e) {
				e.printStackTrace();
				laserMeasurementErrorFactor = 1.0f;
			}
		}
		
		return laserMeasurementErrorFactor;
	}
	
	public static String getTempDirectory() {
		if(tempDir == null)
			tempDir = properties.getProperty(TEMP_DIR);
		
		String base = getBaseDirectory();
		
		return base+tempDir;
	}
	
	public static int getMobileNo() {
		if(mobileNo == -1) {
			String strValue = (String) properties.get(MOBILE_NO);
			try {
				mobileNo = Integer.valueOf(strValue);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return mobileNo;
	}

	public static long getLastInspectionNo() {
		if(lastInspectionNo == -1) {
			try {
				String strValue = (String) properties.get(LAST_INSPECTION_NO);
				lastInspectionNo = Long.valueOf(strValue);
			}catch(Exception ex) {
				ex.printStackTrace();
				lastInspectionNo = -1;
			}
		}

		return lastInspectionNo;
	}
	
	public static void setLastInspectionNo(long no) {
		lastInspectionNo = no;
		properties.put(LAST_INSPECTION_NO, String.valueOf(lastInspectionNo));
	}
	
	public static Date getLastSynchronizeDate() {
		if(lastSyncDate == null) {
			String strValue = (String) properties.get(LAST_SYNC_DATE);
			try {				
				if(strValue == null) {
					lastSyncDate = new GregorianCalendar(2016, 1, 1, 9, 0, 0).getGregorianChange();
				} else {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
					lastSyncDate = format.parse(strValue);
				}
			}catch(Exception e) {
				e.printStackTrace();
				lastSyncDate = null;
			}
		}
		
		return lastSyncDate;
	}
	
	public static String getProvinceForSync() {
		if(provinceForSync == null) {
			try {
				provinceForSync = new String(properties.getProperty(PROVINCE_FOR_SYNC).getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				provinceForSync = null;
			}
		}
		
		return provinceForSync;
	}
	
	public static String getCountyForSync() {
		if(countyForSync == null) {
			try {
				countyForSync = new String(properties.getProperty(COUNTY_FOR_SYNC).getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				countyForSync = null;
			}
		}
		
		return countyForSync;
	}
	
	public static String getTownForSync() {
		if(townForSync == null) {
			if(townForSync == null) {
				try {
					townForSync = new String(properties.getProperty(TOWN_FOR_SYNC).getBytes("ISO-8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					townForSync = null;
				}
			}
		}
		
		return townForSync;
	}
	
	public static boolean getDataModified() {
		dataModified = Boolean.valueOf(properties.getProperty(DATA_MODIFIED));
		
		return dataModified;
	}
	
	public static void setDataModified(boolean value) {
		properties.put(DATA_MODIFIED, String.valueOf(value));
	}

	public static InspectionCondition[] getConditionCheckOrder() {
		// TODO  프로퍼티 혹은 디비 혹은 그밖의 방법을 이용해 값을 읽어 온다.
		// 일단은 임시로

		InspectionCondition[] conditions = new InspectionCondition[3];
		conditions[0] = InspectionCondition.COUNT_CONDITION;
		conditions[1] = InspectionCondition.LOCATION_CONDITION;
		conditions[2] = InspectionCondition.SIZE_CONDITION;

		return conditions;
	}


}
