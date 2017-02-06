package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;

import java.util.List;

/**
 * Created by Junseo on 2016-09-01.
 */
public class DeleteShopTask extends DefaultAsyncTask<Shop, Integer, Boolean> {
    private Context context;

    public DeleteShopTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(Shop... params) {
        if(params == null)
            return false;

        Shop shop = params[0];

        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        List<Sign> signs = dmgr.findSignsByShopId(shop.getId());
        for(int i=0; i<signs.size(); i++) {
            Sign sign = signs.get(i);
            dmgr.deleteSign(sign.getId());
        }

        boolean result = dmgr.deleteShop(shop.getId());

        return result;
    }
}
