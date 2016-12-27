package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Sign;


/**
 * Created by Junseo on 2016-08-16.
 */
public class ModifySignTask extends DefaultAsyncTask<Sign, Integer, Boolean> {
    public Context context;

    public ModifySignTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(Sign... params) {
        if(params == null || params.length <= 0)
            return false;

        Sign shop = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        boolean result = dmgr.modifySign(shop);

        return result;
    }
}
