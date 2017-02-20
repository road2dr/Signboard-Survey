package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.DemolishedSignActivity;
import com.mjict.signboardsurvey.activity.SignInformationActivity;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.ui.DemolishedSign;
import com.mjict.signboardsurvey.model.ui.SignStatus;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadConsonantTask;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.LoadStreetAddressTask;
import com.mjict.signboardsurvey.task.LoadTownListTask;
import com.mjict.signboardsurvey.task.SearchDemolitionSignTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

import java.util.List;

/**
 * Created by Junseo on 2016-07-25.
 */
public class DemolishedSignActivityHandler extends SABaseActivityHandler {
    private static final int REQUEST_MODIFY_DATA = 5822;

    private DemolishedSignActivity activity;
    private List<Sign> searchList;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (DemolishedSignActivity)getActivity();

        activity.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });

//        activity.setCountySpinnerOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
//            @Override
//            public void onItemSelectionChanged(int position, Object data) {
//                if(position == -1)
//                    return;
//
//                countySpinnerItemChanged(position, (String)data);
//            }
//        });

        activity.setListRowClickListener(new DemolishedSignActivity.ListRowClickListener() {
            @Override
            public void onClick(int position, DemolishedSign data) {
                listRowClicked(position, data);
            }
        });

        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();


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
            public void onItemSelectionChanged(int position, Object id, Object data) {
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

        activity.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });
//        activity.setStreetSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
//            @Override
//            public void onItemSelectionChanged(int position, Object data) {
//                streetSpinnerItemChanged(position, (String)data);
//            }
//        });

//        activity.setOnListItemClickListener(new AddressSearchActivity.BuildingListItemOnClickListener() {
//            @Override
//            public void onListItemClicked(int position, BuildingWithImage info) {
//                listItemClicked(position, info);
//            }
//        });



        activity.addCountyToSpinner(county);

//        // init the spinner
//        String province = Configuration.getProvinceForSync();
//        String county = Configuration.getCountyForSync();
//
//        activity.addCountyData(county);
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

    private void searchButtonClicked() {

        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();
        String town = (String) activity.getSelectedTownSpinnerItem();
        String street = (String) activity.getSelectedStreetSpinnerItem();
        String firstNumber = activity.getInputFirstBuildingNumber();
        String secondNumber = activity.getInputSecondBuildingNumber();

        SearchDemolitionSignTask task = new SearchDemolitionSignTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Sign>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.searching);
                activity.clearList();
            }
            @Override
            public void onTaskFinished(List<Sign> result) {
                activity.hideWaitingDialog();

                searchList = result;
                int n = result.size();

                for(int i=0; i<n; i++) {
                    Sign s = searchList.get(i);
                    DemolishedSign ds = signToDemolishedSign(s);

                    activity.addToList(ds);
                }

//                for(int i=0; i<n; i++)
//                    activity.addToList(result.get(i));
//
                startToLoadAllImages();
            }
        });

        Address address = new Address(province, county, town, street, firstNumber, secondNumber, null, null);
        task.execute(address);
    }

    private void listRowClicked(int position, DemolishedSign data) {
        Sign sign = searchList.get(position);

        Intent intent = new Intent(activity.getBaseContext(), SignInformationActivity.class);
        intent.putExtra(HANDLER_CLASS, SignInformationActivityHandler.class);
        intent.putExtra(MJConstants.SIGN, sign);
//        intent.putExtra(MJConstants.SIGN_LIST, shopSigns);

        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_MODIFY);
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

    private void findSignAndReplace(Sign sign) {
        int position = -1;
        for(int i=0; i<searchList.size(); i++) {
            Sign s = searchList.get(i);
            if(s.getId() == sign.getId()) {
                position = i;
            }
        }
        if(position != -1) {
            searchList.set(position, sign);
            DemolishedSign demolishedSign = signToDemolishedSign(sign);
            activity.replaceListItem(position, demolishedSign);

            startToLoadImage(position);
        }
    }

    private void startToLoadImage(int position) {
        if(searchList == null)
            return;

        Sign sign = searchList.get(position);
        String signPicDir = SyncConfiguration.getDirectoryForSingPicture(sign.isSignPicModified());
        String demolPicDir = SyncConfiguration.getDirectoryForSingPicture(sign.isDemolishPicModified());

        String signImagePath = signPicDir+sign.getPicNumber();
        String demolitionImagePath = null;
        if(sign.getDemolitionPicPath() != null && sign.getDemolitionPicPath().isEmpty() == false)
            demolitionImagePath = demolPicDir+sign.getDemolitionPicPath();

        runSignImageLoadTask(signImagePath);
        runDemolishImageLoadTask(demolitionImagePath);
    }

    private void startToLoadAllImages() {
        if(searchList == null)
            return;

        int n = searchList.size();
        String[] signImagePaths = new String[n];
        String[] demolitionImagePaths = new String[n];
        for(int i=0; i<n; i++) {
            Sign sign = searchList.get(i);
            String signPicDir = SyncConfiguration.getDirectoryForSingPicture(sign.isDemolishPicModified());
            String demolPicDir = SyncConfiguration.getDirectoryForSingPicture(sign.isDemolishPicModified());
            String signImagePath = signPicDir+sign.getPicNumber();

            String demolitionImagePath = null;
            if(sign.getDemolitionPicPath() != null && sign.getDemolitionPicPath().isEmpty() == false)
                 demolitionImagePath = demolPicDir+sign.getDemolitionPicPath();

            signImagePaths[i] = signImagePath;
            demolitionImagePaths[i] = demolitionImagePath;
        }

        runSignImageLoadTask(signImagePaths);
        runDemolishImageLoadTask(demolitionImagePaths);
    }

    private void runSignImageLoadTask(String... paths) {
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
            public void onTaskFinished(Boolean result) {
            }
        });
        signImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paths);
    }

    private void runDemolishImageLoadTask(String... paths) {
        LoadImageTask demolitionImageTask = new LoadImageTask();
        demolitionImageTask.setSampleSize(8);
        demolitionImageTask.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... image) {
                if(image != null) {
                    activity.setDemolitionImage(image[0].index, image[0].image);
                }
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
            }
        });
        demolitionImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paths);
    }

//    private void loadSignImageAndReplace(Sign sign) {
//
//        LoadDemolitionSignBySignTask task = new LoadDemolitionSignBySignTask(activity.getApplicationContext());
//        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<DemolitionSign>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.loading);
//            }
//            @Override
//            public void onTaskFinished(DemolitionSign demolitionSign) {
//                activity.hideWaitingDialog();
//                if(demolitionSign == null)
//                    return;
//
//                if(demolitionSign.status != 1 && demolitionSign.status !=2 )
//                    activity.removeListItem(clickedPosition);
//                else
//                    activity.replaceListItem(clickedPosition, demolitionSign);
//
//                clickedPosition = 0;
//            }
//        });
//        task.execute(sign);
//    }

    private DemolishedSign signToDemolishedSign(Sign sign) {
        SignStatus status = SignStatus.NORMAL;
        int labelColor = -1;
        String labelText = "";
        String type = null;
        String content = null;
        String shopName = null;
        String lightType = null;
        String result = null;
        String size = null;
        String location = null;
        String date = null;

        SettingDataManager sdmgr = SettingDataManager.getInstance();

        if(sign.getStatsCode().equals("1"))        // TODO 상수를 파일이나 다른 어딘가에 지정
            status = SignStatus.DEMOLISHED;
        else if(sign.getStatsCode().equals("2"))
            status = SignStatus.TO_BE_DEMOLISH;
        else
            status = SignStatus.NORMAL;

        boolean labelVisible = (status == SignStatus.DEMOLISHED || status == SignStatus.TO_BE_DEMOLISH) ? true : false;

        if(status == SignStatus.DEMOLISHED)
            labelColor = Color.GRAY;
        else if(status == SignStatus.TO_BE_DEMOLISH)
            labelColor = Color.GREEN;
        else
            labelColor = Color.BLACK;

        Setting statusSetting = sdmgr.getSignStatus(sign.getStatsCode());
        labelText = (statusSetting == null) ? sdmgr.getDefaultSignStatus() : statusSetting.getName();
        content = sign.getContent();
        Setting typeSetting = sdmgr.getSignType(sign.getType());
        type = (typeSetting == null) ? sdmgr.getDefaultSignTypeName() : typeSetting.getName();
        Setting lightTypeSetting = sdmgr.getLightType(sign.getLightType());
        lightType = (lightTypeSetting == null) ? sdmgr.getDefaultLightTypeName(): lightTypeSetting.getName();
        Setting resultSetting = sdmgr.getResult(sign.getInspectionResult());
        result = (resultSetting == null) ? sdmgr.getDefaultResultName() : resultSetting.getName();
        size = sign.getWidth()+"X"+sign.getLength();
        if(sign.getHeight() > 0)
            size = size +"X"+sign.getHeight();
        location = sign.getPlacedFloor()+"/"+sign.getTotalFloor();
        date = sign.getInputDate();

        DemolishedSign demolitionSign = new DemolishedSign(labelVisible, labelColor, labelText, status,
                content, lightType, type, date,location, result, size, null, null);

        return demolitionSign;
    }
}
