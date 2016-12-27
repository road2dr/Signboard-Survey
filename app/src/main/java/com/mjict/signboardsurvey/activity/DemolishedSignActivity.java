package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.DemolitionSignListAdapter;
import com.mjict.signboardsurvey.model.ui.DemolishedSign;
import com.mjict.signboardsurvey.widget.SimpleSpinner;
import com.mjict.signboardsurvey.widget.SwipeMenuListView;


/**
 * Created by Junseo on 2016-07-25.
 */
public class DemolishedSignActivity extends SABaseActivity {
    private SwipeMenuListView listView;
    private SimpleSpinner countySpinner;
    private SimpleSpinner townSpinner;
    private SimpleSpinner consonantSpinner;
    private SimpleSpinner streetSpinner;
    private EditText firstBuildingNumberEditText;
    private EditText secondBuildingNumberEditText;
    private Button searchButton;
    private DemolitionSignListAdapter adapter;

    private ListRowClickListener listRowClickListener;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_demolished_sign;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.demolished_signs);

        listView = (SwipeMenuListView)this.findViewById(R.id.sign_list_view);
        countySpinner = (SimpleSpinner)this.findViewById(R.id.county_spinner);
        townSpinner = (SimpleSpinner)this.findViewById(R.id.town_spinner);
        consonantSpinner = (SimpleSpinner)this.findViewById(R.id.consonant_spinner);
        streetSpinner = (SimpleSpinner)this.findViewById(R.id.street_spinner);
        firstBuildingNumberEditText = (EditText)this.findViewById(R.id.first_building_number_text_view);
        secondBuildingNumberEditText = (EditText)this.findViewById(R.id.second_building_number_text_view);
        searchButton = (Button)this.findViewById(R.id.search_button);

        adapter = new DemolitionSignListAdapter(this);

        listView.setAdapter(adapter);
        listView.setCloseInterpolator(new BounceInterpolator());
        listView.setOpenInterpolator(new BounceInterpolator());
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
                Log.d("junseo", "onSwipeStart");
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                Log.d("junseo", "onSwipeEnd");
            }
        });

        // set MenuStateChangeListener
        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                Log.d("junseo", "onMenuOpen");
            }

            @Override
            public void onMenuClose(int position) {
                Log.d("junseo", "onMenuClose");
            }
        });
    }



    public void setSearchButtonOnClickListener(View.OnClickListener listener) {
        searchButton.setOnClickListener(listener);
    }

    public void addToList(DemolishedSign data) {
        adapter.add(data);
    }

    public void replaceListItem(int position, DemolishedSign data) {
        DemolishedSign sd = adapter.getItem(position);
        adapter.remove(sd);
        adapter.insert(data, position);
    }

    public void removeListItem(int position) {
        DemolishedSign sd = adapter.getItem(position);
        adapter.remove(sd);
    }

    public void clearList() {
        adapter.clear();
    }

    public void setSignImage(int position, Bitmap image) {
        adapter.setSignImage(position, image);
    }

    public void setDemolitionImage(int position, Bitmap image) {
        adapter.setDemolitionImage(position, image);
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

    public void setListRowClickListener(ListRowClickListener listener) {
        listRowClickListener = listener;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DemolishedSign data = adapter.getItem(position);
                if(listRowClickListener != null)
                    listRowClickListener.onClick(position, data);
            }
        });
    }

    public interface ListRowClickListener {
        public void onClick(int position, DemolishedSign data);
    }
}
