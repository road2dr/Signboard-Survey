package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.util.Log;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.SettingDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-08-31.
 */
public class SearchDemolitionSignTask extends DefaultAsyncTask<Address, Integer, List<Sign>> {

    private Context context;

    public SearchDemolitionSignTask(Context c) {
        context = c;
    }

    @Override
    protected List<Sign> doInBackground(Address... params) {

        Address addr = params[0];
        if(addr == null)
            return null;

        String province = addr.province;
        String county = addr.county;
        String town = addr.town;
        String street = addr.street;
        String firstNumber = addr.firstNumber;
        String secondNumber = addr.secondNumber;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        SettingDataManager sdmgr = SettingDataManager.getInstance();

        // 전체 sign 부터 뒤져보는 방식
//        List<Sign> allSigns = dmgr.getAllSigns();
//        int n = allSigns.size();
//        List<DemolitionSign> demolitionSigns = new ArrayList<>();
//
//        int status = -1;
//        String type = null;
//        String content = null;
//        String shopName = null;
//        String lightType = null;
//        String result = null;
//        String size = null;
//        String location = null;
//        String date = null;
//        Shop shop = null;
//        Sign sign = null;
//
//        int addressId = dmgr.findAddressId(province, county, town);
//
//        Log.d("junseo", "all signs: "+n);
//        for(int i=0; i<n; i++) {
//            sign = allSigns.get(i);
//            if(sign.isDeleted() == true)
//                continue;
//
//            if(sign.getStatusCode() == 1 || sign.getStatusCode() == 2) {  // 철거, 철거 예정
//
//                List<SignOwnership> ownerships = dmgr.findSignOwnershipBySignId(sign.getId());     // TODO 얘도 나중에 변경 되겠지
//                if(ownerships != null && ownerships.size() > 0) {
//                    String shopId = ownerships.get(0).getShopId();
//                    shop = dmgr.getShop(shopId);
//                    if(shop.getAddressId() == addressId) {      // 주소 일치 할 경우
//                        status = sign.getStatusCode();
//                        content = sign.getContent();
//                        Setting typeSetting = sdmgr.getSignType(sign.getType());
//                        type = (typeSetting == null) ? sdmgr.getDefaultSignTypeName() : typeSetting.getName();
//                        Setting lightTypeSetting = sdmgr.getLightType(sign.getLightType());
//                        lightType = (lightTypeSetting == null) ? sdmgr.getDefaultLightTypeName(): lightTypeSetting.getName();
//                        Setting resultSetting = sdmgr.getResult(sign.getInspectionResult());
//                        result = (resultSetting == null) ? sdmgr.getDefaultResultName() : resultSetting.getName();
//                        size = sign.getWidth()+"X"+sign.getLength();
//                        if(sign.getHeight() > 0)
//                            size = size +"X"+sign.getHeight();
//                        location = sign.getPlacedFloor()+"/"+sign.getTotalFloor();
//                        date = sign.getInputDate();
//                        shopName = shop.getName();
//
//                        DemolitionSign demolitionSign = new DemolitionSign(null, null, status, type, content, shopName, lightType,
//                                result, size, location, date, sign, shop);
//                        demolitionSigns.add(demolitionSign);
//                    }
//                }
//            }
//        }

        // 건물 부터 뒤져보는 방식
        List<Sign> demolitionSigns = new ArrayList<>();
        List<Building> buildings = dmgr.findBuilding(province, county, town, street, firstNumber, secondNumber, null, null);

        int n = buildings.size();
        for(int i=0; i<n; i++) {
            Building b = buildings.get(i);
            List<Shop> shops = dmgr.findShopByBuildingId(b.getId());
            for(int j=0; j<shops.size(); j++) {
                Shop shop = shops.get(j);
//                if(shop.getBusinessCondition())   // TODO 원래는 이렇게 접근 해야 하지만 DB 데이터가 완전하지 않은 관계로 각 sign 의 상태 체크

                List<Sign> signs = dmgr.findSignsByShopId(shop.getId());
                for(int k=0; k<signs.size(); k++) {
                    Sign sign = signs.get(k);
                    demolitionSigns.add(sign);
                }
            }
        }

        Log.d("junseo", "철거 간판 갯수: "+demolitionSigns.size());

        return demolitionSigns;
    }
}
