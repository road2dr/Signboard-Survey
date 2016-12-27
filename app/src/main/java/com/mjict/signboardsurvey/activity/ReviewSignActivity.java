package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ReviewSignListAdapter;
import com.mjict.signboardsurvey.model.ui.ReviewSign;
import com.mjict.signboardsurvey.widget.SimpleSpinner;


/**
 * Created by Junseo on 2016-07-24.
 */
public class ReviewSignActivity extends SABaseActivity {

    private ListView signListView;
    private SimpleSpinner countySpinner;
    private SimpleSpinner townSpinner;
    private SimpleSpinner consonantSpinner;
    private SimpleSpinner streetSpinner;
    private EditText firstBuildingNumberEditText;
    private EditText secondBuildingNumberEditText;
    private Button searchButton;

    private ListRowClickListener listRowClickListener;

    private ReviewSignListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_review_sign;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.review_signs);

        signListView = (ListView)this.findViewById(R.id.sign_list_view);
        countySpinner = (SimpleSpinner)this.findViewById(R.id.county_spinner);
        townSpinner = (SimpleSpinner)this.findViewById(R.id.town_spinner);
        consonantSpinner = (SimpleSpinner)this.findViewById(R.id.consonant_spinner);
        streetSpinner = (SimpleSpinner)this.findViewById(R.id.street_spinner);
        firstBuildingNumberEditText = (EditText)this.findViewById(R.id.first_building_number_text_view);
        secondBuildingNumberEditText = (EditText)this.findViewById(R.id.second_building_number_text_view);
        searchButton = (Button)this.findViewById(R.id.search_button);

        adapter = new ReviewSignListAdapter(this);

        signListView.setAdapter(adapter);
        signListView.setOnTouchListener(new ListView.OnTouchListener() {    // 리스트뷰 스크롤 안되는 버그 픽스
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return false;
            }
        });
        signListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

//    @Override
//    protected int getMainContentView() {
//        return R.layout.activity_review_sign;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.option_menu_review_sign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_shop:
//                shopInfoDlg.show();
//                break;
//        }

        return true;
    }

    public void addToList(ReviewSign data) {
        adapter.add(data);
    }

    public void replaceListItem(int position, ReviewSign data) {
        ReviewSign sd = adapter.getItem(position);
        adapter.remove(sd);
        adapter.insert(data, position);
    }

    public void clearList() {
        adapter.clear();
    }

    public void setSignImage(int position, Bitmap image) {
        adapter.setSignImage(position, image);
    }

    public void addCountyToSpinner(String county) {
        countySpinner.addSpinnerData(county);
    }

    public void clearCountySpinner() {
        countySpinner.clearSpinner();;
    }

    public void setCountySpinnerOnItemSelectionChangedListener(SimpleSpinner.OnItemSelectionChangedListener listener) {
        countySpinner.setOnItemSelectionChangedListener(listener);
    }

    public void addToTownSpinner(String town) {
        townSpinner.addSpinnerData(town);
    }

    public void clearTownSpinner() {
        townSpinner.clearSpinner();
    }

    public void setTownSpinnerrOnItemSelectionChangedListener(SimpleSpinner.OnItemSelectionChangedListener listener) {
        townSpinner.setOnItemSelectionChangedListener(listener);
    }

    public Object getSelectedTownSpinnerItem() {
        return townSpinner.getSelectedData();
    }

    public void addConsonantToSpinner(String consonant) {
        consonantSpinner.addSpinnerData(consonant);
    }

    public void clearConsonantSpinner() {
        consonantSpinner.clearSpinner();
    }

    public void setConsonantSpinnerrOnItemSelectionChangedListener(SimpleSpinner.OnItemSelectionChangedListener listener) {
        consonantSpinner.setOnItemSelectionChangedListener(listener);
    }

    public void addStreetToSpinner(String street) {
        streetSpinner.addSpinnerData(street);
    }

    public void clearStreetSpinner() {
        streetSpinner.clearSpinner();
    }

    public Object getSelectedStreetSpinnerItem() {
        return streetSpinner.getSelectedData();
    }

    public Object getSelectedCounty() {
        return countySpinner.getSelectedData();
    }

    public Object getSelectedTown() {
        return townSpinner.getSelectedData();
    }

    public String getInputFirstBuildingNumber() {
        return firstBuildingNumberEditText.getText().toString();
    }

    public String getInputSecondBuildingNumber() {
        return secondBuildingNumberEditText.getText().toString();
    }

    public void setSearchButtonOnClickListener(View.OnClickListener listener) {
        searchButton.setOnClickListener(listener);
    }

    public void setListRowClickListener(ListRowClickListener listener) {
        listRowClickListener = listener;
        signListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewSign data = adapter.getItem(position);
                if(listRowClickListener != null)
                    listRowClickListener.onClick(position, data);
            }
        });
    }

    public interface ListRowClickListener {
        public void onClick(int position, ReviewSign data);
    }

}
