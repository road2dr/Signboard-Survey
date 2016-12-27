package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.ReviewSignActivity;
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
    private static final int REQUEST_MODIFY_DATA = 7476;

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
            public void onItemSelectionChanged(int position, Object data) {
                if(position == -1)
                    return;

                countySpinnerItemChanged(position, (String)data);
            }
        });

        activity.setTownSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object data) {
                if(position == -1)
                    return;

                townSpinnerItemChanged(position, (String)data);
            }
        });

        activity.setConsonantSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object data) {
                if(position == -1)
                    return;

                consonantSpinnerItemChanged(position, (String)data);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQUEST_MODIFY_DATA) {
//            if (resultCode == Activity.RESULT_OK) {
//                Shop shop = (Shop) data.getSerializableExtra(SignInformationActivityHandler.MODIFIED_SHOP);
//                ArrayList<Sign> signs = (ArrayList<Sign>) data.getSerializableExtra(SignInformationActivityHandler.MODIFIED_SIGNS);
//                if(signs != null && signs.size() > 0) {
//                    loadSignImageAndReplace(signs.get(0));
//                }
//            }
//        }

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
        String signPicDir = SyncConfiguration.getDirectoryForSingPicture();
        for(int i=0; i<n; i++) {
            Sign sign = searchResult.get(i);
            String signImagePath = signPicDir+sign.getPicNumber();
            signImagePaths[i] = signImagePath;
        }

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
        signImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, signImagePaths);


    }

    int clickedPosition = 0;
    private void listRowClicked(int position, ReviewSign data) {
        clickedPosition = position;

//        Intent intent = new Intent(activity.getBaseContext(), SignInformationActivity.class);
//        intent.putExtra(HANDLER_CLASS, SignInformationActivityHandler.class);
//        intent.putExtra(SignInformationActivityHandler.SHOP_INFORMATION, data.shop);
//        intent.putExtra(SignInformationActivityHandler.CURRENT_SIGN, data.sign);
//        intent.putExtra(SignInformationActivityHandler.ONE_SIGN_MODE, true);
//
//        activity.startActivityForResult(intent, REQUEST_MODIFY_DATA);
    }

    private void loadSignImageAndReplace(Sign sign) {

//        LoadReviewSignBySignTask task = new LoadReviewSignBySignTask(activity.getApplicationContext());
//        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<ReviewSign>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.loading);
//            }
//            @Override
//            public void onTaskFinished(ReviewSign reviewSign) {
//                activity.hideWaitingDialog();
//                if(reviewSign == null)
//                    return;
//
//                activity.replaceListItem(clickedPosition, reviewSign);
//
//                clickedPosition = 0;
//
//            }
//        });
//        task.execute(sign);
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
