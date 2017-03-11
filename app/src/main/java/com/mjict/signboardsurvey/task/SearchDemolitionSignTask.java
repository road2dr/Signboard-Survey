package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.SignInformation;
import com.mjict.signboardsurvey.util.SettingDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-08-31.
 */
public class SearchDemolitionSignTask extends DefaultAsyncTask<Address, Integer, List<SignInformation>> {

    private Context context;

    public SearchDemolitionSignTask(Context c) {
        context = c;
    }

    @Override
    protected List<SignInformation> doInBackground(Address... params) {

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

        // 건물 부터 뒤져보는 방식
        List<SignInformation> demolitionSigns = new ArrayList<>();
        List<Building> buildings = dmgr.findBuilding(province, county, town, street, firstNumber, secondNumber, null, null);

        int n = buildings.size();
        for(int i=0; i<n; i++) {
            Building b = buildings.get(i);
            List<Shop> shops = dmgr.findShopByBuildingId(b.getId());
            for(int j=0; j<shops.size(); j++) {
                Shop shop = shops.get(j);

                List<Sign> signs = dmgr.findSignsByShopId(shop.getId());
                for(int k=0; k<signs.size(); k++) {
                    Sign sign = signs.get(k);

                    if(sign.getStatsCode().equals("4") == false) {  // 정상 빼고 전부다
                        SignInformation sb = new SignInformation(sign, shop, b);
                        demolitionSigns.add(sb);
                    }
                }
            }
        }
        return demolitionSigns;
    }
}
