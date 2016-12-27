package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.StreetAddress;

import java.util.List;

/**
 * Created by Junseo on 2016-08-02.
 */
public class LoadStreetAddressTask extends DefaultAsyncTask<String, Integer, List<StreetAddress>> {

    Context context;

    public LoadStreetAddressTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<StreetAddress> doInBackground(String... params) {
        if(params == null || params.length < 2)
            return null;

        String town = params[0];
        String consonant = params[1];

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<StreetAddress> streetAddr = dmgr.getStreetAddress(town, consonant);

        return streetAddr;
    }
}
