package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.Bundle;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BasicSignInformationInputActivity;
import com.mjict.signboardsurvey.model.IconItem;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;

/**
 * Created by Junseo on 2016-11-18.
 */
public class BasicSignInformationInputActivityHandler extends DefaultSActivityHandler {

    private BasicSignInformationInputActivity activity;
    private Sign currentSign;

    private Setting[] statusSettings;
    private Setting[] lightSettings;


    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        activity = (BasicSignInformationInputActivity)this.getActivity();

        // init
        Intent intent = activity.getIntent();
        currentSign = (Sign)intent.getSerializableExtra(MJConstants.SIGN);

        SettingDataManager smgr = SettingDataManager.getInstance();
        statusSettings = smgr.getSignStatus();
        lightSettings = smgr.getLightTypes();

        initSpinnerData();

        // register listener


        // do first job
        updateToUI();

        startToLoadSignImage();
    }

    private void initSpinnerData() {
        for(int i=0; i<statusSettings.length; i++)
            activity.addToStatusSpinner(i, statusSettings[i]);

        for(int i=0; i<lightSettings.length; i++) {
            Setting s = lightSettings[i];
            IconItem item = new IconItem(-1, s);
            activity.addToLightSpinner(item);
        }
    }

    private void updateToUI() {
        if(currentSign == null)
            return;

        String content = currentSign.getContent();
        String width = String.format("%.2f", currentSign.getWidth());
        String length = String.format("%.2f", currentSign.getLength());
        String height = String.format("%.2f", currentSign.getHeight());

        int lightIndex = -1;
        for(int i=0; i<lightSettings.length; i++) {
            if(lightSettings[i].getCode() == currentSign.getLightType()) {
                lightIndex = i;
                break;
            }
        }

        activity.setContentText(content);
        activity.setWidthText(width);
        activity.setLengthText(length);
        activity.setHeightText(height);
        activity.setLightSpinnerSelection(lightIndex);
        activity.setStatusSpinnerSelection(currentSign.getStatusCode());
    }

    private void startToLoadSignImage() {
        if(currentSign == null || currentSign.getPicNumber().equals(""))
            return;

        String path = SyncConfiguration.getDirectoryForSingPicture()+currentSign.getPicNumber();
        LoadImageTask task = new LoadImageTask();
        task.setSampleSize(8);
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_sign_picture);
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                if(values == null)
                    return;

                if(values.length > 0)
                    activity.setSignImage(values[0].image);
            }

            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
            }
        });
        task.execute(path);
    }
}
