package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.BuildingPicture;

import java.util.List;

/**
 * Created by Junseo on 2016-08-03.
 */
public class LoadBuildingPictureTask extends DefaultAsyncTask<Long, Integer, List<BuildingPicture>> {
    private Context context;

    public LoadBuildingPictureTask(Context c) {
        context = c;
    }

    @Override
    protected List<BuildingPicture> doInBackground(Long... params) {
        if(params == null)
            return null;

        long buildingId = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<BuildingPicture> pictures = dmgr.findBuildingPictureByBuildingId(buildingId);

        return pictures;
    }
}
