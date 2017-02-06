package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BasicSignInformationInputActivity;
import com.mjict.signboardsurvey.activity.SignInformationActivity;
import com.mjict.signboardsurvey.activity.SignListActivity;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.ui.SignInfo;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.DeleteSignTask;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.LoadSignsByShopTask;
import com.mjict.signboardsurvey.task.RegisterSignTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.widget.SignOptionDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Junseo on 2016-11-15.
 */
public class SignListActivityHandler extends SABaseActivityHandler {
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

        activity.setSignListOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                signListLongClicked(position);
                return true;
            }
        });

        activity.setOptionMenuItemClickListener(new SignListActivity.OnOptionMenuItemClickListener() {
            @Override
            public void onAddNewSignClicked() {
                goToSignInput();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MJConstants.REQUEST_SIGN_INPUT_INFORMATION) {
            if(resultCode == Activity.RESULT_OK) {  // 새 간판 정보 받아옴
                Sign sign = (Sign)data.getSerializableExtra(MJConstants.SIGN);
                startToInsertSignAndUpdateUI(sign);
            } else {    // 간판 정보 입력 취소
                Toast.makeText(activity, R.string.input_sign_information_canceled, Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == MJConstants.REQUEST_SIGN_MODIFY) {
            if(resultCode == Activity.RESULT_OK) { // 간판 정보가 바뀌었음
                Sign sign = (Sign)data.getSerializableExtra(MJConstants.SIGN);
                findSignAndReplace(sign);
            } else {    // 간판 정보가 바뀌지 않았음
            }
        } else {

        }


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

                        SignInfo si = signToSignInfo(s);

                        activity.addToList(si);
                    }
                }
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();

                String[] paths = new String[shopSigns.size()];
                for(int i=0; i<shopSigns.size(); i++) {
                    Sign sign = shopSigns.get(i);
                    String path = SyncConfiguration.getDirectoryForSingPicture(sign.isModified()) + sign.getPicNumber();
                    paths[i] = path;
                }

                startToLoadSignImages(paths);
            }
        });
        task.execute(currentShop);
    }

    private void startToInsertSignAndUpdateUI(final Sign sign) {
        // 나머지 정보 채우기
        Calendar current = Calendar.getInstance();
        SimpleDateFormat josaDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        SimpleDateFormat syncTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
        String currentTimeString = Utilities.getCurrentTimeAsString();

        String josaDate = josaDateFormat.format(current.getTime());
        String syncDate = syncTimeFormat.format(SyncConfiguration.getLastSynchronizeDate());
        String inputDate = Utilities.getCurrentTimeAsString();
        long newInspectionNo = SyncConfiguration.generateInspectionNo();

        sign.setInspectionDate(josaDate);
        sign.setSyncDate(syncDate);
        sign.setModifyDate(currentTimeString);
        sign.setShopId(currentShop.getId());
        sign.setSgCode(currentShop.getSgCode());
        sign.setAddressId(currentShop.getAddressId());
        sign.setInputDate(inputDate);
        sign.setInputter(MJContext.getCurrentUser().getUserId());
        sign.setModifier(MJContext.getCurrentUser().getUserId());
        sign.setInspectionNumber(String.valueOf(newInspectionNo));

        RegisterSignTask task = new RegisterSignTask(activity.getApplicationContext(), currentShop.getId());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Long>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }

            @Override
            public void onTaskFinished(Long result) {
                activity.hideWaitingDialog();
                if(result < 0) {
                    // TODO 등록 실패 - 사진 파일 삭제
                    Toast.makeText(activity, R.string.failed_to_save_sign_information, Toast.LENGTH_SHORT);
                } else {
                    // 등록 성공
                    sign.setId(result);

                    SignInfo si = signToSignInfo(sign);
                    String path = SyncConfiguration.getDirectoryForSingPicture(sign.isModified()) + sign.getPicNumber();
                    shopSigns.add(sign);
                    activity.addToList(si);

                    startToLoadSignImage(shopSigns.size()-1, path);

                    MJContext.addRecentSing(sign.getId());
                    SyncConfiguration.increaseInspectionSeq();
                }
            }
        });
        task.execute(sign);
    }


    private void findSignAndReplace(Sign sign) {
        int position = -1;
        for(int i=0; i<shopSigns.size(); i++) {
            Sign s = shopSigns.get(i);
            if(s.getId() == sign.getId()) {
                position = i;
            }
        }
        if(position != -1) {
            SignInfo si = signToSignInfo(sign);
            String path = SyncConfiguration.getDirectoryForSingPicture(sign.isModified()) + sign.getPicNumber();
            shopSigns.set(position, sign);
            activity.setSignInfo(position, si);
            startToLoadSignImage(position, path);
        }
    }

    private SignInfo signToSignInfo(Sign s) {
        SettingDataManager smgr = SettingDataManager.getInstance();

        // update to ui
        String content = s.getContent();
        String size = s.getWidth()+" X "+s.getLength();
        if(s.getHeight() != 0)
            size = size + " X "+s.getHeight();

        Setting statusSetting = smgr.getSignStatus(s.getStatsCode());
        Setting lightSetting = smgr.getLightType(s.getLightType());
        Setting resultSetting = smgr.getResult(s.getInspectionResult());

        String status = statusSetting == null ? smgr.getDefaultShopStatusName() : statusSetting.getName();
        String light = lightSetting == null ? smgr.getDefaultLightTypeName() : lightSetting.getName();
        String result = resultSetting == null ? smgr.getDefaultResultName() : resultSetting.getName();
        String location = s.getPlacedFloor() +" / "+s.getTotalFloor();

        int color = -1;
//        0xffFFFFA5, 0xffE7FFC0, 0xffFFD2FF
        if(s.getStatsCode().equals("2")) {
            color = 0xff0099fd;
        } else if(s.getStatsCode().equals("3")) {
            color = 0xff75b3f2;
        } else {
            color = -1;
        }

        SignInfo si = new SignInfo(null, content, size, status, light, location, result, color);
        return si;
    }

    private void startToLoadSignImages(String... paths) {
        signImageLoadTask = new LoadImageTask();
        signImageLoadTask.setSampleSize(8);
        signImageLoadTask.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                for(int i=0; i<values.length; i++) {
                    Log.d("junseo", "image: "+values[i].index+" img: "+values[i].image);
                    activity.setSignImage(values[i].index, values[i].image);
                }

            }
            @Override
            public void onTaskFinished(Boolean result) {
            }
        });
        signImageLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paths);
    }

    private void startToLoadSignImage(final int position, String path) {
        signImageLoadTask = new LoadImageTask();
        signImageLoadTask.setSampleSize(8);
        signImageLoadTask.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                for(int i=0; i<values.length; i++)
                    activity.setSignImage(position, values[i].image);
            }
            @Override
            public void onTaskFinished(Boolean result) {
            }
        });
        signImageLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path);
    }

    private void startToDeleteSign(final int position) {
        Sign sign = shopSigns.get(position);
        DeleteSignTask task = new DeleteSignTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.deleteing);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
                int resId = result ? R.string.succeeded_to_delete : R.string.failed_to_delete;
                Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();

                if(result) {
                    shopSigns.remove(position);
                    activity.removeFromList(position);
                }
            }
        });
        task.execute(sign);
    }

    private void signListLongClicked(final int position) {
        Sign sign = shopSigns.get(position);
        boolean deleteEnabled = sign.isSynchronized() == true ? false : true;
        activity.setSignDeleteDialogButtonVisible(deleteEnabled);

        activity.showSignOptionDialog(new SignOptionDialog.SignOptionDialogOnClickListener() {
            @Override
            public void onShowDetailButtonClicked() {
                activity.hideSignOptionDialog();
                goToSignInformation(position);
            }
            @Override
            public void onDeleteButtonClicked() {
                activity.hideSignOptionDialog();
                startToDeleteSign(position);
            }
        });

    }

    private void goToSignInformation(int position) {
        Sign sign = shopSigns.get(position);
        MJContext.addRecentSing(sign.getId());

        Intent intent = new Intent(activity, SignInformationActivity.class);
        intent.putExtra(HANDLER_CLASS, SignInformationActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, sign);
        intent.putExtra(MJConstants.SIGN_LIST, shopSigns);

        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_MODIFY);
    }

    private void goToSignInput() {
        Intent intent = new Intent(activity, BasicSignInformationInputActivity.class);
        intent.putExtra(HANDLER_CLASS, BasicSignInformationInputActivityHandler.class);
        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_INPUT_INFORMATION);
    }
}
