package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.activity.AddressSearchActivity;
import com.mjict.signboardsurvey.activity.DemolishedSignActivity;
import com.mjict.signboardsurvey.activity.ReviewSignActivity;
import com.mjict.signboardsurvey.activity.SABaseActivity;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;

/**
 * Created by Junseo on 2016-12-23.
 */
public class SABaseActivityHandler extends DefaultSActivityHandler {
    private SABaseActivity activity;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SABaseActivity)getActivity();
        activity.setAddressSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddressSearch();
            }
        });

        activity.setDemolishedSignButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDemolishedSign();
            }
        });

        activity.setReviewSignButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReviewSign();
            }
        });
    }

    private void goToAddressSearch() {
        Intent intent = new Intent(activity, AddressSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, AddressSearchActivityHandler.class);

        activity.startActivity(intent);
    }

    private void goToDemolishedSign() {
        Intent intent = new Intent(activity, DemolishedSignActivity.class);
        intent.putExtra(HANDLER_CLASS, DemolishedSignActivityHandler.class);

        activity.startActivity(intent);
    }

    private void goToReviewSign() {
        Intent intent = new Intent(activity, ReviewSignActivity.class);
        intent.putExtra(HANDLER_CLASS, ReviewSignActivityHandler.class);

        activity.startActivity(intent);
    }
}
