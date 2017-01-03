package com.mjict.signboardsurvey.handler;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.UserDataSearchActivity;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.Term;
import com.mjict.signboardsurvey.model.User;
import com.mjict.signboardsurvey.model.ui.ReviewSign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.FilterOutSignTask;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.LoadTownListTask;
import com.mjict.signboardsurvey.task.SearchUserSignDataTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Junseo on 2016-12-30.
 */
public class UserDataSearchActivityHandler extends SABaseActivityHandler {
    private static final int ALL = 999;

    private UserDataSearchActivity activity;
    private Term term = Term.ALL;
    private List<Sign> allSigns;
    private List<Sign> filteredSigns;
    private ArrayList<Bitmap> loadedImages;

    private LoadImageTask signImageTask;
//    private

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (UserDataSearchActivity)getActivity();

        // init
        String allCounty = activity.getString(R.string.all_county);
        String county = SyncConfiguration.getCountyForSync();
        activity.addToCountySpinner(0, county);
        activity.addToCountySpinner(ALL, allCounty);

        filteredSigns = new ArrayList<>();
        loadedImages = new ArrayList<>();

        User user = MJContext.getCurrentUser();
        String userText = activity.getString(R.string.user_info, user.getUserId(), user.getName());
        activity.setUserInfoText(userText);

        // register listener
        activity.setCountySpinnerOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, int id, Object data) {
                if(position == -1)
                    return;

                countySpinnerItemChanged(id, (String)data);
            }
        });

        activity.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });

        activity.setListScrollChangedListener(new UserDataSearchActivity.ListScrollChangedListener() {
            @Override
            public void onScrollStart() {
                Log.d("junseo", "onScrollStart");
                releaseAllImages();
            }
            @Override
            public void onScrollFinished(int firstVisibleItem, int lastVisibleItem) {
                Log.d("junseo", "onScrollFinished");
                startToLoadImages(firstVisibleItem, lastVisibleItem);
            }
        });

        activity.setTermRadioOnCheckedChangedListener(new UserDataSearchActivity.TermRadioOnCheckedChangeListener() {
            @Override
            public void onTodayRadioChecked() {
                term = Term.TODAY;
                applyFilter();
            }
            @Override
            public void onThreeDaysRadioChecked() {
                term = Term.THREE_DAYS;
                applyFilter();
            }
            @Override
            public void onWeekRadioChecked() {
                term = Term.A_WEEK;
                applyFilter();
            }
            @Override
            public void onMonthRadioChecked() {
                term = Term.A_MONTH;
                applyFilter();
            }
            @Override
            public void onAllRadioChecked() {
                term = Term.ALL;
                applyFilter();
            }
        });


        // do first job

    }


    public void countySpinnerItemChanged(int id, String county) {
        if(id == ALL) {
            activity.clearTownSpinner();
            return;
        }

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
                    activity.addToTownSpinner(i, result[i]);
            }
        });
        task.execute(province, county);
    }

    private void searchButtonClicked() {
        if(allSigns == null) {
            startToSearchAllSignsAndApplayFilter();
        } else {
            applyFilter();
        }
    }
    private void startToSearchAllSignsAndApplayFilter() {
        String userId = MJContext.getCurrentUser().getUserId();

        ////////////////////////
        SearchUserSignDataTask task = new SearchUserSignDataTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Sign>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_all_user_sign_data);
            }

            @Override
            public void onTaskFinished(List<Sign> signList) {
                activity.hideWaitingDialog();
                allSigns = signList;

                if(allSigns == null) {
                    // TODO 에러 발생
                    return;
                }

                if(allSigns.size() <= 0) {
                    Toast.makeText(activity, R.string.there_are_no_user_sign_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                applyFilter();
            }
        });
        task.execute(userId);
    }

    private void applyFilter() {    // TODO I/O 는 없지만 시간이 제법 걸린다(5000개 일 때 1~2초). AsyncTask 를 사용하자
        if(allSigns == null)
            return;

        activity.clearList();
        filteredSigns.clear();

        // filter
        String province = SyncConfiguration.getProvinceForSync();
        String county = (activity.getSelectedCountyId() == ALL) ? null : (String)activity.getSelectedCounty();
        String town = (activity.getSelectedTownId() == ALL) ? null : (String)activity.getSelectedTown();
        Calendar searchTime = getTimeForSearch();
        FilterOutSignTask.SearchFilter filter = new FilterOutSignTask.SearchFilter(province, county, town, searchTime);

        FilterOutSignTask task = new FilterOutSignTask(activity.getApplicationContext(), allSigns);
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<Sign, Boolean>() {
            @Override
            public void onTaskStart() {
                // 모든 UI disabled
                activity.disabledAll();
                // 백 버튼막기
                activity.setBackButtonLocked(true);


                activity.setStatusText(R.string.applying_filter);
            }

            @Override
            public void onTaskProgressUpdate(Sign... values) {
                if(values == null)
                    return;

                for(int i=0; i<values.length; i++) {
                    Sign sign = values[i];
                    ReviewSign rs = signToReviewSign(sign);
                    filteredSigns.add(sign);

                    activity.addToList(rs);
                }
            }

            @Override
            public void onTaskFinished(Boolean result) {
                // 모든 UI enabled
                activity.enabledAll();
                activity.setBackButtonLocked(false);

                String text = activity.getString(R.string.filtered_signs_count, filteredSigns.size());
                activity.setStatusText(text);

                Log.d("junseo", "aa 1");
                activity.smoothScrollToFirst();
                Log.d("junseo", "aa 2");


            }
        });
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filter);
    }

    private void releaseAllImages() {
        if(signImageTask != null && signImageTask.getStatus() == AsyncTask.Status.RUNNING)
            signImageTask.cancel(true);

        if (loadedImages == null)
            return;

        for(int i=0; i<loadedImages.size(); i++) {
            Bitmap img = loadedImages.get(i);
            img.recycle();
        }

        loadedImages.clear();
    }

    private void startToLoadImages(int firstVisibleItem, int lastVisibleItem) {
        int n = lastVisibleItem - firstVisibleItem + 1;
        String[] signImagePaths = new String[n];
        String signPicDir = SyncConfiguration.getDirectoryForSingPicture();
        for(int i=0; i<n; i++) {
            Sign sign = filteredSigns.get(firstVisibleItem+i);
            String signImagePath = signPicDir+sign.getPicNumber();
            signImagePaths[i] = signImagePath;
        }

        signImageTask = new LoadImageTask();
        signImageTask.setSampleSize(8);
        signImageTask.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... image) {
                if(image != null) {
                    activity.setListImage(image[0].index, image[0].image);
                }
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
            }
        });
        signImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, signImagePaths);
    }

    private ReviewSign signToReviewSign(Sign sign) {
        Bitmap signImage = null;
        String status = "";
        String type = "";
        String content = "";
        String lightType = "";
        String result = "";
        String size = "";
        String location = "";
        String date = "";

        Sign s = sign;
        SettingDataManager smgr = SettingDataManager.getInstance();
        Setting statusSetting = smgr.getSignStatus(s.getStatusCode());
        status = statusSetting == null ? smgr.getDefaultSignStatus() : statusSetting.getName();
        Setting typeSetting = smgr.getSignType(s.getType());
        type = typeSetting == null ? smgr.getDefaultSignTypeName() : typeSetting.getName();
        content = s.getContent();
        Setting lightSetting = smgr.getLightType(s.getLightType());
        lightType = lightSetting == null ? smgr.getDefaultLightTypeName() : lightSetting.getName();
        Setting resultSetting = smgr.getResult(s.getInspectionResult());
        result = resultSetting == null ? smgr.getDefaultResultName() : resultSetting.getName();
        size = s.getWidth() +" X "+s.getLength();
        if(s.getHeight() != 0)
            size = size + " X " +s.getHeight();
        location = s.getPlacedFloor()+" / "+s.getTotalFloor();
        date = Utilities.getCurrentTimeAsString();

        ReviewSign reviewSign = new ReviewSign(signImage, status, type, content, lightType, result, size, location, date);
        return reviewSign;
    }

    private Calendar getTimeForSearch() {
        Calendar dayForSearch = null;
        switch (term) {
            case TODAY:
                dayForSearch = Utilities.getStartTimeBeforeDays(0);
                break;
            case THREE_DAYS:
                dayForSearch = Utilities.getStartTimeBeforeDays(-3);
                break;
            case A_WEEK:
                dayForSearch = Utilities.getStartTimeBeforeDays(-7);
                break;
            case A_MONTH:
                dayForSearch = Utilities.getStartTimeBeforeMonths(-1);
                break;
            default:
                dayForSearch = null;
                break;
        }

        return dayForSearch;
    }
}
