package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.BuildingListAdapter;
import com.mjict.signboardsurvey.model.ui.BuildingResult;
import com.mjict.signboardsurvey.widget.SimpleSpinner;


/**
 * Created by Junseo on 2016-07-21.
 */
public class AddressSearchActivity extends SABaseActivity {

    private SimpleSpinner countySpinner;
    private SimpleSpinner townSpinner;
    private SimpleSpinner consonantSpinner;
    private SimpleSpinner streetSpinner;
    private EditText firstBuildingNumberEditText;
    private EditText secondBuildingNumberEditText;

    private Button searchButton;
    private ListView buildingListView;
    private BuildingListAdapter listAdapter;

    private BuildingListItemOnClickListener listItemOnClickListener;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_address_search;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.search_by_address);

        buildingListView = (ListView)this.findViewById(R.id.building_list_view);
        listAdapter = new BuildingListAdapter(this);
        buildingListView.setAdapter(listAdapter);

        countySpinner = (SimpleSpinner)this.findViewById(R.id.county_spinner);
        townSpinner = (SimpleSpinner)this.findViewById(R.id.town_spinner);
        consonantSpinner = (SimpleSpinner)this.findViewById(R.id.consonant_spinner);
        streetSpinner = (SimpleSpinner)this.findViewById(R.id.street_spinner);
        firstBuildingNumberEditText = (EditText)this.findViewById(R.id.first_building_number_text_view);
        secondBuildingNumberEditText = (EditText)this.findViewById(R.id.second_building_number_text_view);

        searchButton = (Button)this.findViewById(R.id.search_button);
    }

    public void setListImage(int index, Bitmap image) {
        listAdapter.setImage(index, image);
    }

    public void setOnListItemClickListener(BuildingListItemOnClickListener listener) {
        listItemOnClickListener = listener;
        buildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BuildingResult info = listAdapter.getItem(position);
                if(listItemOnClickListener != null)
                    listItemOnClickListener.onListItemClicked(position, info);
            }
        });
    }

    public void addToList(BuildingResult data) {
        listAdapter.add(data);
    }

    public void setListItem(int index, BuildingResult building) {
        listAdapter.replaceItem(index, building);
    }

    public void clearBuildingList() {
        listAdapter.clear();
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

    public void addTownToSpinner(String town) {
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

    public void setStreetSpinnerrOnItemSelectionChangedListener(SimpleSpinner.OnItemSelectionChangedListener listener) {
        streetSpinner.setOnItemSelectionChangedListener(listener);
    }

    public Object getSelectedStreetSpinnerItem() {
        return streetSpinner.getSelectedData();
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


    public interface BuildingListItemOnClickListener {
        public void onListItemClicked(int position, BuildingResult info);
    }

//    public static class SimpleBuilding {
//        public int image;
//        public String buildingNumber;
//        public String streetAddress;
//        public String houseNumAddress;
//
//        public SimpleBuilding(int image, String buildingNumber, String streetAddress, String houseNumAddress) {
//            this.image = image;
//            this.buildingNumber = buildingNumber;
//            this.streetAddress = streetAddress;
//            this.houseNumAddress = houseNumAddress;
//        }
//    }
}