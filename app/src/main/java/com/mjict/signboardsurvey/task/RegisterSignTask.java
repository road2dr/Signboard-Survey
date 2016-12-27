package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Inspection;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.SignOwnership;
import com.mjict.signboardsurvey.util.SyncConfiguration;


/**
 * Created by Junseo on 2016-08-12.
 */
public class RegisterSignTask extends DefaultAsyncTask<Sign, Integer, Long> {
    private Context context;
    private String shopId;

    public RegisterSignTask(Context c, String id) {
        context = c;
        shopId = id;
    }

    @Override
    protected Long doInBackground(Sign... params) {
        if(params == null)
            return -1L;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        Sign sign = params[0];

        long signId = dmgr.insertSign(sign);
        if(signId == -1)
            return -1L;

        SignOwnership ownership = new SignOwnership(0, shopId, signId);
        SignOwnership o = dmgr.insertSignOwnership(ownership);
        if(o == null)
            return -1L;   // TODO 롤백?

        long inspectionNo = SyncConfiguration.getLastInspectionNo();
        int mobileId = SyncConfiguration.getMobileNo();
        String date = sign.getInputDate();
        String userId = sign.getInputor();
        Inspection inspection = new Inspection(0, String.valueOf(inspectionNo), date, mobileId, signId, userId, date, "", "", "");

        long inspectionId = dmgr.insertInspection(inspection);
        if(inspectionId == -1)
            return -1L;

        return signId;    // TODO 롤백?
    }
}
