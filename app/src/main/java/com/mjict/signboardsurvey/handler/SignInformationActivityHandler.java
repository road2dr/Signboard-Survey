package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BasicSignInformationInputActivity;
import com.mjict.signboardsurvey.activity.SignInformationActivity;
import com.mjict.signboardsurvey.autojudgement.InputType;
import com.mjict.signboardsurvey.model.AutoJudgementValue;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.ModifySignTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Junseo on 2016-11-17.
 */
public class SignInformationActivityHandler extends SABaseActivityHandler {
    private SignInformationActivity activity;

    private Sign currentSign;
    private ArrayList<Sign> shopSigns;

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
        shopSigns = (ArrayList<Sign>)intent.getSerializableExtra(MJConstants.SIGN_LIST);
        if(shopSigns == null) {
            // TODO 간판 정보를 불러와야 함.
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
        if(requestCode == MJConstants.REQUEST_SIGN_INPUT_INFORMATION) {
            if(resultCode == Activity.RESULT_OK) {
                Sign sign = (Sign) data.getSerializableExtra(MJConstants.SIGN);

                startToModifySignAndUpdateUI(sign);
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

        Setting typeSetting =  smgr.getSignType(currentSign.getType());
        Setting statusSetting = smgr.getSignStatus(currentSign.getStatsCode());

        String content = currentSign.getContent();
        String displayLocation = activity.getString(R.string.display_location_format, currentSign.getPlacedFloor(), currentSign.getTotalFloor());
        String type = typeSetting == null ? smgr.getDefaultSignTypeName() : typeSetting.getName();
        String status = statusSetting == null ? smgr.getDefaultSignStatus() : statusSetting.getName();
        String size = currentSign.getWidth() +" X "+currentSign.getLength();
        if(currentSign.getHeight() != 0)
            size = size + currentSign.getHeight();

        boolean isFront = currentSign.isFront();
        boolean isFrontBack = currentSign.isFrontBackRoad();
        boolean isIntersection = currentSign.isIntersection();

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
        String path = SyncConfiguration.getDirectoryForSingPicture(currentSign.isModified())+currentSign.getPicNumber();
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

    private void startToModifySignAndUpdateUI(final Sign sign) {
        final Sign target = createTargetSign(sign);

        ModifySignTask task = new ModifySignTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }

            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
                if(result == false) {
                    // TODO 수정 실패 - 사진 파일 삭제
                    responseResult = Activity.RESULT_CANCELED;
                    Toast.makeText(activity, R.string.failed_to_save_sign_information, Toast.LENGTH_SHORT);
                } else {
                    currentSign = target;

                    updateToUI();
                    startToLoadSignImage();

                    responseResult = Activity.RESULT_OK;
                    SyncConfiguration.increaseInspectionSeq();
                    MJContext.addRecentSing(currentSign.getId());

                    Toast.makeText(activity, R.string.succeeded_to_save, Toast.LENGTH_SHORT);
                }
            }
        });
        task.execute(target);
    }

    private Sign createTargetSign(Sign sign) {
        Sign tempSign = new Sign(currentSign);  // 수정에 실패 했을 대를 대비하기 위한 복사본

        // 입력받아온 정보를 넣어준다
        tempSign.setPicNumber(sign.getPicNumber());
        tempSign.setType(sign.getType());
        tempSign.setContent(sign.getContent());
        tempSign.setStatsCode(sign.getStatsCode());
        tempSign.setWidth(sign.getWidth());
        tempSign.setLength(sign.getLength());
        tempSign.setLightType(sign.getLightType());
        tempSign.setPlacedFloor(sign.getPlacedFloor());
        tempSign.setTotalFloor(sign.getTotalFloor());
        tempSign.setHeight(sign.getHeight());
        tempSign.setFront(sign.isFront());
        tempSign.setIntersection(sign.isIntersection());
        tempSign.setFrontBackRoad(sign.isFrontBackRoad());
        tempSign.setCollision(sign.isCollision());
        tempSign.setCollisionWidth(sign.getCollisionWidth());
        tempSign.setCollisionLength(sign.getCollisionLength());
        tempSign.setReviewCode(sign.getReviewCode());
        tempSign.setInstallSide(sign.getInstallSide());
        tempSign.setUniqueness(sign.getUniqueness());
        tempSign.setMemo(sign.getMemo());
        tempSign.setDemolitionPicPath(sign.getDemolitionPicPath());
        tempSign.setDemolishedDate(sign.getDemolishedDate());
        tempSign.setInspectionResult(sign.getInspectionResult());

        // 나머지 정보 채우기
        Calendar current = Calendar.getInstance();
        SimpleDateFormat josaDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        SimpleDateFormat syncTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
        String currentTimeString = Utilities.getCurrentTimeAsString();
        String josaDate = josaDateFormat.format(current.getTime());
        String syncDate = syncTimeFormat.format(SyncConfiguration.getLastSynchronizeDate());
        tempSign.setInspectionDate(josaDate);
        tempSign.setSyncDate(syncDate);
        tempSign.setModifyDate(currentTimeString);
        tempSign.setModifier(MJContext.getCurrentUser().getUserId());
        tempSign.setModified(true);
        if(tempSign.getInspectionNumber().equals("")) { // 조사 번호가 없으면 넣어준다
            long inspectionNo = SyncConfiguration.generateInspectionNo();
            tempSign.setInspectionNumber(String.valueOf(inspectionNo));
        }

        return tempSign;
    }

    private void goToSignInput() {
        AutoJudgementValue value = null;
        if(shopSigns != null)
            value = makeAutoJudgementValue(currentSign);

        Intent intent = new Intent(activity, BasicSignInformationInputActivity.class);
        intent.putExtra(HANDLER_CLASS, BasicSignInformationInputActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, currentSign);
        intent.putExtra(MJConstants.AUTO_JUDGEMENT_VALUE, value);
        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_INPUT_INFORMATION);
    }

    private void goToSignPicture() {

    }

    // 자동판단 값 생성
    private AutoJudgementValue makeAutoJudgementValue(Sign target) {
        InputType[] types = InputType.values();
        AutoJudgementValue autoJudgementValue = new AutoJudgementValue();

        for(int i=0; i<types.length; i++) {     // TODO 상수 지정 및 값 지정 방식에 대해 나중에 수정필요 파일로 지정 할 수 있으면 좋겠지
            InputType type = types[i];
            int value = -1;
            switch(type) {
                case SCOPE_HORIZONTAL_SIGN_COUNT:
                    value = getSignCount(1);  // TODO 가로형 간판 - 나중에 상수는 따로
                    break;

                case SCOPE_ROOFTOP_SIGN_COUNT:
                    value = getSignCount(5);    // TODO 옥상 간판 - 나중에 상수는 따로
                    break;

                case SCOPE_PILLAR_SIGN_COUNT:
                    value = getSignCount(6);    // TODO 지주 이용 간판 - 나중에 상수는 따로
                    break;

                case SCOPE_PROJECTED_SIGN_COUNT:
                    value = getSignCount(3);    // TODO 돌출 간판 - 나중에 상수는 따로
                    break;

                default:
                    value = -1;
            }
            autoJudgementValue.putValue(type, value);
        }

        return autoJudgementValue;
    }

    private int getSignCount(int type) {
        if(type == -1)
            return shopSigns.size();

        int value = 0;
        for(int j=0; j<shopSigns.size(); j++) {
            Sign s = shopSigns.get(j);
            if(s.getType() == type)
                value++;
        }

        return value;
    }
}
