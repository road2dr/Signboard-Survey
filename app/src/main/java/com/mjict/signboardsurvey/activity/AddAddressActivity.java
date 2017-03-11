package com.mjict.signboardsurvey.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SimpleBuildingListAdapter;
import com.mjict.signboardsurvey.model.ui.SimpleBuilding;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

/**
 * Created by Junseo on 2017-03-07.
 */
public class AddAddressActivity extends SABaseActivity {

    private SimpleSpinner countySpinner;
    private SimpleSpinner townSpinner;
    private EditText villageEditText;
    private EditText houseNumberEditText;
    private Button searchButton;
    private ListView listView;
    private SimpleBuildingListAdapter adapter;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.add_address);

        countySpinner = (SimpleSpinner)this.findViewById(R.id.county_spinner);
        townSpinner = (SimpleSpinner)this.findViewById(R.id.town_spinner);
        villageEditText = (EditText)this.findViewById(R.id.village_edit_text);
        houseNumberEditText = (EditText)this.findViewById(R.id.house_number_edit_text);
        searchButton = (Button)this.findViewById(R.id.search_button);
        listView =  (ListView)this.findViewById(R.id.building_list_view);

        adapter = new SimpleBuildingListAdapter(this);
        listView.setAdapter(adapter);
    }

    public void addCountyToSpinner(String county) {
        countySpinner.addSpinnerData(county);
    }

    public void clearCountySpinner() {
        countySpinner.clearSpinner();;
    }

    public Object getSelectedCountyItem() {
        return countySpinner.getSelectedData();
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

    public void setTownSpinnerOnItemSelectionChangedListener(SimpleSpinner.OnItemSelectionChangedListener listener) {
        townSpinner.setOnItemSelectionChangedListener(listener);
    }

    public Object getSelectedTownItem() {
        return townSpinner.getSelectedData();
    }

    public void setSearchButtonOnClickListener(View.OnClickListener listener) {
        searchButton.setOnClickListener(listener);
    }

    public String getInputVillage() {
        return villageEditText.getText().toString();
    }

    public String getInputHouseNumber() {
        return houseNumberEditText.getText().toString();
    }

    public void addToList(SimpleBuilding building) {
        adapter.add(building);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }
}
