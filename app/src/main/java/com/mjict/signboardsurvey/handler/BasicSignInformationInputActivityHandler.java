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
import com.mjict.signboardsurvey.activity.CameraActivity;
import com.mjict.signboardsurvey.activity.ExtraSignInformationInputActivity;
import com.mjict.signboardsurvey.activity.PictureActivity;
import com.mjict.signboardsurvey.activity.SignMeasureActivity;
import com.mjict.signboardsurvey.model.AutoJudgementValue;
import com.mjict.signboardsurvey.model.IconItem;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.util.WrongDataInputtedException;
import com.mjict.signboardsurvey.util.WrongNumberFormatException;
import com.mjict.signboardsurvey.util.WrongNumberScopeException;

/**
 * Created by Junseo on 2016-11-18.
 */
public class BasicSignInformationInputActivityHandler extends SABaseActivityHandler {
    private static final int MAX_INPUT_VALUE = 999;

    private BasicSignInformationInputActivity activity;
    private Sign currentSign;
    private String imagePath;

    private Setting[] statusSettings;
    private Setting[] lightSettings;
    private Setting[] signTypeSettings;

    // value for auto judgement
    private AutoJudgementValue autoJudgementValue;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        activity = (BasicSignInformationInputActivity)this.getActivity();

        // init
        Intent intent = activity.getIntent();
        currentSign = (Sign)intent.getSerializableExtra(MJConstants.SIGN);
        if(currentSign == null) {
            // 새 간판 입력
            currentSign = createNewSign();
        }
        autoJudgementValue = (AutoJudgementValue)intent.getSerializableExtra(MJConstants.AUTO_JUDGEMENT_VALUE);
        if(autoJudgementValue == null)
            autoJudgementValue = new AutoJudgementValue();


        if(currentSign.getPicNumber() != null && currentSign.getPicNumber().equals("") == false)
            imagePath = SyncConfiguration.getDirectoryForSingPicture(currentSign.isSynchronized())+currentSign.getPicNumber();

        SettingDataManager smgr = SettingDataManager.getInstance();
        statusSettings = smgr.getSignStatus();
        lightSettings = smgr.getLightTypes();
        signTypeSettings = smgr.getSignTypes();

        initSpinnerData();

        // register listener
//        activity.setSi
        activity.setAddSignImageButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCamera();
            }
        });

        activity.setSignImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignPicture();
            }
        });

        activity.setNextButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonClicked();

            }
        });

        activity.setBackImageButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(Activity.RESULT_CANCELED);
            }
        });


        // do first job
        updateToUI();

        startToLoadSignImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MJConstants.REQUEST_TAKE_AND_SAVE) {
            if(resultCode == Activity.RESULT_OK) {
                Boolean result = data.getBooleanExtra(MJConstants.RESPONSE, false);
                if(result == false) {
                    // TODO 사진 저장 실패
                } else {
                    // 사진 찍기 성공
                    imagePath = data.getStringExtra(MJConstants.PATH);

                    // 초점거리, 화각 등의 값을 가져온다
                    float horizontalAngle = data.getFloatExtra(MJConstants.HORIZONTAL_ANGLE, -1);
                    float verticalAngle = data.getFloatExtra(MJConstants.VERTICAL_ANGLE, -1);
                    float zoom = data.getFloatExtra(MJConstants.CAMERA_ZOOM, -1);

                    goToSignMeasurement(horizontalAngle, verticalAngle, zoom);
                }
            }
        } else if (requestCode == MJConstants.REQUEST_MEASURE_SIGN) {
            if(resultCode == Activity.RESULT_OK) {
                float width = data.getFloatExtra(MJConstants.SIZE_X, 0)/1000;
                float length = data.getFloatExtra(MJConstants.SIZE_Y, 0)/1000;
                float area = width*length;

                width = Float.parseFloat(String.format("%.2f",width));
                length = Float.parseFloat(String.format("%.2f",length));
                area = Float.parseFloat(String.format("%.2f",area));

                currentSign.setWidth(width);
                currentSign.setLength(length);
                currentSign.setArea(area);

                updateToUI();
            } else {
                Toast.makeText(activity, R.string.sign_measure_canceled, Toast.LENGTH_SHORT).show();
            }

            startToLoadSignImage();
        } else if(requestCode == MJConstants.REQUEST_SIGN_INFORMATION){
            if(resultCode == Activity.RESULT_OK) {
                currentSign = (Sign)data.getSerializableExtra(MJConstants.SIGN);

                Intent responseIntent = new Intent();
                responseIntent.putExtra(MJConstants.SIGN, currentSign);
                activity.setResult(Activity.RESULT_OK, responseIntent);
                activity.finish();

            } else {
                // nothing to do
            }

        } else {

        }
    }

    @Override
    public void onBackPressed() {
        // TODO imagePath 이미지 지우기
        activity.setResult(Activity.RESULT_CANCELED);

        super.onBackPressed();
    }

    @Override
    public void onActivityDestroy() {
        // TODO imagePath 이미지 지우기


        super.onActivityDestroy();
    }

    private void initSpinnerData() {
        for(int i=0; i<statusSettings.length; i++)
            activity.addToStatusSpinner(statusSettings[i].getCode(), statusSettings[i]);  // TODO 원래 이렇게 직접 넣는게 아니라 wrapper 클래스를 만들어 넣어 주는게 맞을 듯

        for(int i=0; i<lightSettings.length; i++) {
            Setting s = lightSettings[i];
            IconItem item = new IconItem(-1, s);
            activity.addToLightSpinner(item);
        }

        for(int i=0; i<signTypeSettings.length; i++)
            activity.addToSignTypeSpinner(signTypeSettings[i].getCode(), signTypeSettings[i]);
    }

    private void updateToUI() {
        if(currentSign == null)
            return;

        String content = currentSign.getContent();
        String width = String.format("%.2f", currentSign.getWidth());
        String length = String.format("%.2f", currentSign.getLength());
        String height = String.format("%.2f", currentSign.getHeight());

        String placedFloorText = String.valueOf(currentSign.getPlacedFloor());
        String totalFloorText = String.valueOf(currentSign.getTotalFloor());
        boolean frontChecked = currentSign.isFront();
        boolean intersectionChecked = currentSign.isIntersection();
        boolean frontBackChecked = currentSign.isFrontBackRoad();

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
        activity.setStatusSpinnerSelection(currentSign.getStatsCode());
        activity.setSignTypeSpinnerSelection(currentSign.getType());

        activity.setPlacedFloorText(placedFloorText);
        activity.setTotalFloorText(totalFloorText);
        activity.setFrontChecked(frontChecked);
        activity.setIntersectionChecked(intersectionChecked);
        activity.setFrontBackChecked(frontBackChecked);
    }

    private void nextButtonClicked() {
        String content = activity.getInputContent();
        Setting statusSetting = (Setting)activity.getSelectedStatus();
        Setting signTypeSetting = (Setting)activity.getSelectedSignType();
        String widthText = activity.getInputWidth();
        String lengthText = activity.getInputLength();
        String heightText = activity.getInputHeight();
        Setting lightSetting = (Setting)activity.getSelectedLight().obj;
        String picName = (imagePath == null) ? "" : imagePath.substring(imagePath.lastIndexOf("/")+1);

        String placedFloorText = activity.setInputPlacedFloor();
        String totalFloorText = activity.getInputTotalFloor();
        boolean isFront = activity.getFrontChecked();
        boolean isIntersection = activity.getIntersectionChecked();
        boolean isFrontBack = activity.getFrontBackChecked();

        // 필수 내용 체크
        try {
            checkCriticalItems(content, widthText, lengthText, placedFloorText, totalFloorText);
        } catch(WrongDataInputtedException e) {
            Toast.makeText(activity, e.getCauseString(), Toast.LENGTH_SHORT).show();
            return;
        }

        // 숫자값 체크
        try {
            checkNumberValue(widthText, lengthText, heightText, placedFloorText, totalFloorText);
        } catch (WrongNumberFormatException e) {
            String cause = activity.getString(R.string.wrong_number_type_input, e.getCauseValue());
            Toast.makeText(activity, cause, Toast.LENGTH_SHORT).show();
            return;
        } catch (WrongNumberScopeException e) {
            Toast.makeText(activity, R.string.wrong_number_scope_input, Toast.LENGTH_SHORT).show();
            return;
        }

        //
        currentSign.setContent(content);
        currentSign.setType(signTypeSetting.getCode());
        currentSign.setStatsCode(statusSetting.getCode());
        currentSign.setWidth(Float.parseFloat(widthText));
        currentSign.setLength(Float.parseFloat(lengthText));
        currentSign.setHeight(Float.parseFloat(heightText));
        currentSign.setLightType(lightSetting.getCode());
        currentSign.setPlacedFloor(Integer.parseInt(placedFloorText));
        currentSign.setTotalFloor(Integer.parseInt(totalFloorText));
        currentSign.setFront(isFront);
        currentSign.setIntersection(isIntersection);
        currentSign.setFrontBackRoad(isFrontBack);
        currentSign.setPicNumber(picName);

        goToSignExtraInformation();
    }

    private void checkCriticalItems(String content, String width, String length, String placedFloor, String totalFloor) throws WrongDataInputtedException {
        if(content.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_sign_content);
        }
        if(width.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_sign_width);
        }
        if(length.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_sign_length);
        }
        if(placedFloor.equals(""))
            throw new WrongDataInputtedException(-1, R.string.input_placed_floor);
        if(totalFloor.equals(""))
            throw new WrongDataInputtedException(-1, R.string.input_total_floor);
    }

    private void checkNumberValue(String widthText, String lengthText, String heightText, String placedFloorText,
                                    String totalFloorText) throws WrongNumberFormatException, WrongNumberScopeException {
        float width = 0;
        float length = 0;
        float height = 0;
        int placedFloor = 0;
        int totalFloor = 0;

        if(widthText.equals("") == false)
            width = Utilities.stringToFloat(widthText);
        if(lengthText.equals("") == false)
            length = Utilities.stringToFloat(lengthText);
        if(heightText.equals("") == false)
            height = Utilities.stringToFloat(heightText);
        if(placedFloorText.equals("") == false)
            placedFloor = Utilities.stringToInt(placedFloorText);
        if(totalFloorText.equals("") == false)
            totalFloor = Utilities.stringToInt(totalFloorText);

        if(width < 0 || length < 0 || height < 0 || placedFloor < 0 || totalFloor < 0)
            throw new WrongNumberScopeException(-1);

        if(width > MAX_INPUT_VALUE || length > MAX_INPUT_VALUE || height > MAX_INPUT_VALUE || placedFloor > MAX_INPUT_VALUE || totalFloor > MAX_INPUT_VALUE)
            throw new WrongNumberScopeException(-1);

    }

    private void goToSignExtraInformation() {
        Intent intent = new Intent(activity, ExtraSignInformationInputActivity.class);
        intent.putExtra(HANDLER_CLASS, ExtraSignInformationInputActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, currentSign);
        intent.putExtra(MJConstants.AUTO_JUDGEMENT_VALUE, autoJudgementValue);

        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_INFORMATION);
    }

    private void goToSignPicture() {
        String path = SyncConfiguration.getDirectoryForSingPicture(currentSign.isSynchronized())+currentSign.getPicNumber();

        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(HANDLER_CLASS, SignPictureActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);

        activity.startActivity(intent);
    }

    private void goToCamera() {
        String time = Utilities.getCurrentTimeAsString();
        int hash = Math.abs((int)Utilities.hash(time));
        String dir = SyncConfiguration.getDirectoryForSingPicture(currentSign.isSynchronized());
        final String fileName = String.format("sign_%10d.jpg", hash);
        String path = dir + fileName;

        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(HANDLER_CLASS, CameraActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);

        activity.startActivityForResult(intent, MJConstants.REQUEST_TAKE_AND_SAVE);
    }

    private void goToSignMeasurement(float horizontalAngle, float verticalAngle, float zoom) {
        String path = imagePath;

        Intent intent = new Intent(activity.getApplicationContext(), SignMeasureActivity.class);
        intent.putExtra(HANDLER_CLASS, SignMeasureActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);
        intent.putExtra(MJConstants.HORIZONTAL_ANGLE, horizontalAngle);
        intent.putExtra(MJConstants.VERTICAL_ANGLE, verticalAngle);
        intent.putExtra(MJConstants.CAMERA_ZOOM, zoom);

        activity.startActivityForResult(intent, MJConstants.REQUEST_MEASURE_SIGN);
    }

    private void startToLoadSignImage() {
        if(imagePath == null)
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
                        activity.setSignImage(values[0].image);
                    else
                        activity.setSignImage(R.drawable.ic_no_image); // 이미지 없거나 로드 실패
                }
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
            }
        });
        task.execute(imagePath);
    }

    private Sign createNewSign() {
        String currentTime = Utilities.getCurrentTimeAsString();

//        long id = -1;
//        int type = -1;
//        float width = 0f;
//        float length = 0f;
//        float height = 0f;
//        String area = "";   // 안쓰는거 같음
//        float extraSize = 0f;   // 안쓰는거 같음
//        int quantity = 0;   // 안쓰는거 같음
//        String content = "";
//        int placedFloor = 0;
//        String placedSide = "N";
//        int lightType = -1;
//        String placement = "";  // 안쓰는거 같음 - 나중에 쓸줄 모름
//        boolean streetInfringement = false;
//        float collisionWidth = 0f;
//        float collisionLength = 0f;
//        int inspectionResult = -1;
//        String permissionNumber = "";   // 안쓰는거 같음
//        String inputor = MJContext.getCurrentUser().getUserId();
//        String inputDate = currentTime;
//        String needReinspection = "";   // 안쓰는거 같음 - reviewCode로 대체 될 듯
//        int statusCode = -1;
//        String picNumber = "";
//        String modifier = "";
//        String modifyDate = currentTime;
//        int totalFloor = 0;
//        String isIntersection = "N";
//        int tblNumber = 510;
//        int addressId = -1;
//        String demolitionPicPath = "";
//        String demolishedDate = "";
//        boolean isDeleted = false;
//        int reviewCode = -1;
//        boolean isFrontBackRoad = false;

        long id = -1;		// id
        String inspectionNumber = "";
        String inspectionDate = "";
        String mobileId = String.valueOf(MJContext.getDeviceNumber());
        boolean isSynchronized = false;
        String syncDate = "";
        int type = -1;
        float width = 0f;
        float length = 0f;
        float height = 0f;
        float area = 0f;
        float extraSize = 0f;
        int quantity = 0;
        String content = "";
        int placedFloor = 0;
        boolean isFront = false;
        int lightType = -1;
        String placement = "";
        boolean isCollision = false;
        float collisionWidth = 0f;
        float collisionLength = 0f;
        int inspectionResult = -1;
        String permissionNumber = "";
//        String needReinspection = "";
        String inputter = MJContext.getCurrentUser().getUserId();
        String inputDate = "";
        int statsCode = -1;
        String picNumber = "";
        String modifier = MJContext.getCurrentUser().getUserId();;
        String modifyDate = "";
        int totalFloor = 0;
        boolean isIntersection = false;
        int tblNumber = 510;
//        boolean isDeleted = false;
        boolean isFrontBackRoad = false;
        String demolitionPicPath = "";
        String demolishedDate = "";
        int reviewCode = -1;
        long shopId = -1;
        int addressId = -1;
        String sgCode = "";
        int placedSide = -1;
        int uniqueness = -1;
        String memo = "";

        Sign sign = new Sign(id, inspectionNumber,inspectionDate,mobileId,isSynchronized,syncDate,
                type,width,length,height,area,extraSize,quantity,content,placedFloor,isFront,lightType,
                placement,isCollision,collisionWidth,collisionLength,inspectionResult,permissionNumber,
                inputter,inputDate, statsCode,picNumber,modifier,modifyDate,
                totalFloor,isIntersection,tblNumber,isFrontBackRoad,demolitionPicPath,
                demolishedDate,reviewCode,addressId,addressId,sgCode,placedSide,uniqueness,memo);

        return sign;
    }


}
