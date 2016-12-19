package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.activity.KeywordSearchActivity;
import com.mjict.signboardsurvey.activity.SummaryActivity;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.model.BitmapBuilding;
import com.mjict.signboardsurvey.model.BitmapSign;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.ui.RecentBuilding;
import com.mjict.signboardsurvey.model.ui.RecentSign;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.SearchBitmapBuildingsByIdTask;
import com.mjict.signboardsurvey.task.SearchBitmapSignsByIdTask;

/**
 * Created by Junseo on 2016-11-09.
 */
public class SummaryActivityHandler extends DefaultSActivityHandler {

    private SummaryActivity activity;

    private SearchBitmapSignsByIdTask recentSignSearchTask;
    private SearchBitmapBuildingsByIdTask recentBuildingSearchTask;
    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SummaryActivity)this.getActivity();
        // register listener
        activity.setSearchViewOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("junseo", "???");
                goToKeyworkSearch();
            }
        });

        // init

        // do first job
    }

    @Override
    public void onActivityStart() {
        recentSignSearchTask = new SearchBitmapSignsByIdTask(activity.getApplicationContext());
        recentBuildingSearchTask = new SearchBitmapBuildingsByIdTask(activity.getApplication());

        startToLoadRecentSigns();
        startToLoadRecentBuilding();

        super.onActivityStart();
    }

    private void startToLoadRecentSigns() {
        Long[] ids = MJContext.getRecentBuildings();
        recentSignSearchTask.setDefaultAsyncTaskListener(new AsyncTaskListener<BitmapSign, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.clearRecentSignList();
            }
            @Override
            public void onTaskProgressUpdate(BitmapSign... values) {
                SettingDataManager smgr = SettingDataManager.getInstance();
                for(int i=0; i<values.length; i++) {
                    if(recentSignSearchTask.isCancelled())
                        break;

                    BitmapSign s = values[i];
                    if(s == null)
                        continue;

                    String name = s.sign.getContent();
                    Setting typeSetting = smgr.getSignType(s.sign.getType());
                    String type = (typeSetting == null) ? smgr.getDefaultSignTypeName() : typeSetting.getName();
                    Setting resultSetting = smgr.getResult(s.sign.getInspectionResult());
                    String result = (resultSetting == null) ? smgr.getDefaultResultName() : typeSetting.getName();

                    RecentSign bs = new RecentSign(s.image, s.sign, name, type, result);
                    activity.addToRecentSign(bs);
                }
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
            }
        });
        recentSignSearchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ids);
    }

    private void startToLoadRecentBuilding() {
        Long[] ids = MJContext.getRecentBuildings();
        recentBuildingSearchTask.setDefaultAsyncTaskListener(new AsyncTaskListener<BitmapBuilding, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.clearRecentBuildingList();
            }
            @Override
            public void onTaskProgressUpdate(BitmapBuilding... values) {
                for(int i=0; i<values.length; i++) {
                    if (recentSignSearchTask.isCancelled())
                        break;

                    BitmapBuilding b = values[i];
                    if (b == null)
                        continue;

                    Bitmap img = b.image;
                    Building building = b.building;
                    String secondBuildingName = building.getSecondBuildingNumber();
                    String bnum = (secondBuildingName != null && !secondBuildingName.equals("")) ? (building.getFirstBuildingNumber() + "-" + secondBuildingName) : building.getFirstBuildingNumber();
                    String name = building.getName().equals("") ? bnum : building.getName();
                    String address = building.getStreetName() + " " + bnum;

                    RecentBuilding rb = new RecentBuilding(img, building, name, address);
                    activity.addToRecentBuilding(rb);
                }
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
            }
        });
        recentBuildingSearchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ids);
    }

    @Override
    public void onActivityStop() {
        recentSignSearchTask.cancel(true);
        recentBuildingSearchTask.cancel(true);

        super.onActivityStop();
    }

    private void goToKeyworkSearch() {
        Intent intent = new Intent(activity, KeywordSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, KeywordSearchActivityHandler.class);
        activity.startActivity(intent);
    }
}
