package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ShopAndSign;
import com.mjict.signboardsurvey.model.Sign;

import java.util.List;

/**
 * Created by Junseo on 2016-12-25.
 */
public class LoadShopAndSignByBuildingTask extends DefaultAsyncTask<Building, ShopAndSign, Boolean> {
    private Context context;

    public LoadShopAndSignByBuildingTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(Building... params) {
        if(params == null)
            return false;

        // 총 정보 로드
        for(int i=0; i<params.length; i++) {
            DatabaseManager dmgr = DatabaseManager.getInstance(context);
            Building b = params[i];
            List<Shop> shopsInBuilding = dmgr.findShopByBuildingId(b.getId());

            List<Sign> signs = null;
            for(int j=0; j<shopsInBuilding.size(); j++) {
                Shop shop = shopsInBuilding.get(j);
                if(shop.isDeleted() == true)
                    continue;

                signs = dmgr.findSignsByShopId(shop.getId());
            }

            ShopAndSign sas = new ShopAndSign(i, shopsInBuilding, signs);
            publishProgress(sas);
        }

        return true;
    }
}
