package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;

import java.util.List;

/**
 * Created by Junseo on 2017-01-05.
 */
public class SearchShopByKeywordTask extends DefaultAsyncTask<String, Integer, List<Shop>> {
    private Context context;
    public SearchShopByKeywordTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Shop> doInBackground(String... params) {
        if(params == null)
            return null;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<Shop> shops = dmgr.findShopsContain(params[0], -1);

        return shops;
    }
}
