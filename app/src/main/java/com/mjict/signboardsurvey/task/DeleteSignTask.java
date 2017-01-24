package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Sign;

/**
 * Created by Junseo on 2017-01-22.
 */
public class DeleteSignTask extends DefaultAsyncTask<Sign, Integer, Boolean> {

    private Context context;

    public DeleteSignTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(Sign... params) {
        if(params == null)
            return false;

        Sign sign = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        boolean answer = dmgr.deleteSign(sign.getId());

        return answer;
    }
}
