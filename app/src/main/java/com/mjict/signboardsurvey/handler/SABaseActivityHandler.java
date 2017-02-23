package com.mjict.signboardsurvey.handler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.AddressSearchActivity;
import com.mjict.signboardsurvey.activity.DemolishedSignActivity;
import com.mjict.signboardsurvey.activity.MapSearchActivity;
import com.mjict.signboardsurvey.activity.ReviewSignActivity;
import com.mjict.signboardsurvey.activity.SABaseActivity;
import com.mjict.signboardsurvey.activity.SettingActivity;
import com.mjict.signboardsurvey.activity.SummaryActivity;
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

        activity.setHomeButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSummary();
            }
        });

        activity.setQuitButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askAndQuit();
            }
        });

        activity.setSettingButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSetting();
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
        activity.closeDrawer();
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
        activity.closeDrawer();
        Intent intent = new Intent(activity, ReviewSignActivity.class);
        intent.putExtra(HANDLER_CLASS, ReviewSignActivityHandler.class);

        activity.startActivity(intent);
    }

    private void goToMapSearch() {
        activity.closeDrawer();
        Intent intent = new Intent(activity, MapSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, MapSearchActivityHandler.class);

        activity.startActivity(intent);
    }

    private void goToUserStatistics() {
        activity.closeDrawer();
        Intent intent = new Intent(activity, UserDataSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, UserDataSearchActivityHandler.class);

        activity.startActivity(intent);
    }

    private void goToSummary() {
        activity.closeDrawer();
        Intent intent = new Intent(activity, SummaryActivity.class);
        intent.putExtra(HANDLER_CLASS, SummaryActivityHandler.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        activity.startActivity(intent);
    }

    private void askAndQuit() {
        activity.closeDrawer();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.quit)
                .setMessage(R.string.do_you_want_to_quit)
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        startToQuit();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
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

    protected void goToSetting() {
        activity.closeDrawer();

        Intent intent = new Intent(activity, SettingActivity.class);

        activity.startActivity(intent);
    }









}
