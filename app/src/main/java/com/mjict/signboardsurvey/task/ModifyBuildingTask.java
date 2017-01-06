package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;



/**
 * Created by Junseo on 2016-09-27.
 */
public class ModifyBuildingTask extends DefaultAsyncTask<Building, Integer, Boolean> {
    private Context context;

    public ModifyBuildingTask(Context c) {
        this.context = c;
    }

    @Override
    protected Boolean doInBackground(Building... params) {
        if(params == null || params.length <= 0)
            return false;

        Building building = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        boolean result = dmgr.modifyBuilding(building);

        return result;
    }
}
