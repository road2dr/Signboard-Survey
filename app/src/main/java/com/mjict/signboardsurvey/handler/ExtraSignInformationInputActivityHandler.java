package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.ExtraSignInformationInputActivity;
import com.mjict.signboardsurvey.autojudgement.InputType;
import com.mjict.signboardsurvey.model.AutoJudgementValue;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.util.WrongNumberFormatException;
import com.mjict.signboardsurvey.util.WrongNumberScopeException;
import com.mjict.signboardsurvey.widget.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        activity.setTimePickerConfirmButtonOnClickListener(new TimePickerDialog.ConfirmButtonOnClickListener() {
            @Override
            public void onClicked(Date time) {
                activity.hideTimePickerDialog();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
                String timeText = format.format(time);
                activity.setDemolishDateText(timeText);
            }
        });

        // do first job
        initSpinner();

        updateUI();
    }

    private void nextButtonClicked() {
        boolean collisionChecked = activity.getCollisionChecked();
        String collisionWidthText = activity.getInputCollisionWidth();
        String collisionLengthText = activity.getInputCollisionLength();
        Setting installedSideSetting = (Setting) activity.getSelectedInstalledSide();
        Setting uniquenessSetting = (Setting)activity.getSelectedUniqueness();
        String memo = activity.getInputMemo();
        String demolishDate = activity.getInputDemolishDate();
        Setting resultSetting = (Setting)activity.getSelectedResult();
        Setting reviewSetting = (Setting)activity.getSelectedReview();

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

        int judgement = Utilities.autoJudgement(currentSign.getType(), autoJudgementValue);
        if(judgement == -1)
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

        int installedSideCode = currentSign.getInstallSide();
        int uniquenessCode = currentSign.getUniqueness();
        String memo = currentSign.getMemo();

        String demolishDate = currentSign.getDemolishedDate();
        int result = currentSign.getInspectionResult();


//        if(status == 철거 ) {
//            activity.setDemolitionLayoutVisible(true);
//        } else {
//            activity.setDemolitionLayoutVisible(false);
//        }


        activity.setCollisionChecked(collisionChecked);
        activity.setCollisionWidthText(String.format("%.2f", collisionWidth));
        activity.setCollisionLengthText(String.format("%.2f", collisionLength));
        activity.setDemolishDateText(demolishDate);
        activity.setResultSpinnerSelection(result);
        activity.setMemoText(memo);
        activity.setInstalledSideSpinnerSelection(installedSideCode);
        activity.setUniquenessSpinnerSelection(uniquenessCode);
        activity.setReviewSpinnerSelection(currentSign.getReviewCode());
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
                    value = currentSign.getLightType();
                    break;

                case EQUATION_INSTALL_SIDE:
                    Setting s = (Setting)activity.getSelectedInstalledSide();
                    if(s != null)
                        value = s.getCode();
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
