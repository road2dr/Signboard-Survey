package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.location.Location;

import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.util.Utilities;

/**
 * Created by Junseo on 2016-07-27.
 */
public class SearchAddressFromLocationTask extends DefaultAsyncTask<Location, Integer, Address> {

    private Context context;

    public SearchAddressFromLocationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Address doInBackground(Location... params) {
        Location location = params[0];


        Address address = Utilities.getAddressFromLocation(context, location);


        return address;
    }


}
