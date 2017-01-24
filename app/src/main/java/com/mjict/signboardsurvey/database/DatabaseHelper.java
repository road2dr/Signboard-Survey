package com.mjict.signboardsurvey.database;

import android.app.Notification;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.Mobile;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.TownAddress;
import com.mjict.signboardsurvey.model.User;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private final static String DATABASE_NAME = "MROKGTotal.db3";
	private final static int DATABASE_VERSION = 1;
	
	private Dao<Sign, Long> signsDao;
	private Dao<User, String> userDao;
	private Dao<TownAddress, Void> addressDao;
	private Dao<StreetAddress, Void> streetAddressDao;
	private Dao<Building, Long> buildingDao;
	private Dao<Shop, Long> shopDao;
	private Dao<Setting, Void> settingDao;
	private Dao<BuildingPicture, Long> buildingPictureDao;
	private Dao<Mobile, Integer> mobileDao;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
//		try {

//			TableUtils.createTable(connectionSource, User.class);
//			TableUtils.createTable(connectionSource, Sign.class);
//			TableUtils.createTable(connectionSource, Building.class);
//			TableUtils.createTable(connectionSource, Shop.class);
//			TableUtils.createTable(connectionSource, Inspection.class);
//			TableUtils.createTable(connectionSource, StreetAddress.class);
//			TableUtils.createTable(connectionSource, Setting.class);
//			TableUtils.createTable(connectionSource, SignOwnership.class);
//			TableUtils.createTable(connectionSource, BuildingPicture.class);
//			TableUtils.createTable(connectionSource, Notification.class);
//			TableUtils.createTable(connectionSource, BuildingPicture.class);
//			TableUtils.createTable(connectionSource, Mobile.class);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.d("junseo", "DatabaseHelper - onUpgrade");
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Sign.class, true);
			TableUtils.dropTable(connectionSource, Building.class, true);
			TableUtils.dropTable(connectionSource, Shop.class, true);
			TableUtils.dropTable(connectionSource, StreetAddress.class, true);
			TableUtils.dropTable(connectionSource, TownAddress.class, true);
			TableUtils.dropTable(connectionSource, Setting.class, true);
			TableUtils.dropTable(connectionSource, BuildingPicture.class, true);
			TableUtils.dropTable(connectionSource, Notification.class, true);
			
//			TableUtils.dropTable(connectionSource, User.class, true);
//			TableUtils.dropTable(connectionSource, Sign.class, true);
//			TableUtils.dropTable(connectionSource, Building.class, true);
//			TableUtils.dropTable(connectionSource, Shop.class, true);
//			TableUtils.dropTable(connectionSource, Inspection.class, true);
//			TableUtils.dropTable(connectionSource, StreetAddress.class, true);
			
			onCreate(database, connectionSource);
			
//			Table
//			TableUtils.createTable(connectionSource, tableConfig)
//			ViewU
			
			// temp
//			createViewTable();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public Dao<Sign, Long> getSignDao() throws SQLException {
		if(signsDao == null) {
			signsDao = getDao(Sign.class);
		}
		
		return signsDao;
	}
	
	public Dao<User, String> getUserDao() throws SQLException {
		if(userDao == null) {
			userDao = getDao(User.class);
		}
		
		return userDao;
	}
	
	public Dao<TownAddress, Void> getTownForInspectionDao() throws SQLException {
		if(addressDao == null) {
			addressDao = getDao(TownAddress.class);
		}
		
		return addressDao;
	}
	
	public Dao<StreetAddress, Void> getStreetAddressDao() throws SQLException {
		if(streetAddressDao == null) {
			streetAddressDao = getDao(StreetAddress.class);
		}
		
		return streetAddressDao;
	}
	
	public Dao<Building, Long> getBuildingDao() throws SQLException {
		if(buildingDao == null)
			buildingDao = getDao(Building.class);
		
		return buildingDao;
	}
	
	public Dao<Shop, Long> getShopDao() throws SQLException {
		if(shopDao == null)
			shopDao = getDao(Shop.class);
		
		return shopDao;
	}
	
	public Dao<Setting, Void> getSettingDao() throws SQLException {
		if(settingDao == null)
			settingDao = getDao(Setting.class);
		
		return settingDao;
	}
	
	public Dao<BuildingPicture, Long> getBuildingPictureDao() throws SQLException {
		if(buildingPictureDao == null)
			buildingPictureDao = getDao(BuildingPicture.class);
		
		return buildingPictureDao;
	}
	
	public Dao<Mobile, Integer> getMobileDao() throws SQLException {
		if(mobileDao == null)
			mobileDao = getDao(Mobile.class);
		
		return mobileDao;
	}
	
//	private void createViewTable() {
//		String sqlSentence = "CREATE VIEW "
//	}
}
