package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;


/**
 * Created by Junseo on 2016-08-18.
 */
public class ModifyShopTask extends DefaultAsyncTask<Shop, Integer, Boolean> {
    private Context context;

    public ModifyShopTask(Context c) {
        this.context = c;
    }

    @Override
    protected Boolean doInBackground(Shop... params) {
        if(params == null || params.length <= 0)
            return false;

        Shop shop = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        boolean result = dmgr.modifyShop(shop);

        return result;
    }
}
