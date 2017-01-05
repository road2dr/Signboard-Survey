package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.StreetAddress;

import java.util.List;

/**
 * Created by Junseo on 2017-01-05.
 */
public class SearchAddressByKeywordTask extends DefaultAsyncTask<String, Integer, List<StreetAddress>> {
    private Context context;

    public SearchAddressByKeywordTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<StreetAddress> doInBackground(String... params) {
        if(params == null)
            return null;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<StreetAddress> streetAddresses = dmgr.findStreetAddressContain(params[0], -1);

        return streetAddresses;
    }
}
