package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;

import java.util.List;

/**
 * Created by Junseo on 2016-12-13.
 */
public class LoadSignsByShopTask extends DefaultAsyncTask<Shop, Sign, Boolean> {
    private Context context;

    public LoadSignsByShopTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(Shop... params) {
        if (params == null)
            return false;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        for (int i = 0; i < params.length; i++) {
            List<Sign> signs = dmgr.findSignsByShopId(params[i].getId());
            for (int j = 0; j < signs.size(); j++) {
                Sign s = signs.get(j);
                publishProgress(s);
            }
        }


        return true;
    }
}
