package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.ReviewSignActivity;
import com.mjict.signboardsurvey.activity.SignInformationActivity;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.ui.ReviewSign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadConsonantTask;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.LoadStreetAddressTask;
import com.mjict.signboardsurvey.task.LoadTownListTask;
import com.mjict.signboardsurvey.task.SearchReviewSignTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

import java.util.List;

/**
 * Created by Junseo on 2016-07-24.
 */
public class ReviewSignActivityHandler extends SABaseActivityHandler {

    private ReviewSignActivity activity;
    private List<Sign> searchResult;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (ReviewSignActivity)getActivity();

        activity.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });

        activity.setListRowClickListener(new ReviewSignActivity.ListRowClickListener() {
            @Override
            public void onClick(int position, ReviewSign data) {
                listRowClicked(position, data);
            }
        });

        // init the spinner.
        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();
        activity.addCountyToSpinner(county);

        activity.setCountySpinnerOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object id, Object data) {
                if(position == -1)
                    return;

                countySpinnerItemChanged(position, (String)data);
            }
        });

        activity.setTownSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object Object, Object data) {
                if(position == -1)
                    return;

                townSpinnerItemChanged(position, (String)data);
            }
        });

        activity.setConsonantSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object id, Object data) {
                if(position == -1)
                    return;

                consonantSpinnerItemChanged(position, (String)data);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MJConstants.REQUEST_SIGN_MODIFY) {
            if(resultCode == Activity.RESULT_OK) { // 간판 정보가 바뀌었음
                Sign sign = (Sign)data.getSerializableExtra(MJConstants.SIGN);

                findSignAndReplace(sign);
            } else {    // 간판 정보가 바뀌지 않았음
            }
        } else {

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void countySpinnerItemChanged(int position, String county) {
        String province = SyncConfiguration.getProvinceForSync();

        LoadTownListTask task = new LoadTownListTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<String[]>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_town_list);
                activity.clearTownSpinner();
            }
            @Override
            public void onTaskFinished(String[] result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_load_address_data);
                    return;
                }
                for(int i=0; i<result.length; i++)
                    activity.addToTownSpinner(result[i]);
            }
        });
        task.execute(province, county);
    }

    private void searchButtonClicked() {

        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();
        String town = (String) activity.getSelectedTownSpinnerItem();
        String street = (String) activity.getSelectedStreetSpinnerItem();
        String firstNumber = activity.getInputFirstBuildingNumber();
        final String secondNumber = activity.getInputSecondBuildingNumber();

        SearchReviewSignTask task = new SearchReviewSignTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Sign>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.searching);
                activity.clearList();
                searchResult = null;
            }
            @Override
            public void onTaskFinished(List<Sign> result) {
                activity.hideWaitingDialog();

                searchResult = result;
                if(searchResult == null) {
                    // TODO 에러 발생
                }
                for(int i=0; i<searchResult.size(); i++) {
                    Sign sign = searchResult.get(i);
                    ReviewSign s = signToReviewSign(sign);
                    activity.addToList(s);
                }

                startToLoadAllImages();
            }
        });

        Address address = new Address(province, county, town, street, firstNumber, secondNumber, null, null);
        task.execute(address);
    }

    private void townSpinnerItemChanged(int position, String data) {
        String town = data;
        LoadConsonantTask task = new LoadConsonantTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<String[]>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_street_address_list);
                activity.clearConsonantSpinner();
            }
            @Override
            public void onTaskFinished(String[] result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_load_address_data);
                    return;
                }
                for(int i=0; i<result.length; i++)
                    activity.addConsonantToSpinner(result[i]);
            }
        });
        task.execute(town);
    }

    private void consonantSpinnerItemChanged(int position, String data) {
        String consonant = data;
        String town = (String)activity.getSelectedTownSpinnerItem();

        LoadStreetAddressTask task = new LoadStreetAddressTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<StreetAddress>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_street_address_list);
                activity.clearStreetSpinner();
            }
            @Override
            public void onTaskFinished(List<StreetAddress> result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_load_address_data);
                    return;
                }

                for(int i=0; i<result.size(); i++)
                    activity.addStreetToSpinner(result.get(i).getStreet());
            }
        });
        task.execute(town, consonant);
    }

    private void startToLoadAllImages() {
        if(searchResult == null)
            return;

        int n = searchResult.size();
        String[] signImagePaths = new String[n];

        for(int i=0; i<n; i++) {
            Sign sign = searchResult.get(i);
            String signPicDir = SyncConfiguration.getDirectoryForSingPicture(sign.isSignPicModified());
            String signImagePath = signPicDir+sign.getPicNumber();
            signImagePaths[i] = signImagePath;
        }

        runImageLoadTask(signImagePaths);
    }

    private void startToLoadImage(int position) {
        if(searchResult == null)
            return;

        Sign sign = searchResult.get(position);
        String signPicDir = SyncConfiguration.getDirectoryForSingPicture(sign.isSignPicModified());
        String signImagePath = signPicDir+sign.getPicNumber();

        runImageLoadTask(signImagePath);
    }

    private void runImageLoadTask(String... paths) {
        LoadImageTask signImageTask = new LoadImageTask();
        signImageTask.setSampleSize(8);
        signImageTask.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... image) {
                if(image != null) {
                    activity.setSignImage(image[0].index, image[0].image);
                }
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
            }
        });
        signImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paths);
    }
    private void listRowClicked(int position, ReviewSign data) {
        Sign sign = searchResult.get(position);

        Intent intent = new Intent(activity.getBaseContext(), SignInformationActivity.class);
        intent.putExtra(HANDLER_CLASS, SignInformationActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, sign);
//        intent.putExtra(MJConstants.SIGN_LIST, shopSigns);

        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_MODIFY);
    }

    private void findSignAndReplace(Sign sign) {
        int position = -1;
        for(int i=0; i<searchResult.size(); i++) {
            Sign s = searchResult.get(i);
            if(s.getId() == sign.getId()) {
                position = i;
            }
        }
        if(position != -1) {
            searchResult.set(position, sign);
            ReviewSign reviewSign = signToReviewSign(sign);
            activity.replaceListItem(position, reviewSign);

            startToLoadImage(position);
        }
    }

    private ReviewSign signToReviewSign(Sign sign) {
        SettingDataManager sdmgr = SettingDataManager.getInstance();

        Setting reviewSetting = sdmgr.getReviewCode(sign.getReviewCode());
        Setting typeSetting = sdmgr.getSignType(sign.getType());
        Setting lightSetting = sdmgr.getLightType(sign.getLightType());
        Setting resultSetting = sdmgr.getResult(sign.getInspectionResult());

        String status = reviewSetting.getName();
        String type = typeSetting == null ? sdmgr.getDefaultSignTypeName() : typeSetting.getName();
        String content = sign.getContent();
        String lightType = lightSetting == null ? sdmgr.getDefaultLightTypeName() : lightSetting.getName();
        String result = resultSetting == null ? sdmgr.getDefaultResultName() : resultSetting.getName();
        String size = sign.getHeight() > 0 ? sign.getWidth()+"X"+sign.getLength() : sign.getWidth()+"X"+sign.getLength()+"X"+sign.getHeight();
        String location = sign.getPlacedFloor()+"/"+sign.getTotalFloor();
        String date = sign.getInputDate();

        ReviewSign reviewSign = new ReviewSign(null, status, type, content, lightType,
                result, size, location, date);

        return reviewSign;
    }
}
