package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ReviewSignListAdapter;
import com.mjict.signboardsurvey.model.ui.ReviewSign;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

/**
 * Created by Junseo on 2016-12-30.
 */
public class UserDataSearchActivity extends SABaseActivity {

    private TextView userInfoTextView;
    private CheckBox reviewCheckBox;
    private CheckBox demolitionCheckBox;
    private RadioGroup termRadioGroup;
    private TermRadioOnCheckedChangeListener radioCheckedChangeListener;
    private SimpleSpinner countySpinner;
    private SimpleSpinner townSpinner;
    private TextView statusTextView;
    private Button searchButton;
    private ListView resultListView;
    private ReviewSignListAdapter listAdapter;
    private ListScrollChangedListener listScrollListener;
    int lastScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_data_search;
    }

    @Override
    protected void init() {
        super.init();

        userInfoTextView = (TextView)this.findViewById(R.id.user_information_text_view);
        reviewCheckBox = (CheckBox)this.findViewById(R.id.review_check_box);
        demolitionCheckBox = (CheckBox)this.findViewById(R.id.demolish_check_box);
        termRadioGroup = (RadioGroup)this.findViewById(R.id.term_radio_group);
        resultListView = (ListView)this.findViewById(R.id.result_list_view);
        countySpinner = (SimpleSpinner)this.findViewById(R.id.county_spinner);
        townSpinner = (SimpleSpinner)this.findViewById(R.id.town_spinner);
        statusTextView = (TextView)this.findViewById(R.id.status_text_view);
        searchButton = (Button)this.findViewById(R.id.search_button);

        listAdapter = new ReviewSignListAdapter(this);
        resultListView.setAdapter(listAdapter);

        termRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioCheckedChangeListener == null)
                    return;
                switch (checkedId) {
                    case R.id.today_radio_button:
                        radioCheckedChangeListener.onTodayRadioChecked();
                        break;
                    case R.id.three_days_radio_button:
                        radioCheckedChangeListener.onThreeDaysRadioChecked();
                        break;
                    case R.id.week_radio_button:
                        radioCheckedChangeListener.onWeekRadioChecked();
                        break;
                    case R.id.month_radio_button:
                        radioCheckedChangeListener.onMonthRadioChecked();
                        break;
                    case R.id.all_radio_button:
                        radioCheckedChangeListener.onAllRadioChecked();
                        break;
                }
            }
        });

        resultListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE) {
                    if(listScrollListener != null) {
                        int last = view.getLastVisiblePosition();
                        int first = view.getFirstVisiblePosition();

                        listScrollListener.onScrollFinished(first, last);
                    }
                } else {
                    if(lastScrollState == SCROLL_STATE_IDLE && listScrollListener != null)
                        listScrollListener.onScrollStart();
                }

                lastScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(backButtonLocked)
            return;

        super.onBackPressed();
    }

    private boolean backButtonLocked = false;
    public void setBackButtonLocked(boolean locked) {
        backButtonLocked = locked;
    }

    public void enabledAll() {
        reviewCheckBox.setEnabled(true);
        demolitionCheckBox.setEnabled(true);
        for(int i=0; i<termRadioGroup.getChildCount(); i++) {
            View child = termRadioGroup.getChildAt(i);
            if(child != null && child instanceof RadioButton)
                child.setEnabled(true);
        }

        countySpinner.setEnabled(true);
        townSpinner.setEnabled(true);
        statusTextView.setEnabled(true);
        searchButton.setEnabled(true);
    }


    public void disabledAll() {
        reviewCheckBox.setEnabled(false);
        demolitionCheckBox.setEnabled(false);
        for(int i=0; i<termRadioGroup.getChildCount(); i++) {
            View child = termRadioGroup.getChildAt(i);
            if(child != null && child instanceof RadioButton)
                child.setEnabled(false);
        }

        countySpinner.setEnabled(false);
        townSpinner.setEnabled(false);
        statusTextView.setEnabled(false);
        searchButton.setEnabled(false);
    }

    public void setUserInfoText(String text) {
        userInfoTextView.setText(text);
    }

    public void setReviewCheckBoxOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        reviewCheckBox.setOnCheckedChangeListener(listener);
    }

    public void demolitionCheckBoxOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        demolitionCheckBox.setOnCheckedChangeListener(listener);
    }

    public void setTermRadioOnCheckedChangedListener(TermRadioOnCheckedChangeListener listener) {
        radioCheckedChangeListener = listener;
    }

    public void addToList(ReviewSign data) {
        listAdapter.add(data);
    }

    public void clearList() {
        listAdapter.clear();
    }

    public int getListCount() {
        return listAdapter.getCount();
    }

    public void setListScrollChangedListener(ListScrollChangedListener listener) {
        listScrollListener = listener;
    }

    public void smoothScrollToFirst() {
//        resultListView.smoothScrollToPosition(1);
//        resultListView.smoothScrollToPosition(0);
//        resultListView.smoothScrollByOffset(0);
        resultListView.fling(1);
        resultListView.fling(-1);
    }

    public void setListImage(int position, Bitmap image) {
        listAdapter.setSignImage(position, image);
    }

    public void addToCountySpinner(int id, Object data) {
        countySpinner.addSpinnerData(id, data);
    }

    public Object getSelectedCounty() {
        return countySpinner.getSelectedData();
    }

    public int getSelectedCountyId() {
        return countySpinner.getSelectedDataId();
    }

    public void setCountySpinnerOnItemSelectionChangedListener(SimpleSpinner.OnItemSelectionChangedListener listener) {
        countySpinner.setOnItemSelectionChangedListener(listener);
    }

    public void addToTownSpinner(int id, Object data) {
        townSpinner.addSpinnerData(id, data);
    }

    public Object getSelectedTown() {
        return townSpinner.getSelectedData();
    }

    public int getSelectedTownId() {
        return townSpinner.getSelectedDataId();
    }

    public void setTownSpinnerOnItemSelectionChangedListener(SimpleSpinner.OnItemSelectionChangedListener listener) {
        townSpinner.setOnItemSelectionChangedListener(listener);
    }

    public void clearTownSpinner() {
        townSpinner.clearSpinner();
    }

    public void setStatusText(String text) {
        statusTextView.setText(text);
    }

    public void setStatusText(int resId) {
        statusTextView.setText(resId);
    }

    public void setSearchButtonOnClickListener(View.OnClickListener listener) {
        searchButton.setOnClickListener(listener);
    }

    public static interface TermRadioOnCheckedChangeListener {
        public void onTodayRadioChecked();
        public void onThreeDaysRadioChecked();
        public void onWeekRadioChecked();
        public void onMonthRadioChecked();
        public void onAllRadioChecked();
    }

    public static interface ListScrollChangedListener {
        public void onScrollStart();
        public void onScrollFinished(int firstVisibleItem, int lastVisibleItem);
    }
}
