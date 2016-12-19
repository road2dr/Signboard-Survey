package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.BuildingPicture;

/**
 * Created by Junseo on 2016-12-14.
 */
public class InsertBuildingPictureTask extends DefaultAsyncTask<BuildingPicture, Long, Boolean> {

    private Context context;

    public InsertBuildingPictureTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(BuildingPicture... params) {
        if(params == null)
            return false;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        for(int i=0; i<params.length; i++) {
            BuildingPicture bp = params[i];
            long id = dmgr.insertBuildingPicture(bp);
            publishProgress(id);
        }

        return true;
    }
}
