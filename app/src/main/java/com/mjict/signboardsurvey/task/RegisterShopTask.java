package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;


/**
 * Created by Junseo on 2016-08-18.
 */
public class RegisterShopTask extends DefaultAsyncTask<Shop, Integer, Long> {
    private Context context;

    public RegisterShopTask(Context c) {
        context = c;
    }

    @Override
    protected Long doInBackground(Shop... params) {
        if(params == null)
            return -1L;

        Shop shop = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        long result = dmgr.insertShop(shop);

        return result;
    }
}
