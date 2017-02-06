package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.SettingDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-09-08.
 */
public class SearchReviewSignTask extends DefaultAsyncTask<Address, Integer, List<Sign>> {
    private Context context;

    public SearchReviewSignTask(Context c) {
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

        // 전체 간판에서 찾아가는 방식
//        int addressId = dmgr.findAddressId(province, county, town);
//
//        List<ReviewSign> reviewSigns = new ArrayList<>();
//        List<Sign> allSigns = dmgr.getAllSigns();
//        int n = allSigns.size();
//
//        for(int i=0; i<n; i++) {
//            Sign sign = allSigns.get(i);
//            if(sign.getReviewCodeCode() == 0)
//                continue;
//
//            Setting reviewSetting = sdmgr.getReviewCode(sign.getReviewCodeCode());
//            if(reviewSetting == null)
//                continue;
//
//            Shop shop = null;
//            List<SignOwnership> ownerships = dmgr.findSignOwnershipBySignId(sign.getId());     // TODO 얘도 나중에 변경 되겠지
//            if(ownerships != null && ownerships.size() > 0) {
//                String shopId = ownerships.get(0).getShopId();
//                shop = dmgr.getShop(shopId);
//                if (shop.getAddressId() == addressId) {      // 주소 일치 할 경우
//                    Setting typeSetting = sdmgr.getSignType(sign.getType());
//                    Setting lightSetting = sdmgr.getLightType(sign.getLightType());
//                    Setting resultSetting = sdmgr.getResult(sign.getInspectionResult());
//
//                    String status = reviewSetting.getName();
//                    String type = typeSetting == null ? sdmgr.getDefaultSignTypeName() : typeSetting.getName();
//                    String content = sign.getContent();
//                    String shopName = shop.getName();
//                    String lightType = lightSetting == null ? sdmgr.getDefaultLightTypeName() : lightSetting.getName();
//                    String result = resultSetting == null ? sdmgr.getDefaultResultName() : resultSetting.getName();
//                    String size = sign.getHeight() > 0 ? sign.getWidth()+"X"+sign.getLength() : sign.getWidth()+"X"+sign.getLength()+"X"+sign.getHeight();
//                    String location = sign.getPlacedFloor()+"/"+sign.getTotalFloor();
//                    String date = sign.getInputDate();
//
//                    ReviewSign reviewSign = new ReviewSign(null, status, type, content, shopName, lightType,
//                            result, size, location, date, sign, shop);
//                    reviewSigns.add(reviewSign);
//                }
//            }
//        }

        // 건물 검색 후 찾는 방법
        // 건물 부터 뒤져보는 방식
        List<Sign> reviewSigns = new ArrayList<>();
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

                    if(sign.getReviewCode().equals("0"))
                        continue;

                    Setting reviewSetting = sdmgr.getReviewCode(sign.getReviewCode());
                    if(reviewSetting == null)
                        continue;

                    reviewSigns.add(sign);
//                    Setting typeSetting = sdmgr.getSignType(sign.getType());
//                    Setting lightSetting = sdmgr.getLightType(sign.getLightType());
//                    Setting resultSetting = sdmgr.getResult(sign.getInspectionResult());
//
//                    String status = reviewSetting.getName();
//                    String type = typeSetting == null ? sdmgr.getDefaultSignTypeName() : typeSetting.getName();
//                    String content = sign.getContent();
//                    String shopName = shop.getName();
//                    String lightType = lightSetting == null ? sdmgr.getDefaultLightTypeName() : lightSetting.getName();
//                    String result = resultSetting == null ? sdmgr.getDefaultResultName() : resultSetting.getName();
//                    String size = sign.getHeight() > 0 ? sign.getWidth()+"X"+sign.getLength() : sign.getWidth()+"X"+sign.getLength()+"X"+sign.getHeight();
//                    String location = sign.getPlacedFloor()+"/"+sign.getTotalFloor();
//                    String date = sign.getInputDate();
//
//                    ReviewSign reviewSign = new ReviewSign(null, status, type, content, shopName, lightType,
//                            result, size, location, date, sign, shop);
//                    reviewSigns.add(reviewSign);

                }
            }
        }


        return reviewSigns;
    }
}
