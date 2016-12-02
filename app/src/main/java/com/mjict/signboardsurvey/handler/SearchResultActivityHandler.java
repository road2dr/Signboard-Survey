package com.mjict.signboardsurvey.handler;

import android.os.Bundle;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.activity.SearchResultActivity;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;

/**
 * Created by Junseo on 2016-11-11.
 */
public class SearchResultActivityHandler extends DefaultSActivityHandler {

    private SearchResultActivity activity;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SearchResultActivity)getActivity();

        // register listener

        // init
        String keyword = activity.getIntent().getStringExtra(MJConstants.KEYWORD);
        if(keyword == null) {
            // TODO 메세지 추가
            activity.finish();
            return;
        }

        // do first job
        startToFindResult(keyword);
    }

    private void startToFindResult(String keyword) {

    }
}
