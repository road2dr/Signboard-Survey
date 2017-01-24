package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.model.Sign;

import java.util.ArrayList;

/**
 * Created by Junseo on 2017-01-22.
 */
public class LoadSignsFromSignTask extends DefaultAsyncTask<Sign, Integer, ArrayList<Sign>> {
    private Context context;

    public LoadSignsFromSignTask(Context c) {
        context = c;
    }

    @Override
    protected ArrayList<Sign> doInBackground(Sign... params) {
        if(params == null)
            return null;

        Sign sign = params[0];
        // TODO sign 에 shopID 추가 되면 그때 추가

        return null;
    }
}
