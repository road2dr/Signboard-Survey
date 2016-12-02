package com.mjict.signboardsurvey.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.Inspection;
import com.mjict.signboardsurvey.model.Mobile;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.SignOwnership;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.TownForInspection;
import com.mjict.signboardsurvey.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * �� ���α׷����� DB ������ ��ȸ, ����, ������ ���� ����� ��Ƶ� Ŭ����. <br>
 *
 * @author Junseo
 *
 */
public class DatabaseManager {

	private static DatabaseManager instance;
	private DatabaseHelper helper;
	
	private DatabaseManager(Context context) {
		helper = new DatabaseHelper(context);
	}
	
	public static DatabaseManager getInstance(Context context) {
		// TODO context 가 activity 타입이면 Exception 발생 시키게 수정
		if(instance == null)
			instance = new DatabaseManager(context);
		return instance;
	}
	
	public DatabaseHelper getHelper() {
		return helper;
	}

	public long getAllSignsCount() {

		long numRows = 0;
		try {
			Dao<Sign, Long> signDao = helper.getSignDao();
			numRows = signDao.countOf();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return numRows;
	}

	public long getUserSignsCount(String id) {
		long numRows = 0;
		try {
			Dao<Sign, Long> signDao = helper.getSignDao();
			numRows = signDao.queryBuilder().where().eq("inputor", id).countOf();
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return numRows;
	}

	/**
	 * ��� Sign �����͸� ��ȸ.
	 * @return Sign ����Ʈ, ���� �߻��� null
	 */
	public List<Sign> getAllSigns() {
		List<Sign> list = null;
		try {
			list = helper.getSignDao().queryForAll();			
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return list;
	}
	
	/**
	 * ��� Shop �����͸� ��ȸ.
	 * @return Shop ����Ʈ, ���� �߻��� null
	 */
	public List<Shop> getAllShops() {
		List<Shop> list = null;		
		try {
			list = helper.getShopDao().queryForAll();			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * ��� ����� �����͸� ��ȸ.
	 * @return User ����Ʈ, ���� �߻��� null
	 */
	public List<User> getAllUsers() {
		List<User> users = null;
		
		try {
			users = helper.getUserDao().queryForAll();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return users;
	}

	public List<StreetAddress> contain(String filterKey) {
//		try {
//			QueryBuilder<StreetAddress, Void> qb = helper.getStreetAddressDao().queryBuilder();
//			qb.where().like("madeCompany", "%"+filterKey+"%");
//			qb.limit(10)
//			PreparedQuery<StreetAddress> pq = qb.prepare();
//			return helper.getStreetAddressDao().query(pq);
//		} catch (SQLException e) {
//			throw new AppException(e);
//		}
		return null;
	}
	
	/**
	 * �ش� id �� User ������ ��ȸ
	 * @param id ã�� User�� id
	 * @return �ش� �����Ͱ� ���ų� ���� �߻� �� null
	 */
	public User getUser(String id) {
		User user = null;
		try {
			user = helper.getUserDao().queryForId(id);			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return user;
	}
	
	/**
	 * �ش� id �� Shop �����͸� ��ȸ
	 * @param id ã�� Shop�� id
	 * @return Shop, �ش� �����Ͱ� ���ų� ���� �߻� �� null
	 */
	public Shop getShop(String id) {
		Shop shop = null;
		
		try {
			shop = helper.getShopDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shop;
	}
	
	/**
	 * �ش� id �� Inspection �����͸� ��ȸ
	 * @param id ã�� Inspection�� id
	 * @return Inspection, �ش� �����Ͱ� ���ų� ���� �߻� �� null
	 */
	public Inspection getInspection(long id) {
		Inspection inspection = null;
		
		try {
			inspection = helper.getInspectionDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return inspection;
	}
	
	/**
	 * �ش� town(��/��/��)�� ���� Building �����͸� ��ȸ
	 * @param town ã�� town
	 * @return Building ����Ʈ, ���� �߻� �� null
	 */
	public List<Building> findBuildingByTown(String town) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("town", town);
		
		List<Building> buildings = null;
		try {
			buildings = helper.getBuildingDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return buildings;
	}

	public List<Building> findBuildingByProvince(String province) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("province", province);

		List<Building> buildings = null;
		try {
			buildings = helper.getBuildingDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return buildings;
	}
	
	/**
	 * �ش� Building�� ���� Shop �����͸� ��ȸ
	 * @param id Building�� id
	 * @return Shop ����Ʈ, ���� �߻� �� null
	 */
	public List<Shop> findShopByBuildingId(long id) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("buildingId", id);

		List<Shop> shops = null;
		try {
			shops = helper.getShopDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shops;
	}
	
//	public List<Shop> findShopByAddress(String province, String county, String town) {
//		
//		QueryBuilder<Shop, String> sqb = helper.getShopDao().queryBuilder();
//		
//		QueryBuilder<Building, Long> bqb = helper.getBuildingDao().queryBuilder();
//				
//		bqb.
//		
//		
//		return null;
//	}
	
	/**
	 * �ش� Building id, ���� �̸����� Shop ������ ��ȸ
	 * @param buildingId Building �� id
	 * @param shopName ���� �̸�
	 * @return Shop, ���� �߻� �� null
	 */
	public Shop findShopByBuildingIdAndShopName(long buildingId, String shopName) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
		argMap.put("buildingId", buildingId);
		argMap.put("name", shopName);

		List<Shop> shops = null;
		try {
			shops = helper.getShopDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// ������ �ϳ��� �����°� ���������� ������ ���� ���� �ְ���..
		Shop shop = null;
		if(shops != null && shops.size() > 0)
			shop = shops.get(0);
		
		return shop;
	}
	
	/**
	 * �ش� �̸��� Shop ������ ��ȸ.
	 * @param shopName Shop �� �̸�
	 * @return Shop ����Ʈ, ���� �߻� �� null
	 */
	public List<Shop> findShopByShopName(String shopName) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("name", shopName);
		
		List<Shop> shops = null;
		try {
			shops = helper.getShopDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shops;
	}
	
	public List<Shop> findShopByNameAndAddress(String shopName, int addressId) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
		argMap.put("name", shopName);
		argMap.put("addressId", addressId);
		
		List<Shop> shops = null;
		try {
			shops = helper.getShopDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shops;
	}
	
	public List<Shop> findShopByAddress(int addressId) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
		argMap.put("addressId", addressId);
		
		List<Shop> shops = null;
		try {
			shops = helper.getShopDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shops;
	}
	
	
	public Sign getSign(long id) {
		Sign sign = null;
		try {
			sign = helper.getSignDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return sign;
	}
	
//	public List<Inspection> findInspectionBySignId(long id, boolean inspectionNoIncluded) {
//		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
//		argMap.put("signId", id);
//		if(inspectionNoIncluded == false)
//			argMap.put("no", "");
//		
//		List<Inspection> inspections = null;
//		try {
//			inspections = helper.getInspectionDao().queryForFieldValuesArgs(argMap);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		return inspections;		
//	}
//	
	
	
	public List<TownForInspection> findTown(String province, String county) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
		argMap.put("province", province);
		argMap.put("county", county);
		
		List<TownForInspection> addr = null;
		
		try {
			addr = helper.getTownForInspectionDao().queryForFieldValues(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return addr;
	}
	
	public List<StreetAddress> getStreetAddress(String town, String consonant) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
		argMap.put("town", town);
		argMap.put("initialConsonant", consonant);
		
		List<StreetAddress> addr = null;
		
		try {
			addr = helper.getStreetAddressDao().queryForFieldValues(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return addr;
	}
	
	public List<StreetAddress> getStreetAddress(String town) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
		argMap.put("town", town);
		
		List<StreetAddress> addr = null;
		
		try {
			addr = helper.getStreetAddressDao().queryForFieldValues(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return addr;
	}
	
	public int insertStreetAddress(String consonant, String street, String town) {
		StreetAddress address = new StreetAddress(consonant, street, town);
		int result = -1;
		try {
			result = helper.getStreetAddressDao().create(address);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Building getBuilding(long id) {
		Building building = null;
		try {
			building = helper.getBuildingDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return building;
	}
	
	public List<Building> getBuilding(String firstNumber, String secondNumber) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(2);
		argMap.put("firstBuildingNumber", firstNumber);
		argMap.put("secondBuildingNumber", secondNumber);
		
		List<Building> result = null;
		
		try {
			result = helper.getBuildingDao().queryForFieldValues(argMap);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
//	public List<Building> findBuilding(String province, String county, String town, String street, String firstNumber, String secondNumber) {
//		HashMap<String, Object> argMap = new HashMap<String, Object>(6);
//		argMap.put("province", province);
//		argMap.put("county", county);
//		argMap.put("town", town);
//		argMap.put("streetName", street);
//		argMap.put("firstBuildingNumber", firstNumber);
//		argMap.put("secondBuildingNumber", secondNumber);
//		
//		List<Building> result = null;
//		
//		try {
//			result = helper.getBuildingDao().queryForFieldValues(argMap);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
	
	public List<Building> findBuilding(String province, String county, String town, String street, String firstNumber,
															String secondNumber, String village, String houseNumber) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(8);
		if(province != null && province.equals("") == false)
			argMap.put("province", province);
		if(county != null && county.equals("") == false)
			argMap.put("county", county);
		if(town != null && town.equals("") == false)
			argMap.put("town", town);
		if(street != null && street.equals("") == false)
			argMap.put("streetName", street);
		if(firstNumber != null && firstNumber.equals("") == false)
			argMap.put("firstBuildingNumber", firstNumber);
		if(secondNumber != null && secondNumber.equals("") == false)
			argMap.put("secondBuildingNumber", secondNumber);
		if(village != null && village.equals("") == false)
			argMap.put("village", village);
		if(houseNumber != null && houseNumber.equals("") == false)
			argMap.put("houseNumber", houseNumber);
		
		List<Building> result = null;
		
		try {
			result = helper.getBuildingDao().queryForFieldValues(argMap);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<Shop> getShopByBuildingId(long id) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("buildingId", id);
		
		List<Shop> result = null;
		
		try {
			result = helper.getShopDao().queryForFieldValues(argMap);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * shop ������ �߰� Ȥ�� ����. <br>
	 * ���� �����Ͱ� �̹� ������ ���� �ο����� id �� �Բ� �ش� ��ü ����.<br>
	 * �׷��� ������ �̹� �ִ� �����͸� ����.
	 * 
	 * @param shop
	 * @return
	 */
	public Shop insertOrUpdateShop(Shop shop) {
		Shop result;
		try {
			result = helper.getShopDao().createIfNotExists(shop);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	public List<SignOwnership> findSignOwnershipByShopId(String id) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("shopId", id);
		
		List<SignOwnership> result = null;
		
		try {
			result = helper.getSignOwnershipDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		helper.getSignDao().q
		
		return result;
	}
	
	public List<SignOwnership> findSignOwnershipBySignId(long id) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("signId", id);
		
		List<SignOwnership> result = null;
		
		try {
			result = helper.getSignOwnershipDao().queryForFieldValuesArgs(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<Inspection> findInspectionBySignId(long id) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("signId", id);
		
		List<Inspection> result = null;
		
		try {
			result = helper.getInspectionDao().queryForFieldValues(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		helper.getInspectionDao().queryBuilder().
		
		return result;
	}
	
	public List<Setting> getSettingByItem(String item) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("item", item);
		
		List<Setting> result = null;
		
		try {
			result = helper.getSettingDao().queryForFieldValues(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<Setting> getAllSetting() {
		List<Setting> result = null;
		
		try {
			result = helper.getSettingDao().queryForAll();
			for(int i=0; i<result.size(); i++) {
				Setting s = result.get(i);
//				String msg = String.format("%d ��° ����: ī�װ�:%d, �ڵ�:%d, �̸�:%s, ord:%d", (i+1), s.getCategory(), s.getCode(), s.getName(), s.getOrd());
//				Log.d("junseo", msg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public SignOwnership insertSignOwnership(SignOwnership swnership) {
		SignOwnership owership = null;
		try {
			owership = helper.getSignOwnershipDao().createIfNotExists(swnership);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return owership;
	}
	
	public List<BuildingPicture> findBuildingPictureByBuildingId(long buildingId) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
		argMap.put("buildingId", buildingId);
		
		List<BuildingPicture> result = null;
		
		try {
			result = helper.getBuildingPictureDao().queryForFieldValues(argMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int insertBuildingPicture(BuildingPicture data) {
		int result = -1;
		try {
			result = helper.getBuildingPictureDao().create(data);
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}
		
		return result;
	}
	
	public int deleteBuildingPicture(BuildingPicture data) {
		int result = -1;
		
		try {
			result = helper.getBuildingPictureDao().delete(data);
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}
		
		return result;
	}
	
	public int insertShop(Shop shop) {
		int result = -1;
		
		try {
			result = helper.getShopDao().create(shop);
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}
		
		return result;
	}
	
	public long insertSign(Sign sign) {
		long result = -1;
		
		try {
			Sign s = helper.getSignDao().createIfNotExists(sign);
			if(s != null)
				result = s.getId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public long insertInspection(Inspection inspection) {
		long result = -1;
		
		try {
			Inspection s = helper.getInspectionDao().createIfNotExists(inspection);
			if(s != null)
				result = s.getId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Mobile getMobile(int id) {
		
		Mobile mobile = null;
		
		try {
			mobile = helper.getMobileDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mobile;
	}

	public boolean modifyBuilding(Building building) {
		int result = -1;
		try {
			result = helper.getBuildingDao().update(building);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return (result == 1);
	}

	public boolean modifyShop(Shop shop) {
		int result = -1;
		try {
			result = helper.getShopDao().update(shop);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result == 1);
	}
	
	public boolean modifySign(Sign sign) {
		int result = -1;
		
		try {
			result = helper.getSignDao().update(sign);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result == 1);
	}
	
	public boolean modifyInspection(Inspection inspection) {
		int result = -1;
		
		try {
			result = helper.getInspectionDao().update(inspection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result == 1);
	}
	
	public boolean deleteSign(long signId) {
		int result = -1;
		
		try {
			result = helper.getSignDao().deleteById(signId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result == 1);
	}
	
	public boolean deleteSignOwnershipsBySignId(long signId) {
//		HashMap<String, Object> argMap = new HashMap<String, Object>(1);
//		argMap.put("signId", signId);
		
		int result = -1;
		List<SignOwnership> target = findSignOwnershipBySignId(signId);
		try {			
			result = helper.getSignOwnershipDao().delete(target);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return (target.size() == result);		
	}
	
	public boolean deleteInspectionsBySignId(long signId) {
		int result = -1;
		List<Inspection> target = findInspectionBySignId(signId);
		
		try {
			result = helper.getInspectionDao().delete(target);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return (target.size() == result);
	}
	
	public int findAddressId(String province, String county, String town) {
		HashMap<String, Object> argMap = new HashMap<String, Object>(3);
		argMap.put("province", province);
		argMap.put("county", county);
		argMap.put("town", town);
		
		int id = -1;		
		
		try {
			List<TownForInspection> addr = helper.getTownForInspectionDao().queryForFieldValues(argMap);
			if(addr != null && addr.size() > 0)
				id = addr.get(0).getId();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
}
