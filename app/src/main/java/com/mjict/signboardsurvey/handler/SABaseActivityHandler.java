package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.AddressSearchActivity;
import com.mjict.signboardsurvey.activity.DemolishedSignActivity;
import com.mjict.signboardsurvey.activity.MapSearchActivity;
import com.mjict.signboardsurvey.activity.ReviewSignActivity;
import com.mjict.signboardsurvey.activity.SABaseActivity;
import com.mjict.signboardsurvey.activity.UserDataSearchActivity;
import com.mjict.signboardsurvey.receiver.WiFiMonitor;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.QuitTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;

/**
 * Created by Junseo on 2016-12-23.
 */
public class SABaseActivityHandler extends DefaultSActivityHandler {
    private SABaseActivity activity;

    private static WiFiMonitor wiFiMonitor = null;

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

        activity.setMapSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMapSearch();
            }
        });

        activity.setUserStatisticsButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserStatistics();
            }
        });

        if(wiFiMonitor == null) {
            wiFiMonitor = new WiFiMonitor(activity);
//            activity.registerReceiver(wiFiMonitor, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }

        activity.registerReceiver(wiFiMonitor, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    @Override
    public void onActivityDestroy() {

//        if(wi)
        // TODO 카메라 찍기 화면 에서 뒤로 갈 때 exception 발생 나중에 고쳐
        // : Unable to destroy activity {com.mjict.signboardsurvey/com.mjict.signboardsurvey.activity.CameraActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'void com.mjict.signboardsurvey.activity.SABaseActivity.unregisterReceiver(android.content.BroadcastReceiver)' on a null object reference
//        at android.app.ActivityThread.performDestroyActivity(ActivityThread.java:5145)
        if(activity != null)
            activity.unregisterReceiver(wiFiMonitor);

        super.onActivityDestroy();
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

    private void goToMapSearch() {
        Intent intent = new Intent(activity, MapSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, MapSearchActivityHandler.class);

        activity.startActivity(intent);
    }

    private void goToUserStatistics() {
        Intent intent = new Intent(activity, UserDataSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, UserDataSearchActivityHandler.class);

        activity.startActivity(intent);
    }

    protected void startToQuit() {
        QuitTask task = new QuitTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Void>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving_changes);
            }
            @Override
            public void onTaskFinished(Void aVoid) {
                activity.hideWaitingDialog();
                activity.finishAffinity();
            }
        });
        task.execute();
    }










}
