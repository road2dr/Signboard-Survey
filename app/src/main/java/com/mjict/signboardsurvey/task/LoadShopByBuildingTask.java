package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;

import java.util.List;

/**
 * Created by Junseo on 2016-08-03.
 */
public class LoadShopByBuildingTask extends DefaultAsyncTask<Long, Integer, List<Shop>> {

    private Context context;

    public LoadShopByBuildingTask(Context c) {
        context = c;
    }

    @Override
    protected List<Shop> doInBackground(Long... params) {
        if(params == null)
            return null;

        long buildingId = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        List<Shop> shops = dmgr.findShopByBuildingId(buildingId);
        for(int i=0; i<shops.size(); i++) {
            Shop shop = shops.get(i);
            if(shop.getIsDeleted() == true)     // TODO 삭제 기능 관련 해서 한번 얘기를 해보자
                shops.remove(i);
        }

        return shops;
    }
}