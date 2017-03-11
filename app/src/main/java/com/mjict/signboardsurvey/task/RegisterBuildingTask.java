package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;

/**
 * Created by Junseo on 2017-03-07.
 */
public class RegisterBuildingTask extends DefaultAsyncTask<Building, Integer, Long> {
    private Context context;

    public RegisterBuildingTask(Context c) {
        context = c;
    }

    @Override
    protected Long doInBackground(Building... params) {
        if(params == null)
            return -1L;

        Building building = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        long result = dmgr.insertBuilding(building);

        return result;
    }


}
