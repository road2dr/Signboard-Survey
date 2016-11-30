package com.mjict.signboardsurvey.handler;

import android.os.Bundle;

import com.mjict.signboardsurvey.activity.SummaryActivity;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;

/**
 * Created by Junseo on 2016-11-09.
 */
public class SummaryActivityHandler extends DefaultSActivityHandler {

    private SummaryActivity activity;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        // register listener
        activity = (SummaryActivity)this.getActivity();


        // do first job
    }
}
