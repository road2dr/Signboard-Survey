package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.SignInformationActivity;
import com.mjict.signboardsurvey.activity.SignListActivity;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.SignInfo;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.LoadSignsByShopTask;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.ArrayList;

/**
 * Created by Junseo on 2016-11-15.
 */
public class SignListActivityHandler extends DefaultSActivityHandler {
    private SignListActivity activity;
    private Shop currentShop;
    private ArrayList<Sign> shopSigns;
    private LoadImageTask signImageLoadTask;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SignListActivity)getActivity();

        // register listener
        activity.setSignListOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToSignInformation(position);
            }
        });

        // init
        Intent intent = activity.getIntent();
        currentShop = (Shop)intent.getSerializableExtra(MJConstants.SHOP);
        if(currentShop == null) {
            // TODO 상점 정보 없음
            activity.finish();
            return;
        }
        shopSigns = new ArrayList<>();

        SettingDataManager smgr = SettingDataManager.getInstance();
        Setting categorySetting = smgr.getShopCategory(currentShop.getCategory());
        String category = categorySetting == null ? smgr.getDefaultShopCategoty() : categorySetting.getName();
        activity.setShopCategoryText(category);
        activity.setShopNameText(currentShop.getName());

        // do first job
        startToLoadSignList();
    }

    @Override
    public void onActivityStop() {
        if(signImageLoadTask != null && signImageLoadTask.isCancelled() == false)
            signImageLoadTask.cancel(true);

        super.onActivityStop();
    }

    private void startToLoadSignList() {
        LoadSignsByShopTask task = new LoadSignsByShopTask(activity.getApplicationContext());
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<Sign, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_sign_information);
            }
            @Override
            public void onTaskProgressUpdate(Sign... signs) {
                if(signs != null) {
                    for(int i=0; i<signs.length; i++) {
                        Sign s = signs[0];
                        shopSigns.add(s);

                        SettingDataManager smgr = SettingDataManager.getInstance();

                        // update to ui
                        String content = s.getContent();
                        String size = s.getWidth()+" X "+s.getLength();
                        if(s.getHeight() != 0)
                            size = size + " X "+s.getHeight();

                        Setting statusSetting = smgr.getSignStatus(s.getStatusCode());
                        Setting lightSetting = smgr.getLightType(s.getLightType());
                        Setting resultSetting = smgr.getResult(s.getInspectionResult());

                        String status = statusSetting == null ? smgr.getDefaultShopStatusName() : statusSetting.getName();
                        String light = lightSetting == null ? smgr.getDefaultLightTypeName() : lightSetting.getName();
                        String result = resultSetting == null ? smgr.getDefaultResultName() : resultSetting.getName();
                        String location = s.getPlacedFloor() +" / "+s.getTotalFloor();
                        SignInfo si = new SignInfo(null, content, size, status, light, location, result);
                        activity.addToList(si);

                        startToLoadSignImages();
                    }
                }
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
            }
        });
        task.execute(currentShop);
    }

    private void startToLoadSignImages() {
        String[] paths = new String[shopSigns.size()];
        for(int i=0; i<shopSigns.size(); i++) {
            Sign sign = shopSigns.get(i);
            String path = SyncConfiguration.getDirectoryForSingPicture() + sign.getPicNumber();
            paths[i] = path;
        }

        signImageLoadTask = new LoadImageTask();
        signImageLoadTask.setSampleSize(8);
        signImageLoadTask.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                for(int i=0; i<values.length; i++)
                    activity.setSignImage(values[i].index, values[i].image);
            }
            @Override
            public void onTaskFinished(Boolean result) {
            }
        });
        signImageLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paths);
    }

    private void goToSignInformation(int position) {
        Sign sign = shopSigns.get(position);

        Intent intent = new Intent(activity, SignInformationActivity.class);
        intent.putExtra(HANDLER_CLASS, SignInformationActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, sign);
        activity.startActivity(intent);
    }
}
