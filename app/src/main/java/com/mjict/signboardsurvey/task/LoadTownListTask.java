package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.TownForInspection;

import java.util.List;

/**
 * Created by Junseo on 2016-08-02.
 */
public class LoadTownListTask extends DefaultAsyncTask<String, Integer, String[]> {

    private Context context;

    public LoadTownListTask(Context c) {
        context = c;
    }

    @Override
    protected String[] doInBackground(String... params) {
        if(params == null)
            return null;

        if(params.length < 2)
            return null;

        String province = params[0];
        String county = params[1];

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<TownForInspection> ts = dmgr.findTown(province, county);
        if(ts == null)
            return null;

        int n = ts.size();
        String[] towns = new String[n];
        for(int i=0; i<n; i++)
            towns[i] = ts.get(i).getTown();

        return towns;
    }
}
