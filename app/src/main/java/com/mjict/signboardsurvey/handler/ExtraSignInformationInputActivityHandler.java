package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.CameraActivity;
import com.mjict.signboardsurvey.activity.ExtraSignInformationInputActivity;
import com.mjict.signboardsurvey.autojudgement.InputType;
import com.mjict.signboardsurvey.model.AutoJudgementValue;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.util.WrongNumberFormatException;
import com.mjict.signboardsurvey.util.WrongNumberScopeException;
import com.mjict.signboardsurvey.widget.TimePickerDialog;

import java.util.Date;

/**
 * Created by Junseo on 2016-11-18.
 */
public class ExtraSignInformationInputActivityHandler extends SABaseActivityHandler {
    private static final int MAX_INPUT_VALUE = 999;

    private ExtraSignInformationInputActivity activity;
    private String demolishPicPath;
    private Sign currentSign;
    private AutoJudgementValue autoJudgementValue;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        // init
        activity = (ExtraSignInformationInputActivity)getActivity();
        Intent intent = activity.getIntent();
        currentSign = (Sign)intent.getSerializableExtra(MJConstants.SIGN);
        if(currentSign == null) {
            // TODO 에러
            activity.setResult(Activity.RESULT_CANCELED);
            activity.finish();
            return;
        }

        autoJudgementValue = (AutoJudgementValue)intent.getSerializableExtra(MJConstants.AUTO_JUDGEMENT_VALUE);
        if(autoJudgementValue == null) {
            autoJudgementValue = new AutoJudgementValue();
        }

        if(currentSign.getDemolitionPicPath() != null && currentSign.getDemolitionPicPath().equals("") == false)
            demolishPicPath = SyncConfiguration.getDirectoryForSingPicture(currentSign.isDemolishPicModified())+currentSign.getDemolitionPicPath();

        // register listener
        activity.setBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
            }
        });

        activity.setNextButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonClicked();
            }
        });

        activity.setAutoJudgementButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAutoJudgement();
            }
        });

        activity.setDemolishDateTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showTimePickerDialog();
            }
        });

        activity.addPicButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCamera();
            }
        });

        activity.setTimePickerConfirmButtonOnClickListener(new TimePickerDialog.ConfirmButtonOnClickListener() {
            @Override
            public void onClicked(Date time) {
                activity.hideTimePickerDialog();
                activity.setDemolishDate(time);
            }
        });

        activity.setCollisionCheckBoxOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.setCollisionWidthAndLengthEnabled(isChecked);
            }
        });

        // do first job
        initSpinner();

        updateUI();

        startToLoadDemolishImage();
    }

    boolean imageChanged = false;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == MJConstants.REQUEST_TAKE_AND_SAVE) {
            if(resultCode == Activity.RESULT_OK) {
                Boolean result = data.getBooleanExtra(MJConstants.RESPONSE, false);
                if(result == false) {
                    // TODO 사진 저장 실패
                    imageChanged = false;
                } else {
                    // 사진 찍기 성공
                    imageChanged = true;
                    demolishPicPath = data.getStringExtra(MJConstants.PATH);

                    startToLoadDemolishImage();
                }
            }
        }
    }

    private void nextButtonClicked() {
        boolean collisionChecked = activity.getCollisionChecked();
        String collisionWidthText = activity.getInputCollisionWidth();
        String collisionLengthText = activity.getInputCollisionLength();
        Setting installedSideSetting = (Setting) activity.getSelectedInstalledSide();
        Setting uniquenessSetting = (Setting)activity.getSelectedUniqueness();
        String memo = activity.getInputMemo();
        String demolishDate = Utilities.dayToString(activity.getInputDemolishDate());
        Setting resultSetting = (Setting)activity.getSelectedResult();
        Setting reviewSetting = (Setting)activity.getSelectedReview();
        String picPath = (demolishPicPath == null) ? "" : demolishPicPath.substring(demolishPicPath.lastIndexOf("/")+1);

        try {
            checkNumberValue(collisionWidthText, collisionLengthText);
        } catch (WrongNumberFormatException e) {
            String cause = activity.getString(R.string.wrong_number_type_input, e.getCauseValue());
            Toast.makeText(activity, cause, Toast.LENGTH_SHORT).show();
            return;
        } catch (WrongNumberScopeException e) {
            Toast.makeText(activity, R.string.wrong_number_scope_input, Toast.LENGTH_SHORT).show();
            return;
        }

        currentSign.setDemolitionPicPath(picPath);
        currentSign.setDemolishPicModified(imageChanged);
        currentSign.setCollision(collisionChecked);
        currentSign.setInstallSide(installedSideSetting.getCode());
        currentSign.setUniqueness(uniquenessSetting.getCode());
        currentSign.setMemo(memo);
        currentSign.setDemolishedDate(demolishDate);
        currentSign.setInspectionResult(resultSetting.getCode());
        currentSign.setCollisionWidth(Float.parseFloat(collisionWidthText));
        currentSign.setCollisionLength(Float.parseFloat(collisionLengthText));
        currentSign.setReviewCode(reviewSetting.getCode());

        Intent responseIntent = new Intent();
        responseIntent.putExtra(MJConstants.SIGN, currentSign);
        activity.setResult(Activity.RESULT_OK, responseIntent);
        activity.finish();
    }

    private void doAutoJudgement() {
        putAutoJudgementValues();

        String judgement = Utilities.autoJudgement(currentSign.getType(), autoJudgementValue);
        if(judgement.equals("-1"))
            return;

        activity.setResultSpinnerSelection(judgement);
    }

    private void initSpinner() {
        SettingDataManager smgr = SettingDataManager.getInstance();
        Setting[] installedSettings = smgr.getInstallSides();
        Setting[] uniquenessSettings = smgr.getUniqueness();
        Setting[] resultSettings = smgr.getResults();
        Setting[] reviewSettings = smgr.getReviewCodes();

        for(int i=0; i<resultSettings.length; i++)
            activity.addToResultSpinner(resultSettings[i].getCode(), resultSettings[i]);
        for(int i=0; i<installedSettings.length; i++)
            activity.addToInstalledSideSpinner(installedSettings[i].getCode(), installedSettings[i]);
        for(int i=0; i<uniquenessSettings.length; i++)
            activity.addToUniquenessSpinner(uniquenessSettings[i].getCode(), uniquenessSettings[i]);
        for(int i=0; i<reviewSettings.length; i++)
            activity.addToReviewSpinner(reviewSettings[i].getCode(), reviewSettings[i]);
    }

    private void updateUI() {
        boolean collisionChecked = currentSign.isCollision();
        float collisionWidth = currentSign.getCollisionWidth();
        float collisionLength = currentSign.getCollisionLength();

        Object installedSideCode = currentSign.getInstallSide();
        Object uniquenessCode = currentSign.getUniqueness();
        String memo = currentSign.getMemo();

        String demolishDate = currentSign.getDemolishedDate();
        Object result = currentSign.getInspectionResult();

        Date demolishTime = Utilities.stringToDay(currentSign.getDemolishedDate());
//        if(status == 철거 ) {
//            activity.setDemolitionLayoutVisible(true);
//        } else {
//            activity.setDemolitionLayoutVisible(false);
//        }


        activity.setCollisionChecked(collisionChecked);
        activity.setCollisionWidthAndLengthEnabled(collisionChecked);
        activity.setCollisionWidthText(String.format("%.2f", collisionWidth));
        activity.setCollisionLengthText(String.format("%.2f", collisionLength));
        activity.setDemolishDate(demolishTime);
        activity.setResultSpinnerSelection(result);
        activity.setMemoText(memo);
        activity.setInstalledSideSpinnerSelection(installedSideCode);
        activity.setUniquenessSpinnerSelection(uniquenessCode);
        activity.setReviewSpinnerSelection(currentSign.getReviewCode());
    }

    private void startToLoadDemolishImage() {
        if(demolishPicPath == null)
            return;

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

                if(values.length > 0) {
                    if(values[0].image != null)
                        activity.setDemolitionPicImage(values[0].image);
                    else
                        activity.setDemolitionPicImage(R.drawable.ic_no_image); // 이미지 없거나 로드 실패
                }
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
            }
        });
        task.execute(demolishPicPath);
    }

    private void goToCamera() {
        String time = Utilities.getCurrentTimeAsString();
        int hash = Math.abs((int)Utilities.hash(time));
        String dir = SyncConfiguration.getDirectoryForSingPicture(true);
        final String fileName = String.format("sign_%010d.jpg", hash);
        String path = dir + fileName;

        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(HANDLER_CLASS, CameraActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);

        activity.startActivityForResult(intent, MJConstants.REQUEST_TAKE_AND_SAVE);
    }


    private void checkNumberValue(String collisionWidthText, String collisionLengthText)
            throws WrongNumberFormatException, WrongNumberScopeException {
        float collisionWidth = 0;
        float collisionLength = 0;

        if(collisionWidthText.equals("") == false)
            collisionWidth = Utilities.stringToFloat(collisionWidthText);
        if(collisionLengthText.equals("") == false)
            collisionLength = Utilities.stringToFloat(collisionLengthText);

        if(collisionWidth < 0 || collisionLength < 0)
            throw new WrongNumberScopeException(-1);

        if(collisionWidth > MAX_INPUT_VALUE || collisionLength > MAX_INPUT_VALUE)
            throw new WrongNumberScopeException(-1);

    }

    //    SCOPE_INSTALL_FLOOR_COUNT("scope_install_floor_count"),
//    SCOPE_HORIZONTAL_SIGN_COUNT("scope_horizontal_sign_count"),
//    SCOPE_WIDTH("scope_width"),
//    SCOPE_AREA("scope_area"),
//    SCOPE_LENGTH("scope_length"),
//    SCOPE_INSTALL_HEIGHT("scope_install_height"),
//    SCOPE_PROJECTED_SIGN_COUNT("scope_projected_sign_count"),
//    SCOPE_INSTALL_HEIGHT_TOP("scope_install_height_top"),
//    SCOPE_PILLAR_SIGN_COUNT("scope_pillar_sign_count"),
//    SCOPE_TOTAL_FLOOR_COUNT("scope_total_floor_count"),
//    SCOPE_ROOFTOP_SIGN_COUNT("scope_rooftop_sign_count"),
//    EQUATION_LIGHT("multi_equation_light"),
//    EQUATION_TOTAL_FLOOR_COUNT("multi_equation_install_floor_count"),
//    EQUATION_INSTALL_SIDE("multi_equation_install_side"),
//    EQUATION_INTERSECTION("single_equation_intersection"),

    private void putAutoJudgementValues() {


        InputType[] types = InputType.values();
        for(int i=0; i<types.length; i++) {
            InputType type = types[i];
            int value = -1;
            switch(type) {
                case SCOPE_INSTALL_FLOOR_COUNT:
                    value = currentSign.getPlacedFloor();
                    break;

                case SCOPE_WIDTH:
                    value = (int)currentSign.getWidth();
                    break;

                case SCOPE_AREA:
                    value = (int)(currentSign.getWidth()*currentSign.getLength());
                    break;

                case SCOPE_LENGTH:
                    value = (int)currentSign.getLength();
                    break;

                case SCOPE_INSTALL_HEIGHT:
                    value = (int)currentSign.getHeight();
                    break;

                case SCOPE_INSTALL_HEIGHT_TOP:
                    value = (int)(currentSign.getHeight()+currentSign.getLength());
                    break;

                case EQUATION_LIGHT:
                    value = Integer.valueOf(currentSign.getLightType());
                    break;

                case EQUATION_INSTALL_SIDE:
                    Setting s = (Setting)activity.getSelectedInstalledSide();
                    if(s != null)
                        value = Integer.valueOf(s.getCode());
                    break;

                case EQUATION_INTERSECTION:
                    value = currentSign.isIntersection() ? 1: 0;
                    break;

                default:
                    value = -1;
            }
            autoJudgementValue.putValue(type, value);
        }
    }

}
