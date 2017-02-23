package com.mjict.signboardsurvey.handler;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
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
import com.mjict.signboardsurvey.task.LoadAllSignStatusTask;
import com.mjict.signboardsurvey.task.LoadSignResultStatusTask;
import com.mjict.signboardsurvey.task.SearchBitmapBuildingsByIdTask;
import com.mjict.signboardsurvey.task.SearchBitmapSignsByIdTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
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
    private LoadSignResultStatusTask signResultStatusTask;
    private LoadAllSignStatusTask allSignStatusTask;

    private List<Building> recentBuildings;
    private List<Sign> recentSigns;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SummaryActivity)this.getActivity();
        activity.setFinishWithBackButton(false);

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

        activity.setRefreshStatisticsButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToLoadAllSignStatus();
            }
        });

        activity.setRefreshPieChartButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToLoadSignResultStatus();
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
        startToLoadSignResultStatus();
        startToLoadAllSignStatus();
    }

    @Override
    public void onActivityStart() {
        recentSignSearchTask = new SearchBitmapSignsByIdTask(activity.getApplicationContext());
        recentBuildingSearchTask = new SearchBitmapBuildingsByIdTask(activity.getApplication());

        startToLoadRecentSigns();
        startToLoadRecentBuilding();

        super.onActivityStart();
    }

    @Override
    public void onBackPressed() {
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

    private void startToLoadRecentSigns() {
        Long[] ids = MJContext.getRecentSings();
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
                    String result = (resultSetting == null) ? smgr.getDefaultResultName() : resultSetting.getName();

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

    private void startToLoadSignResultStatus() {
        signResultStatusTask = new LoadSignResultStatusTask(activity.getApplicationContext());
        signResultStatusTask.setLawfulSignTypes("01", "02", "04", "06", "03");
        signResultStatusTask.setIllegalSignTypes("08", "09", "10", "11");
        signResultStatusTask.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<long[]>() {
            @Override
            public void onTaskStart() {
                activity.clearSignResultStatusPieChart();
                activity.setSignStatusLoadingViewVisible(true);
                activity.setSignStatusPieChartVisible(false);
            }

            @Override
            public void onTaskFinished(long[] result) {
                if(signResultStatusTask.isCancelled())
                    return;

                activity.setSignStatusLoadingViewVisible(false);
                activity.setSignStatusPieChartVisible(true);

                if(result == null)
                    return;

                if(result.length != 3)
                    return;

                String etcSignType = activity.getString(R.string.etc_sign_type);
                String lawfulSignType = activity.getString(R.string.lawful_sign_type);
                String illegalSignType = activity.getString(R.string.illegal_sign_type);

                activity.addToSignResultStatusPieChart(lawfulSignType, result[0]);
                activity.addToSignResultStatusPieChart(illegalSignType, result[1]);
                activity.addToSignResultStatusPieChart(etcSignType, result[2]);

            }
        });
        signResultStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void startToLoadAllSignStatus() {
        allSignStatusTask = new LoadAllSignStatusTask(activity.getApplicationContext());
        allSignStatusTask.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Long[]>() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskFinished(Long[] result) {
                if(allSignStatusTask.isCancelled())
                    return;

                if(result == null)
                    return;

                if(result.length != 10)
                    return;

                String allBuildingCountText = activity.getString(R.string.number_of_case, result[0]);
                String allShopCountText = activity.getString(R.string.number_of_case, result[1]);
                String allSignCountText = activity.getString(R.string.number_of_case, result[2]);
                String reviewSignCountText = activity.getString(R.string.number_of_case, result[3]);
                String demolishSignCountText = activity.getString(R.string.number_of_case, result[4]);

                String userAllSignCountText = activity.getString(R.string.number_of_case, result[5]);
                String userTodaySignCountText = activity.getString(R.string.number_of_case, result[6]);
                String userReviewSignCountText = activity.getString(R.string.number_of_case, result[7]);
                String userShopCountText = activity.getString(R.string.number_of_case, result[8]);
                String userDemolishSignCountText = activity.getString(R.string.number_of_case, result[9]);

                activity.setFirstAllSignPageText(allBuildingCountText, allShopCountText, allSignCountText,
                        reviewSignCountText, demolishSignCountText);

                activity.setSecondAllSignPageText(userAllSignCountText, userTodaySignCountText, userReviewSignCountText,
                        userShopCountText, userDemolishSignCountText);
            }
        });
        allSignStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, MJContext.getCurrentUser().getUserId());
    }

    @Override
    public void onActivityStop() {
        recentSignSearchTask.cancel(true);
        recentBuildingSearchTask.cancel(true);
        signResultStatusTask.cancel(true);
        allSignStatusTask.cancel(true);

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
