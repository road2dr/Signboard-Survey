package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BasicSignInformationInputActivity;
import com.mjict.signboardsurvey.activity.SignInformationActivity;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;

/**
 * Created by Junseo on 2016-11-17.
 */
public class SignInformationActivityHandler extends SABaseActivityHandler {
    private SignInformationActivity activity;

    private Sign currentSign;


    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        activity = (SignInformationActivity)getActivity();

        // init
        Intent intent = activity.getIntent();
        currentSign = (Sign)intent.getSerializableExtra(MJConstants.SIGN);
        if(currentSign == null) {
            // TODO 에러
            activity.finish();
            return;
        }

        // register listener
        activity.setOptionMenuItemClickListener(new SignInformationActivity.OnOptionMenuItemClickListener() {
            @Override
            public void onModifySignClicked() {
                goToSignInput();
            }
        });

        activity.setSignImageViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignPicture();
            }
        });


        // do first job
        updateToUI();

        startToLoadSignImage();
    }

    private int responseResult = Activity.RESULT_CANCELED;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MJConstants.REQUEST_SIGN_INFORMATION) {
            if(resultCode == Activity.RESULT_OK) {
                Sign sign = (Sign) data.getSerializableExtra(MJConstants.SIGN);
                currentSign = sign;

                updateToUI();

                startToLoadSignImage();
                responseResult = Activity.RESULT_OK;
            } else {
                responseResult = Activity.RESULT_CANCELED;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent responseIntent = new Intent();
        responseIntent.putExtra(MJConstants.SIGN, currentSign);
        activity.setResult(responseResult, responseIntent);

        super.onBackPressed();
    }

    private void updateToUI() {
        SettingDataManager smgr = SettingDataManager.getInstance();
        // TODO 건물 위치 값에 대한 정의 필요 함. 기존의 isFront isIntersection 와의 값도 고려 해야 함.

        Setting typeSetting =  smgr.getSignType(currentSign.getType());
        Setting statusSetting = smgr.getSignStatus(currentSign.getStatusCode());

        String content = currentSign.getContent();
        String displayLocation = activity.getString(R.string.display_location_format, currentSign.getPlacedFloor(), currentSign.getTotalFloor());
        String type = typeSetting == null ? smgr.getDefaultSignTypeName() : typeSetting.getName();
        String status = statusSetting == null ? smgr.getDefaultSignStatus() : statusSetting.getName();
        String size = currentSign.getWidth() +" X "+currentSign.getLength();
        if(currentSign.getHeight() != 0)
            size = size + currentSign.getHeight();

        boolean isFront = currentSign.getPlacedSide().equals("Y") ? false : true;   // TODO 정면 도로. 확인 필요
        boolean isFrontBack = currentSign.isFrontBackRoad();
        boolean isIntersection = currentSign.getIsIntersection().equals("Y") ? true : false;

        activity.setContentText(content);
        activity.setDisplayLocationText(displayLocation);
        activity.setTypeText(type);
        activity.setStatusText(status);
        activity.setSizeText(size);
        activity.setFrontChecked(isFront);
        activity.setFrontBackChecked(isFrontBack);
        activity.setIntersectionChecked(isIntersection);
    }

//    private void startToModifySign(final Sign sign) {
//        ModifySignTask task = new ModifySignTask(activity.getApplicationContext());
//        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.saving);
//            }
//
//            @Override
//            public void onTaskFinished(Boolean result) {
//                activity.hideWaitingDialog();
//
//                if(result == false) {
//                    activity.showAlertDialog(R.string.failed_to_save_sign_information);
//                    return;
//                }
//
//                currentSign = sign;
//                signDataChanged = true;
//
//                updateToUI();
//
//                startToLoadSignImage();
//            }
//        });
//        task.execute(sign);
//    }

    private void startToLoadSignImage() {
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

    private void goToSignInput() {
        Intent intent = new Intent(activity, BasicSignInformationInputActivity.class);
        intent.putExtra(HANDLER_CLASS, BasicSignInformationInputActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, currentSign);
        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_INFORMATION);
    }

    private void goToSignPicture() {

    }
}
