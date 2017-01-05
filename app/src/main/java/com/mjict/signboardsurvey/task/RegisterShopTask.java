package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;


/**
 * Created by Junseo on 2016-08-18.
 */
public class RegisterShopTask extends DefaultAsyncTask<Shop, Integer, Boolean> {
    private Context context;

    public RegisterShopTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(Shop... params) {
        if(params == null)
            return false;

        Shop shop = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        int result = dmgr.insertShop(shop);

        return (result != -1);
    }
}
