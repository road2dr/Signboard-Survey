package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.activity.AddressSearchActivity;
import com.mjict.signboardsurvey.activity.KeywordSearchActivity;
import com.mjict.signboardsurvey.activity.MapSearchActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.activity.SignInformationActivity;
import com.mjict.signboardsurvey.activity.SummaryActivity;
import com.mjict.signboardsurvey.activity.UserDataSearchActivity;
import com.mjict.signboardsurvey.adapter.SummaryStatisticsViewPagerAdapter;
import com.mjict.signboardsurvey.model.BitmapBuilding;
import com.mjict.signboardsurvey.model.BitmapSign;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.ui.RecentBuilding;
import com.mjict.signboardsurvey.model.ui.RecentSign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.SearchBitmapBuildingsByIdTask;
import com.mjict.signboardsurvey.task.SearchBitmapSignsByIdTask;
import com.mjict.signboardsurvey.util.SettingDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-11-09.
 */
public class SummaryActivityHandler extends SABaseActivityHandler {

    private SummaryActivity activity;

    private SearchBitmapSignsByIdTask recentSignSearchTask;
    private SearchBitmapBuildingsByIdTask recentBuildingSearchTask;

    private List<Building> recentBuildings;
    private List<Sign> recentSigns;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SummaryActivity)this.getActivity();

        // register listener
        activity.setSearchViewOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("junseo", "???");
                goToKeywordSearch();
            }
        });

        activity.setStatisticsViewPagerOnClickListener(new SummaryStatisticsViewPagerAdapter.PageOnClickListener() {
            @Override
            public void onPageClicked(int position) {
                goToUserDataSearch();
            }
        });

        activity.setRecentBuildingListOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Building building = recentBuildings.get(position);
                goToShopList(building);
            }
        });

        activity.setRecentSignListOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sign sign = recentSigns.get(position);
                goToSignInformation(sign);
            }
        });

//        activity.setAddressSearchSummaryButtonOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToAddressSearch();
//            }
//        });
//
//        activity.setMapSearchSummaryButtonOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToMapSearch();
//            }
//        });

        // init
        recentBuildings = new ArrayList<>();
        recentSigns = new ArrayList<>();

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
                recentSigns.clear();
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
                    recentSigns.add(s.sign);
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
                recentBuildings.clear();
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
                    recentBuildings.add(b.building);
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

    private void goToKeywordSearch() {
        Intent intent = new Intent(activity, KeywordSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, KeywordSearchActivityHandler.class);
        activity.startActivity(intent);
    }

    private void goToUserDataSearch() {
        Intent intent = new Intent(activity, UserDataSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, UserDataSearchActivityHandler.class);
        activity.startActivity(intent);
    }

    private void goToSignInformation(Sign sign) {
        Intent intent = new Intent(activity, SignInformationActivity.class);
        intent.putExtra(HANDLER_CLASS, SignInformationActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, sign);
        activity.startActivity(intent);
    }

    private void goToShopList(Building building) {
        Intent intent = new Intent(activity, ShopListActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopListActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, building);
        activity.startActivity(intent);
    }

    private void goToAddressSearch() {
        Intent intent = new Intent(activity, AddressSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, AddressSearchActivityHandler.class);
        activity.startActivity(intent);
    }

    private void goToMapSearch() {
        Intent intent = new Intent(activity, MapSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, MapSearchActivityHandler.class);
        activity.startActivity(intent);
    }
}
