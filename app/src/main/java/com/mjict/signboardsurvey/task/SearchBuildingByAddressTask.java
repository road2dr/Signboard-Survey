package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;

import java.util.List;

/**
 * Created by Junseo on 2016-12-06.
 */
public class SearchBuildingByAddressTask extends DefaultAsyncTask<Address, Integer, List<Building>> {
    private Context context;

    public SearchBuildingByAddressTask(Context c) {
        context = c;
    }


    @Override
    protected List<Building> doInBackground(Address... params) {

        if (params == null)
            return null;

        if (params.length <= 0)
            return null;

        Address cond = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        List<Building> buildings = dmgr.findBuilding(cond.province, cond.county, cond.town, cond.street,
                cond.firstNumber, cond.secondNumber, cond.village, cond.houseNumber);

        if(buildings == null)
            return null;

        return buildings;
    }
}
