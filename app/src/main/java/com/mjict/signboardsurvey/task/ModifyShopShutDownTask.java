package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;

import java.util.List;

/**
 * Created by Junseo on 2017-01-22.
 */
public class ModifyShopShutDownTask extends DefaultAsyncTask<Shop, Integer, Shop> {

    private Context context;

    public ModifyShopShutDownTask(Context c) {
        context = c;
    }

    @Override
    protected Shop doInBackground(Shop... params) {
        if(params == null)
            return null;

        Shop shop = params[0];
        shop.setBusinessCondition(1);       // TODO 이런 상수는 따로 파일로 혹은 enum 으로..

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        boolean answer = dmgr.modifyShop(shop);
        if(answer == false)
            return null;

        List<Sign> signs = dmgr.findSignsByShopId(shop.getId());
        if(signs == null)
            return null;

        for(int i=0; i<signs.size(); i++) {
            Sign sign = signs.get(i);
            sign.setStatsCode(3);

            answer = dmgr.modifySign(sign);
            if(answer == false)
                return null;
        }

        return shop;
    }
}
