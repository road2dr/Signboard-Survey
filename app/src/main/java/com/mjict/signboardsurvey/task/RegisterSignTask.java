package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Sign;


/**
 * Created by Junseo on 2016-08-12.
 */
public class RegisterSignTask extends DefaultAsyncTask<Sign, Integer, Long> {
    private Context context;
    private long shopId;

    public RegisterSignTask(Context c, long id) {
        context = c;
        shopId = id;
    }

    @Override
    protected Long doInBackground(Sign... params) {
        if(params == null)
            return -1L;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        Sign sign = params[0];
        sign.setShopId(shopId);

        long signId = dmgr.insertSign(sign);
        if(signId == -1)
            return -1L;

        return signId;    // TODO 롤백?
    }
}
