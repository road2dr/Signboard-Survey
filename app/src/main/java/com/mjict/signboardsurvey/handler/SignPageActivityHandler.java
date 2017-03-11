package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.CameraActivity;
import com.mjict.signboardsurvey.activity.PictureActivity;
import com.mjict.signboardsurvey.activity.SignMeasureActivity;
import com.mjict.signboardsurvey.activity.SignPageActivity;
import com.mjict.signboardsurvey.adapter.SignViewPagerAdapter;
import com.mjict.signboardsurvey.autojudgement.InputType;
import com.mjict.signboardsurvey.model.AutoJudgementValue;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.ui.SignInputData;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.ModifySignTask;
import com.mjict.signboardsurvey.task.RegisterSignTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.MJUtilities;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.util.WrongDataInputtedException;
import com.mjict.signboardsurvey.util.WrongNumberFormatException;
import com.mjict.signboardsurvey.util.WrongNumberScopeException;
import com.mjict.signboardsurvey.widget.DemolitionImageOptionDialog;
import com.mjict.signboardsurvey.widget.SignImageOptionDialog;
import com.mjict.signboardsurvey.widget.SimpleSpinner;
import com.mjict.signboardsurvey.widget.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Junseo on 2017-02-13.
 */
public class SignPageActivityHandler extends DefaultSActivityHandler {

    private static final int MAX_VALUE = 999;

    private SignPageActivity activity;

    private Shop targetShop;
    private ArrayList<Sign> signs;
    private int initialIndex = 0;
    private Building targetBuilding;

//    private Sign oneModeSign;
    private boolean isOneSignMode = false;
    private int responseValue = Activity.RESULT_CANCELED;

    private AutoJudgementValue autoJudgementValue;



    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SignPageActivity)getActivity();
        activity.setFinishWithBackButton(false);    // 백버튼 누르면 바로 종료 되는게 아니고 체크해야 함

        Intent intent = activity.getIntent();
        targetShop = (Shop)intent.getSerializableExtra(MJConstants.SHOP);
        initialIndex = intent.getIntExtra(MJConstants.INDEX, -1);
        isOneSignMode = intent.getBooleanExtra(MJConstants.ONE_SIGN_MODE, false);
        signs = (ArrayList<Sign>)intent.getSerializableExtra(MJConstants.SIGN_LIST);
        targetBuilding = (Building)intent.getSerializableExtra(MJConstants.BUILDING);

        if(targetShop == null || signs == null) {
            // 업소 정보 없음 -> 종료
            Toast.makeText(activity, R.string.there_are_no_shop_or_sign_information, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_CANCELED);
            activity.finish();
            return;
        }

        autoJudgementValue = (AutoJudgementValue)intent.getSerializableExtra(MJConstants.AUTO_JUDGEMENT_VALUE);
        if(autoJudgementValue == null) {
            autoJudgementValue = new AutoJudgementValue();
        }

        // TODO 상점 정보 변경 기능은 여기서 빼자
//        activity.setShopConfirmButtonOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shopConfirmButtonClicked();
//            }
//        });

        activity.setConfirmButtonOnClickListener(new SignViewPagerAdapter.ConfirmButtonOnClickListener() {
            @Override
            public void onClick(int index, SignInputData inputData) {
                confirmButtonClicked(index, inputData);
            }
        });

        activity.setAddSignImageButtonOnClickListener(new SignViewPagerAdapter.AddSignImageButtonOnClickListener() {
            @Override
            public void onClick(int index) {
                addSignImageButtonClicked(index);
            }
        });

        activity.setSignImageOnClickListener(new SignViewPagerAdapter.SignImageOnClickListener() {
            @Override
            public void onClick(int index) {
                signImageClicked(index);
            }
            @Override
            public void onLongClick(int index) {
                signImageLongClicked(index);
            }
        });

        activity.setAddDemolitionImageButtonOnClickListener(new SignViewPagerAdapter.AddDemolitionImageButtonOnClickListener() {
            @Override
            public void onClick(int index) {
                addDemolitionImageButtonClicked(index);
            }
        });

        activity.setDemolitionImageOnClickListener(new SignViewPagerAdapter.DemolitionImageOnClickListener() {
            @Override
            public void onClick(int index) {
                demolitionImageClicked(index);
            }
            @Override
            public void onLongClick(int index) {
                demolitionImageLongClicked(index);
            }
        });

        activity.setDateTextOnClickListener(new SignViewPagerAdapter.DateTextOnClickListener() {
            @Override
            public void onClick(final int index) {
                activity.showTimePickerDialog();
                activity.setTimePickerConfirmButtonOnClickListener(new TimePickerDialog.ConfirmButtonOnClickListener() {
                    @Override
                    public void onClicked(Date time) {
                        activity.hideTimePickerDialog();
                        activity.setTime(index, time);
                    }
                });
            }
        });

        activity.setLastContentEditTextFocusChangeListener(new SignViewPagerAdapter.ContentTextFocusChangedListener() {
            @Override
            public void onFocusChange(int page, boolean hasFocus) {
                if(hasFocus == true)
                    contentEditTextFocused(page);
            }
        });

        activity.setAutoJudgementButtonOnClickListener(new SignViewPagerAdapter.AutoJudgementButtonOnClickListener() {
            @Override
            public void onClick(int page, SignInputData inputData) {
                doAutoJudgement(page, inputData);
            }
        });

        // init

        activity.setShopInfoViewEnabled(false); // 상점 정보 수정 불가

        initSpinnerData();

        updateShopInfoUI();

        if(isOneSignMode == false)
            updateSignInfoUI();

        if(isOneSignMode == true && signs != null)
            updateSelectedSign();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MJConstants.REQUEST_TAKE_AND_SAVE) {
            if(resultCode == Activity.RESULT_OK) {
                String path = data.getStringExtra(MJConstants.PATH);

                if(isDemolishPicture)
                    activity.setDemolishImage(imageRequestPage, path);
                else
                    activity.setSignImage(imageRequestPage, path);

                if(measureSign) {
                    float horizontalAngle = data.getFloatExtra(MJConstants.HORIZONTAL_ANGLE, -1);
                    float verticalAngle = data.getFloatExtra(MJConstants.VERTICAL_ANGLE, -1);
                    float zoom = data.getFloatExtra(MJConstants.CAMERA_ZOOM, -1);

                    moveToMeasureActivity(path, horizontalAngle, verticalAngle, zoom);
                } else {
                }

            }
        }

        if(requestCode == SignMeasureActivityHandler.REQUEST_MEASURE) {
            if(resultCode == Activity.RESULT_OK) {
                float width = data.getFloatExtra(MJConstants.SIZE_X, 0);
                float length = data.getFloatExtra(MJConstants.SIZE_Y, 0);

                String widthText = String.format("%.2f", (width/1000));
                String lengthText = String.format("%.2f", (length/1000));
                activity.setSignWidthText(imageRequestPage, widthText);
                activity.setSignLengthText(imageRequestPage, lengthText);
            } else {
                Toast.makeText(activity, R.string.sign_measure_canceled, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 변경 사항 있는지 체크
        boolean isChanged = false;
        for(int i=0; i<signs.size(); i++) {
            SignInputData data = activity.getCurrentInputData(i);

            boolean checkValid = true;
            try {
                checkInputDataValue(data);
            } catch (WrongNumberFormatException e) {
                checkValid = false;
            } catch (WrongNumberScopeException e) {
                checkValid = false;
            }
            if(checkValid == false) {
                isChanged = true;
                break;
            }

            boolean check = checkChanges(i, data, data.signImagePath, data.demolishImagePath);
            Log.d("junseo", i+" 번째 페이지 바뀐거 => "+check);
            if(check == true) {
                isChanged = true;
                break;
            }
        }

        // 마지막 페이지 입력 된거 있는지 체크
        if(isChanged == false) {
            SignInputData data = activity.getCurrentInputData(signs.size());
            isChanged = checkChangeLast(data);
        }

        if(isChanged == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.there_are_changes)
                    .setMessage(R.string.do_you_want_to_quit_even_if_there_are_changes)
                    .setCancelable(false)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
                        // 확인 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton){
                            dialog.cancel();
                            moveToPreviousActivity();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton){
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
        } else {
            moveToPreviousActivity();
        }
    }

    private void updateShopInfoUI() {
        if(targetShop == null)
            return;

        activity.setShopNameText(targetShop.getName());
        activity.setPhoneNumberText(targetShop.getPhoneNumber());
        activity.setRepresentativeText(targetShop.getRepresentative());
        activity.setShopCategorySpinnerSelection(targetShop.getCategory());

        // TODO 나중에 스피너 처럼 id 가지고 값을 셋팅 할 수 있도록 수정(RadioGroup을 재정의 해서 쓰면 될듯)
        int checkIndex = (targetShop.getBusinessCondition().equals("4")) ? 0 : 1;   // TODO 상수 빼놓기
        activity.setShopStatusChecked(checkIndex);
    }

    private void moveToPreviousActivity() {
        Intent responseIntent = new Intent();
        ArrayList<Sign> modifiedSigns = new ArrayList<>(signs);
        responseIntent.putExtra(MJConstants.SHOP, targetShop);
        responseIntent.putExtra(MJConstants.SIGN_LIST, modifiedSigns);
        activity.setResult(responseValue, responseIntent);

        activity.finish();
    }

    private void moveToMeasureActivity(String path, float horizontalAngle, float verticalAngle, float zoom) {
        if(path == null || horizontalAngle == -1 || verticalAngle == -1 || zoom == -1) {
            Toast.makeText(activity, R.string.cannot_measure_because_wrong_picture_info, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(activity.getApplicationContext(), SignMeasureActivity.class);
        intent.putExtra(HANDLER_CLASS, SignMeasureActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);
        intent.putExtra(MJConstants.HORIZONTAL_ANGLE, horizontalAngle);
        intent.putExtra(MJConstants.VERTICAL_ANGLE, verticalAngle);
        intent.putExtra(MJConstants.CAMERA_ZOOM, zoom);

        activity.startActivityForResult(intent, SignMeasureActivityHandler.REQUEST_MEASURE);
    }

    private void contentEditTextFocused(int page) {
        if(page >= signs.size())
            activity.setContentText(page, targetShop.getName());
    }

    private void signImageClicked(int page) {   // TODO 이미지를 바꾸고 저장 안하고 화면을 이동 할 때를 위한 시나리오가 정해져애함.
//        String path = currentSignPics.get(page).path;
        String path = activity.currentSignImagePath(page);
        moveToPictureActivity(path);
    }

    private void signImageLongClicked(final int page) {
        imageRequestPage = page;
        activity.showSignImageOptionDialog();
        activity.setSignImageOptionDialogListener(new SignImageOptionDialog.SignImageOptionDialogListener() {
            @Override
            public void onPictureChangeButtonClicked() {
                activity.hideSignImageOptionDialog();
                measureSign = false;
                isDemolishPicture = false;
                goToCamera();
            }
            @Override
            public void onChangeAndMeasureButtonClicked() {
                activity.hideSignImageOptionDialog();
                measureSign = true;
                isDemolishPicture = false;
                goToCamera();
            }
            @Override
            public void onMeasureButtonClicked() {  // remove 없는 기능
            }
            @Override
            public void onShowPictureButtonClicked() {
                activity.hideSignImageOptionDialog();
                String path = activity.currentSignImagePath(page);
                moveToPictureActivity(path);
            }
        });
    }

    private void demolitionImageClicked(int page) {
        String path = activity.currentDemolishImagePath(page);
        moveToPictureActivity(path);
    }

    private void demolitionImageLongClicked(final int page) {
        imageRequestPage = page;
        activity.showDemolitionImageOptionDialog();
        activity.setDemolitionImageOptionDailogListener(new DemolitionImageOptionDialog.DemolitionImageOptionDialogListener() {
            @Override
            public void onPictureChangeButtonClicked() {
                activity.hideDemolitionImageOptionDialog();
                imageRequestPage = page;
                measureSign = false;
                isDemolishPicture = true;
                goToCamera();
            }
            @Override
            public void onShowPictureButtonClicked() {
                activity.hideDemolitionImageOptionDialog();
                String path = activity.currentDemolishImagePath(page);
                moveToPictureActivity(path);
            }
        });
    }

//    private void moveToCameraActivity(int requestCode) {
////        Intent intent = new Intent(activity.getApplicationContext(), CameraActivity.class);
////        intent.putExtra(HANDLER_CLASS, SignCameraActivityHandler.class);
////        activity.startActivityForResult(intent, requestCode);
//    }

    private void moveToPictureActivity(String path) {
        Intent intent = new Intent(activity.getApplicationContext(), PictureActivity.class);
        intent.putExtra(HANDLER_CLASS, SignPictureActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);
        activity.startActivity(intent);     // TODO 나중에 result 받아서 사진 보기 화면에서 사진 변경 할 경우
    }

//    private void inputDataChanged(int page, SignInputData inputData) {
//        //
//        Log.d("junseo", "how many time??");
//        try {
//            checkCriticalItems(inputData);
//
//        } catch (WrongDataInputtedException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        if(targetBuilding == null)
//            return;
//
//        int areaType = targetBuilding.getAreaType();
//        int signType = inputData.typeCode;
//        int signCount = (page >= signs.size()) ? signs.size()+1 : signs.size();     // 간판 갯수
//        int placedFloor = Integer.parseInt(inputData.placedFloor);
//        float length = Float.parseFloat(inputData.length);
//        float width = Float.parseFloat(inputData.width);
//        boolean isIntersection = inputData.isIntersection;
//        boolean isFrontBackRoad = inputData.isFrontBackRoad;
//        int lightType = inputData.lightCode;
//
//
//
//        // TODO 자동 판단
//        int inpectionResult = Utilities.autoInspection(areaType, signType, signCount, placedFloor, width, length,
//                isIntersection, isFrontBackRoad, lightType);
//
//        activity.setResultSpinnerSelection(page, inpectionResult);
//    }

    private int imageRequestPage = 0;
    private boolean measureSign = false;    // 사진 찍고 나서 측정 까지 할 경우
    private boolean isDemolishPicture = false;
    private void addSignImageButtonClicked(int page) {
        imageRequestPage = page;
        measureSign = true;
        isDemolishPicture = false;

        goToCamera();
    }

    private void addDemolitionImageButtonClicked(int index) {
        imageRequestPage = index;
        measureSign = false;
        isDemolishPicture = true;

        goToCamera();
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

//    private void shopConfirmButtonClicked() {   // 업소 정보의 확인 버튼 눌렀을 때
//        int status = activity.getCheckedShopStatus();   // TODO 나중에 코드를 DB 에서 가져 와서 쓸 수 있게금 수정
//        if(status == -1)
//            return;
//
//        String shopName = activity.getInputShopName();
//        String phoneNumber = activity.getInputPhoneNumber();
//        String representative = activity.getInputRepresentative();
//        Setting category = (Setting) activity.getSelectesShopCategory();
//
//        boolean check = checkShopChanges(status, category.getCode(), shopName, phoneNumber, representative);
//        if(check == false)
//            return;
//
//        // 업소 정보 수정
//        final Shop shop = targetShop;
//        shop.setName(shopName);
//        shop.setPhoneNumber(phoneNumber);
//        shop.setRepresentative(representative);
//        shop.setBusinessCondition(status);
//        shop.setCategory(category.getCode());
//
//        ModifyShopTask task = new ModifyShopTask(activity.getApplicationContext());
//        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.saving);
//            }
//            @Override
//            public void onTaskFinished(Boolean result) {
//                activity.hideWaitingDialog();
//
//                if (result == false) {
//                    Toast.makeText(activity, R.string.error_occurred_while_saving_data, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                targetShop = shop;
//                Toast.makeText(activity, R.string.succeeded_to_save, Toast.LENGTH_SHORT).show();
//            }
//        });
//        task.execute(shop);
//    }

    private void confirmButtonClicked(int index, SignInputData inputData) {
        // 사진 체크
        String imagePath = inputData.signImagePath;
        if(imagePath == null) {
            Toast.makeText(activity, R.string.register_sign_picture, Toast.LENGTH_SHORT).show();
            return;
        }

        String demolishPicPath = inputData.demolishImagePath;

        // 필수 입력 내용 체크
        try {
            checkCriticalItems(inputData);
        } catch(WrongDataInputtedException e) {
            Toast.makeText(activity, e.getCauseString(), Toast.LENGTH_SHORT).show();
            return;
        }

        // 숫자 체크 및 값 설정
        try {
            checkInputDataValue(inputData);
        } catch (WrongNumberFormatException e) {
            String cause = activity.getString(R.string.wrong_number_type_input, e.getCauseValue());
            Toast.makeText(activity, cause, Toast.LENGTH_SHORT).show();
            return;
        } catch (WrongNumberScopeException e) {
            Toast.makeText(activity, R.string.wrong_number_scope_input, Toast.LENGTH_SHORT).show();
            return;
        }

        // 돌출형 - 높이 유효성 체크
        String signType = (String)inputData.signType;
        if(signType.equals("03")) {
            if(inputData.height.equals("")) {
                Toast.makeText(activity, R.string.projected_sign_must_have_height, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(index >= signs.size()) {
            // 새로 입력
            Toast.makeText(activity, "새로운 데이터", Toast.LENGTH_SHORT).show();
            startToSaveData(index, inputData);
        } else {
            // 기존 내용 변경
            Toast.makeText(activity, "기존 데이터", Toast.LENGTH_SHORT).show();
            startToChangeData(index, inputData);
        }
    }

//    String shopName = activity.getInputShopName();
//    String phoneNumber = activity.getInputPhoneNumber();
//    String representative = activity.getInputRepresentative();
//    Setting category = (Setting) activity.getSelectesShopCategory();

//    private boolean checkShopChanges(int status, int category, String name, String number, String representative) {
//        if(targetShop.getBusinessCondition() != status)
//            return true;
//        if(targetShop.getCategory() != category)
//            return true;
//        if(targetShop.getName().equals(name) == false)
//            return true;
//        if(targetShop.getPhoneNumber().equals(number) == false)
//            return true;
//        if(targetShop.getRepresentative().equals(representative) == false)
//            return true;
//
//        return false;
//    }

    private boolean checkChanges(final int page, SignInputData inputData, String imagePath, String demolishPicPath) {
        Sign sign = signs.get(page);
        String path = imagePath.substring(imagePath.lastIndexOf("/")+1);

        if(sign.getPicNumber().equals(path) == false)
            return true;
        if(sign.getType().equals(inputData.signType) == false)
            return true;
        if(sign.getContent().equals(inputData.content) == false)
            return true;
        if(sign.getStatsCode().equals(inputData.signStats) == false)
            return true;
        float width = Float.parseFloat(inputData.width);
        if(Utilities.floatCompare(width, sign.getWidth()) != 0)
            return true;
        float length = Float.parseFloat(inputData.length);
        if(Utilities.floatCompare(length, sign.getLength()) != 0)
            return true;
        float height = inputData.height.equals("") ? 0f : Float.parseFloat(inputData.height);
        if(Utilities.floatCompare(height, sign.getHeight()) != 0)
            return true;
        if(sign.isFront() != inputData.isFront)
            return true;
        if(sign.isFrontBackRoad() != inputData.isFrontBack)
            return true;
        if(sign.isIntersection() != inputData.isIntersection)
            return true;
        if(sign.getLightType().equals(inputData.lightType) == false)
            return true;
        if(sign.getPlacedFloor() != Integer.valueOf(inputData.placedFloor))
            return true;
        if(sign.getTotalFloor() != Integer.valueOf(inputData.totalFloor))
            return true;
        if(sign.getCollisionWidth() != Float.valueOf(inputData.collisionWidth))
            return true;
        if(sign.getCollisionLength() != Float.valueOf(inputData.collisionLength))
            return true;
        if(sign.getInspectionResult().equals(inputData.inspectionResult) == false)
            return true;
        if(sign.isCollision() != inputData.isCollision)
            return true;
        if(sign.getDemolitionPicPath() != null) {     // null 은 빈 문자열로 취급
            String dePath = "";
            if(demolishPicPath != null && demolishPicPath.isEmpty() == false)
                dePath = demolishPicPath.substring(demolishPicPath.lastIndexOf("/")+1);
            if (demolishPicPath != null && sign.getDemolitionPicPath().equals(dePath) == false) {
                return true;
            }
        } else {
            if(demolishPicPath != null && demolishPicPath.isEmpty() == false)
                return true;
        }

        if(sign.getDemolishedDate() != null) {
            String demolishDate = Utilities.dayToString(inputData.demolishDate);
            if (sign.getDemolishedDate().equals(demolishDate) == false) {
                return true;
            }
        } else {
            if ((inputData.demolishDate != null) == false) {
                return true;
            }
        }

        if(sign.getReviewCode().equals(inputData.reviewStats) == false)
            return true;
        if(sign.getInstallSide().equals(inputData.installSide) == false)
            return true;
        if(sign.getUniqueness().equals(inputData.uniqueness) == false)
            return true;
        if(sign.getMemo().equals(inputData.memo) == false)
            return true;

        return false;
    }

    private boolean checkChangeLast(SignInputData inputData) {
        if(inputData.signImagePath.equals("") == false)
            return true;
        if(inputData.demolishImagePath.equals("") == false)
            return true;
        if(inputData.content.equals("") == false)
            return true;
        if(inputData.width.equals("") == false)
            return true;
        if(inputData.length.equals("") == false)
            return true;
        if(inputData.height.equals("") == false)
            return true;
        if(inputData.isFront)
            return true;
        if(inputData.isIntersection)
            return true;
        if(inputData.isFrontBack)
            return true;
        if(inputData.placedFloor.equals("") == false)
            return true;
        if(inputData.totalFloor.equals("") == false)
            return true;
        if(inputData.collisionWidth.equals("") == false)
            return true;
        if(inputData.collisionLength.equals("") == false)
            return true;
        if(inputData.isCollision)
            return true;
        if(inputData.demolishDate != null)
            return true;
        if(inputData.memo.equals("") == false)
            return true;

        return false;
    }

    private void startToChangeData(final int page, SignInputData inputData) {
        if(checkChanges(page, inputData, inputData.signImagePath, inputData.demolishImagePath) == false) {
            Toast.makeText(activity, "바뀐 내용 없음", Toast.LENGTH_SHORT).show();    // temp
            return;
        }

        final Sign sign = signs.get(page);
        final Sign target = createSignFromSignAndInputData(sign, inputData);

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
                    activity.showAlertDialog(R.string.error_occurred_while_saving_data);
                    return;
                }
                responseValue = Activity.RESULT_OK;

                // TODO 조사번호 넣어 줬으면 seq++ 시키기

                Toast.makeText(activity, R.string.succeeded_to_save, Toast.LENGTH_SHORT).show();
                signs.set(page, target);

                SignInputData data = signToInputData(target);
                activity.setInputData(page, data);
            }
        });
        task.execute(target);
    }

    private void startToSaveData(final int page, SignInputData inputData) {
        // 간판 데이터
        final Sign newSign = createNewSignFromInputData(inputData);

        RegisterSignTask task = new RegisterSignTask(activity.getApplicationContext(), targetShop.getId());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Long>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.registering_inspection_data);
            }
            @Override
            public void onTaskFinished(Long result) {
                activity.hideWaitingDialog();
                int msg = (result != -1) ? R.string.succeeded_to_save : R.string.failed_to_save;
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();;

                if(result == -1)
                    return;

                responseValue = Activity.RESULT_OK;

                // 등록 성공
                newSign.setId(result);
                signs.add(newSign);

                SignInputData data = signToInputData(newSign);
                activity.setInputData(page, data);
                activity.addNewPageAtLast();

                MJContext.addRecentSing(newSign.getId());
                SyncConfiguration.increaseInspectionSeq();
            }
        });
        task.execute(newSign);
    }

    private void checkCriticalItems(SignInputData inputData) throws WrongDataInputtedException {
        if(inputData.content.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_sign_content);
        }
        if(inputData.width.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_sign_width);
        }
        if(inputData.length.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_sign_length);
        }
        if(inputData.placedFloor.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_placed_floor);
        }
        if(inputData.totalFloor.equals("")) {
            throw new WrongDataInputtedException(-1, R.string.input_total_floor);
        }
    }

    private void checkInputDataValue(SignInputData inputData) throws WrongNumberFormatException, WrongNumberScopeException {
        float width = 0;
        float length = 0;
        float height = 0;
        int placedFloor = 0;
        int totalFloor = 0;
        float collisionWidth = 0;
        float collisionLength = 0;

        if(inputData.width.equals("") == false)
            width = Utilities.stringToFloat(inputData.width);
        if(inputData.length.equals("") == false)
            length = Utilities.stringToFloat(inputData.length);
        if(inputData.height.equals("") == false)
            height = Utilities.stringToFloat(inputData.height);
        if(inputData.collisionWidth.equals("") == false)
            collisionWidth = Utilities.stringToFloat(inputData.collisionWidth);
        if(inputData.collisionLength.equals("") == false)
            collisionLength = Utilities.stringToFloat(inputData.collisionLength);
        if(inputData.placedFloor.equals("") == false)
            placedFloor = Utilities.stringToInt(inputData.placedFloor);
        if(inputData.totalFloor.equals("") == false)
            totalFloor = Utilities.stringToInt(inputData.totalFloor);

        // 범위 검사
        if(width < 0 || length < 0 || height < 0 || collisionWidth < 0 || collisionLength < 0 || placedFloor < 0 || totalFloor < 0)
            throw new WrongNumberScopeException(-1);

        if(width > MAX_VALUE || length > MAX_VALUE || height > MAX_VALUE || collisionWidth > MAX_VALUE ||
                collisionLength > MAX_VALUE || placedFloor > MAX_VALUE || totalFloor > MAX_VALUE)
            throw new WrongNumberScopeException(-1);
    }

//    private void startToLoadSignList() {
//        LoadSignsByShopTask task = new LoadSignsByShopTask(activity.getApplicationContext());
//        task.setDefaultAsyncTaskListener(new AsyncTaskListener<Sign, Boolean>() {
//            @Override
//            public void onTaskStart() {
//                signs = new ArrayList<>();
//                activity.showWaitingDialog(R.string.loading_sign_information);
//            }
//
//            @Override
//            public void onTaskProgressUpdate(Sign... values) {
//                if(values == null) {
//                    activity.showAlertDialog(R.string.error_occurred_while_load_sign_information);
//                    return;
//                }
//
//                for(int i=0; i<values.length; i++)
//                    signs.add(values[i]);
//            }
//
//            @Override
//            public void onTaskFinished(Boolean result) {
//                activity.hideWaitingDialog();
//
//                for (int i = 0; i < signs.size(); i++) {
//                    Sign sign = signs.get(i);
//
//                    SignInputData data = signToInputData(sign);
//                    activity.addToPager(data);
//                }
//
//                // add empty page
//                activity.addNewPageAtLast();
//
//                if(initialIndex == -1)
//                    activity.setCurrentPage(signs.size());  // set last page
//                else
//                    activity.setCurrentPage(initialIndex);
//            }
//        });
//    }

    private void updateSignInfoUI() {
        if(signs == null)
            return;

        for (int i = 0; i < signs.size(); i++) {
            Sign sign = signs.get(i);

            SignInputData data = signToInputData(sign);
            activity.addToPager(data);
        }

        // add empty page
        activity.addNewPageAtLast();

        if(initialIndex == -1)
            activity.setCurrentPage(signs.size());  // set last page
        else
            activity.setCurrentPage(initialIndex);
    }

    private void initSpinnerData() {
        SettingDataManager sdm = SettingDataManager.getInstance();
        Setting[] shopCategories = sdm.getShopCategories();

        for (int i = 0; i < shopCategories.length; i++) {
            Setting s = shopCategories[i];
            activity.addToShopCategorySpinner(s.getCode(), s);
        }

        Setting[] signTypes = MJUtilities.filterFixedSignTypes(sdm.getSignTypes());   // 고정 광고물만 보여줌
        for (int i = 0; i < signTypes.length; i++) {
            Setting s = signTypes[i];
            activity.addToTypeSpinner(new SimpleSpinner.SpinnerItem(s.getCode(), s));
        }

        Setting[] lightTypes = sdm.getLightTypes();
        for (int i = 0; i < lightTypes.length; i++) {
            Setting s = lightTypes[i];
            activity.addToLightSpinner(new SimpleSpinner.SpinnerItem(s.getCode(), s));
        }

        Setting[] results = sdm.getResults();
        for (int i = 0; i < results.length; i++) {
            Setting s = results[i];
            activity.addToResultSpinner(new SimpleSpinner.SpinnerItem(s.getCode(), s));
        }

        Setting[] status = sdm.getSignStatus();
        for (int i = 0; i < status.length; i++) {
            Setting s = status[i];
            activity.addToStatsSpinner(new SimpleSpinner.SpinnerItem(s.getCode(), s));
        }

        Setting[] reviews = sdm.getReviewCodes();
        for (int i = 0; i < reviews.length; i++) {
            Setting s = reviews[i];
            activity.addToReviewSpinner(new SimpleSpinner.SpinnerItem(s.getCode(), s));
        }

        Setting[] installSides = sdm.getInstallSides();
        for(int i=0; i<installSides.length; i++) {
            Setting s = installSides[i];
            activity.addToInstallSideSpinner(new SimpleSpinner.SpinnerItem(s.getCode(), s));
        }

        Setting[] uniqueness = sdm.getUniqueness();
        for(int i=0; i<uniqueness.length; i++) {
            Setting s = uniqueness[i];
            activity.addToUniquenessSpinner(new SimpleSpinner.SpinnerItem(s.getCode(), s));
        }

    }

    private void updateSelectedSign() {
        if(signs == null || signs.size() <= 0)
            return;

        Sign sign = signs.get(0);

        signs = new ArrayList<>();
        signs.add(sign);
        SignInputData data = signToInputData(sign);
        activity.addToPager(data);
    }

    private void doAutoJudgement(int page, SignInputData inputData) {
        String error = null;
        try {
            checkCriticalItems(inputData);  // 필수 입력 내용 체크
            checkInputDataValue(inputData); // 숫자 체크 및 값 설정
        } catch(WrongDataInputtedException e) {
            error = activity.getString(e.getCauseString());
        } catch (WrongNumberFormatException e) {
             error = activity.getString(R.string.wrong_number_type_input, e.getCauseValue());
        } catch (WrongNumberScopeException e) {
            error = activity.getString(R.string.wrong_number_scope_input);
        }

        if(error != null) {
            Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
            return;
        }

        makeAutoJudgementValues(inputData);

        String signType = (String)inputData.signType;
        String judgement = Utilities.autoJudgement(signType, autoJudgementValue);
        if(judgement.equals("-1"))
            return;

        activity.setResultSpinnerSelection(page, judgement);
    }

    private void makeAutoJudgementValues(SignInputData inputData) {
        InputType[] types = InputType.values();
        for(int i=0; i<types.length; i++) {
            InputType type = types[i];
            int value = -1;
            switch(type) {
                case SCOPE_INSTALL_FLOOR_COUNT:
                    value = Integer.valueOf(inputData.placedFloor);
                    break;

                case SCOPE_WIDTH:
                    value = Float.valueOf(inputData.width).intValue();
                    break;

                case SCOPE_AREA:
                    float width = Float.valueOf(inputData.width);
                    float length = Float.valueOf(inputData.length);
                    value = (int)(width*length);
                    break;

                case SCOPE_LENGTH:
                    value = Float.valueOf(inputData.length).intValue();
                    break;

                case SCOPE_INSTALL_HEIGHT:
                    value = (inputData.height.equals("")) ? -1 : Float.valueOf(inputData.height).intValue();
                    break;

                case SCOPE_INSTALL_HEIGHT_TOP:
                    if(inputData.height.equals(""))
                        value = -1;
                    else {
                        float h = Float.valueOf(inputData.height);
                        float l = Float.valueOf(inputData.length);
                        value = (int)(h +l);
                    }
                    break;

                case EQUATION_LIGHT:
                    String light = (String) inputData.lightType;
                    value = Integer.valueOf(light);
                    break;

                case EQUATION_INSTALL_SIDE:
                    String installSide = (String)inputData.installSide;
                    value = Integer.valueOf(installSide);
                    break;

                case EQUATION_INTERSECTION:
                    value = inputData.isIntersection ? 1 : 0;
                    break;

                case SCOPE_HORIZONTAL_SIGN_COUNT:
                    value = getSignCount("01");  // TODO 가로형 간판 - 나중에 상수는 따로
                    break;

                case SCOPE_ROOFTOP_SIGN_COUNT:
                    value = getSignCount("05");    // TODO 옥상 간판 - 나중에 상수는 따로
                    break;

                case SCOPE_PILLAR_SIGN_COUNT:
                    value = getSignCount("06");    // TODO 지주 이용 간판 - 나중에 상수는 따로
                    break;

                case SCOPE_PROJECTED_SIGN_COUNT:
                    value = getSignCount("03");    // TODO 돌출 간판 - 나중에 상수는 따로
                    break;

                default:
                    value = -1;
            }
            autoJudgementValue.putValue(type, value);
        }
    }

    private int getSignCount(String type) {
        int count = 0;
        for(int i=0; i<signs.size(); i++) {
            Sign s = signs.get(i);
            if(s.getType().equals(type))
                count++;
        }
        return count;
    }

    private SignInputData signToInputData(Sign sign) {
        String signImagePath = sign.getPicNumber().equals("") ? "" : SyncConfiguration.getDirectoryForSingPicture(sign.isSignPicModified())+sign.getPicNumber();
        String content = sign.getContent();
        Object signType = sign.getType();
        Object signStats = sign.getStatsCode();
        String width = String.format("%.2f", sign.getWidth());
        String length = String.format("%.2f", sign.getLength());
        Object lightType = sign.getLightType();
        String placedFloor = String.valueOf(sign.getPlacedFloor());
        String totalFloor = String.valueOf(sign.getTotalFloor());
        String height = String.format("%.2f", sign.getHeight());
        boolean isFront = sign.isFront();
        boolean isIntersection = sign.isIntersection();
        boolean isFrontBack = sign.isFrontBackRoad();
        boolean isCollision = sign.isCollision();
        String collisionWidth = String.format("%.2f", sign.getCollisionWidth());
        String collisionLength = String.format("%.2f", sign.getCollisionLength());;
        Object reviewStats = sign.getReviewCode();
        Object installSide = sign.getInstallSide();
        Object uniqueness = sign.getUniqueness();
        String memo = sign.getMemo();
        String demolishImagePath = "";
        if(sign.getDemolitionPicPath() != null && sign.getDemolitionPicPath().isEmpty() == false)
            demolishImagePath = SyncConfiguration.getDirectoryForSingPicture(sign.isDemolishPicModified())+sign.getDemolitionPicPath();
//        String demolishDate =
        Date demolishDate = Utilities.stringToDay(sign.getDemolishedDate());

        Object inspectionResult = sign.getInspectionResult();

        SignInputData inputData = new SignInputData(signImagePath, content, signType, signStats, width,
                length, lightType, placedFloor, totalFloor, height, isFront, isIntersection, isFrontBack,
                isCollision, collisionWidth, collisionLength, reviewStats, installSide, uniqueness, memo,
                demolishImagePath, demolishDate, inspectionResult);

        return inputData;
    }


    private Sign createNewSignFromInputData(SignInputData inputData) {
        long id = -1;		// id
        String inspectionNumber = "";
        String inspectionDate = "";
        String mobileId = String.valueOf(MJContext.getCurrentUser().getMobileId());
        boolean isSynchronized = false;
        String syncDate = "";
        String type = (String)inputData.signType;
        float width = Float.valueOf(inputData.width);
        float length = Float.valueOf(inputData.length);
        float height = inputData.height.equals("") ? 0f : Float.valueOf(inputData.height);
        float area = width*length;
        float extraSize = 0f;
        int quantity = 0;
        String content = inputData.content;
        int placedFloor = inputData.placedFloor.equals("") ? 0 : Integer.valueOf(inputData.placedFloor);
        boolean isFront = inputData.isFront;
        String lightType = (String)inputData.lightType;
        String placement = "";
        boolean isCollision = inputData.isCollision;
        float collisionWidth = inputData.collisionWidth.equals("") ? 0 : Float.valueOf(inputData.collisionWidth);
        float collisionLength = inputData.collisionLength.equals("") ? 0 : Float.valueOf(inputData.collisionLength);
        String inspectionResult = (String)inputData.inspectionResult;
        String permissionNumber = "";
        String inputter = MJContext.getCurrentUser().getUserId();
        String inputDate = "";
        String statsCode = (String)inputData.signStats;
        String picNumber = inputData.signImagePath.substring(inputData.signImagePath.lastIndexOf("/")+1);
        String modifier = MJContext.getCurrentUser().getUserId();;
        String modifyDate = "";
        int totalFloor = inputData.totalFloor.equals("") ? 0 : Integer.valueOf(inputData.totalFloor);;
        boolean isIntersection = inputData.isIntersection;
        int tblNumber = 510;
        boolean isFrontBackRoad = inputData.isFrontBack;
        String demolitionPicPath = inputData.demolishImagePath.substring(inputData.demolishImagePath.lastIndexOf("/")+1);;
        String demolishedDate = Utilities.dayToString(inputData.demolishDate);
        String reviewCode = (String)inputData.reviewStats;
        long shopId = -1;
        int addressId = -1;
        String sgCode = "";
        String placedSide = (String)inputData.installSide;
        String uniqueness = (String)inputData.uniqueness;
        String memo = inputData.memo;
        boolean signPicModified = true;
        boolean demolishPicModified = true;

        Sign sign = new Sign(id, inspectionNumber,inspectionDate,mobileId,isSynchronized,syncDate,
                type,width,length,height,area,extraSize,quantity,content,placedFloor,isFront,lightType,
                placement,isCollision,collisionWidth,collisionLength,inspectionResult,permissionNumber,
                inputter,inputDate, statsCode,picNumber,modifier,modifyDate,
                totalFloor,isIntersection,tblNumber,isFrontBackRoad,demolitionPicPath,
                demolishedDate,reviewCode,shopId,addressId,sgCode,placedSide,uniqueness,memo, signPicModified, demolishPicModified);

        // 나머지 정보 채우기
        Calendar current = Calendar.getInstance();
        SimpleDateFormat josaDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        SimpleDateFormat syncTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
        String currentTimeString = Utilities.getCurrentTimeAsString();

        String josaDate = josaDateFormat.format(current.getTime());
        syncDate = syncTimeFormat.format(SyncConfiguration.getLastSynchronizeDate());
        inputDate = Utilities.getCurrentTimeAsString();
        long newInspectionNo = SyncConfiguration.generateInspectionNo();

        sign.setInspectionDate(josaDate);
        sign.setSyncDate(syncDate);
        sign.setModifyDate(currentTimeString);
        sign.setShopId(targetShop.getId());
        sign.setSgCode(targetShop.getSgCode());
        sign.setAddressId(targetShop.getAddressId());
        sign.setInputDate(inputDate);
        sign.setInputter(MJContext.getCurrentUser().getUserId());
        sign.setModifier(MJContext.getCurrentUser().getUserId());
        sign.setInspectionNumber(String.valueOf(newInspectionNo));

        return sign;
    }

    private Sign createSignFromSignAndInputData(Sign original, SignInputData inputData) {
        Sign tempSign = new Sign(original);  // 수정에 실패 했을 대를 대비하기 위한 복사본

        // 입력받아온 정보를 넣어준다
        String signImagePath = inputData.signImagePath.substring(inputData.signImagePath.lastIndexOf("/")+1);
        String demolishImagePath = inputData.demolishImagePath.equals("") ? "" : inputData.demolishImagePath.substring(inputData.demolishImagePath.lastIndexOf("/")+1);
        boolean signPicModified = original.getPicNumber().equals(signImagePath) ? original.isSignPicModified() : true;
        boolean demolishPicModified = original.getDemolitionPicPath().equals(demolishImagePath) ? original.isDemolishPicModified() : true;
        String type =(String)inputData.signType;
        String content = inputData.content;
        String stats = (String)inputData.signStats;
        float width = Float.parseFloat(inputData.width);
        float length = Float.parseFloat(inputData.length);
        String light = (String) inputData.lightType;
        int placedFloor = Integer.parseInt(inputData.placedFloor);
        int totalFloor = Integer.parseInt(inputData.totalFloor);
        float height = inputData.height.equals("") ? 0f : Float.parseFloat(inputData.height);
        boolean isFront = inputData.isFront;
        boolean isIntersection = inputData.isIntersection;
        boolean isFrontBack = inputData.isFrontBack;
        boolean isCollision = inputData.isCollision;
        float collisionWidth = inputData.collisionWidth.equals("") ? 0f : Float.parseFloat(inputData.collisionWidth);
        float collisionLength = inputData.collisionLength.equals("") ? 0f : Float.parseFloat(inputData.collisionLength);
        String reviewCode = (String) inputData.reviewStats;
        String installSide = (String)inputData.installSide;
        String uniqueness = (String)inputData.uniqueness;
        String memo = inputData.memo;
        String demolishDate = Utilities.dayToString(inputData.demolishDate);
        String inspectionResult = (String) inputData.inspectionResult;

        tempSign.setPicNumber(signImagePath);
        tempSign.setType(type);
        tempSign.setContent(content);
        tempSign.setStatsCode(stats);
        tempSign.setWidth(width);
        tempSign.setLength(length);
        tempSign.setLightType(light);
        tempSign.setPlacedFloor(placedFloor);
        tempSign.setTotalFloor(totalFloor);
        tempSign.setHeight(height);
        tempSign.setFront(isFront);
        tempSign.setIntersection(isIntersection);
        tempSign.setFrontBackRoad(isFrontBack);
        tempSign.setCollision(isCollision);
        tempSign.setCollisionWidth(collisionWidth);
        tempSign.setCollisionLength(collisionLength);
        tempSign.setReviewCode(reviewCode);
        tempSign.setInstallSide(installSide);
        tempSign.setUniqueness(uniqueness);
        tempSign.setMemo(memo);
        tempSign.setDemolitionPicPath(demolishImagePath);
        tempSign.setDemolishedDate(demolishDate);
        tempSign.setInspectionResult(inspectionResult);
        tempSign.setSignPicModified(signPicModified);
        tempSign.setDemolishPicModified(demolishPicModified);

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

        if(tempSign.getInspectionNumber().equals("")) { // 조사 번호가 없으면 넣어준다  // TODO 조사번호 넣어 줬을 때 => 수정 완료후 seq++ 시키기 추가
            long inspectionNo = SyncConfiguration.generateInspectionNo();
            tempSign.setInspectionNumber(String.valueOf(inspectionNo));
        }

        return tempSign;
    }
}
