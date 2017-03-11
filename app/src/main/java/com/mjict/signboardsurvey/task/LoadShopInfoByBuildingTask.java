package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ShopInformation;
import com.mjict.signboardsurvey.model.Sign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-08-03.
 */
public class LoadShopInfoByBuildingTask extends DefaultAsyncTask<Long, Integer, List<ShopInformation>> {

    private Context context;

    public LoadShopInfoByBuildingTask(Context c) {
        context = c;
    }

    @Override
    protected List<ShopInformation> doInBackground(Long... params) {
        if(params == null)
            return null;

        long buildingId = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        List<Shop> shops = dmgr.findShopByBuildingId(buildingId);

        List<ShopInformation> shopInfo = new ArrayList<>();
        for(int i=0; i<shops.size(); i++) {
            Shop shop = shops.get(i);
            List<Sign> signs = dmgr.findSignsByShopId(shop.getId());
            shopInfo.add(new ShopInformation(shop, signs));
        }

        return shopInfo;
    }
}