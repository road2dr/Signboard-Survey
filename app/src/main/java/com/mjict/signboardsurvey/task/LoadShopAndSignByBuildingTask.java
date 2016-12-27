package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ShopAndSign;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.SignOwnership;

import java.util.ArrayList;
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

            // TODO 나중에 SignOwnership 이 없어지면 바뀌겠지
            List<Sign> signs = new ArrayList<>();
            for(int j=0; j<shopsInBuilding.size(); j++) {
                Shop shop = shopsInBuilding.get(j);
                if(shop.getIsDeleted() == true)
                    continue;

                List<SignOwnership> ownerships = dmgr.findSignOwnershipByShopId(shop.getId());

                for(int k=0; k<ownerships.size(); k++) {
                    Sign s = dmgr.getSign(ownerships.get(k).getSignId());
                    if(s.isDeleted() == false)
                        signs.add(s);
                }
            }

            ShopAndSign sas = new ShopAndSign(i, shopsInBuilding, signs);
            publishProgress(sas);
        }

        return true;
    }
}
