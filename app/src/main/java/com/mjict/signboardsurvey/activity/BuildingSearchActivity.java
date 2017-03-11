package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.BuildingListAdapter;
import com.mjict.signboardsurvey.adapter.BuildingTableListAdapter;
import com.mjict.signboardsurvey.model.ui.BuildingResult;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingSearchActivity extends SABaseActivity {

    private TextView baseAddressTextView;
    private EditText firstNumberEditText;
    private EditText secondNumberEditText;
    private Button searchButton;
    private ListView buildingListView;
    private BuildingListAdapter listAdapter;
    private BuildingTableListAdapter tableListAdapter;
    private BuildingListItemOnClickListener itemOnClickListener;

    private RadioGroup listOptionRadioGroup;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_building_search;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.title_building_search);

        baseAddressTextView = (TextView)this.findViewById(R.id.base_address_text_view);
        firstNumberEditText = (EditText)this.findViewById(R.id.first_number_edit_text);
        secondNumberEditText = (EditText)this.findViewById(R.id.second_number_edit_text);
        searchButton = (Button)this.findViewById(R.id.search_button);
        buildingListView = (ListView)this.findViewById(R.id.building_list_view);
        listAdapter = new BuildingListAdapter(this);
        tableListAdapter = new BuildingTableListAdapter(this);
        buildingListView.setAdapter(listAdapter);

        buildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listOptionRadioGroup.getCheckedRadioButtonId() == R.id.radio_option_list) {
                    BuildingResult b = listAdapter.getItem(position);
                    if(itemOnClickListener != null)
                        itemOnClickListener.onItemClicked(position, b);
                }
            }
        });

        tableListAdapter.setOnColumnClickListener(new BuildingTableListAdapter.OnColumnClickListener() {
            @Override
            public void onClick(int index) {
                BuildingResult b = tableListAdapter.getColumnItem(index);
                if(itemOnClickListener != null)
                    itemOnClickListener.onItemClicked(index, b);
            }
        });

        listOptionRadioGroup = (RadioGroup)this.findViewById(R.id.list_option_radio_group);
        listOptionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_option_list:
                        buildingListView.setAdapter(listAdapter);
                        break;

                    case R.id.radio_option_table:
                        buildingListView.setAdapter(tableListAdapter);
                        break;
                }
            }
        });
    }

    public void setBaseAddressText(String text) {
        baseAddressTextView.setText(text);
    }

    public String getInputFirstNumber() {
        return firstNumberEditText.getText().toString();
    }

    public String getInputSecondNumber() {
        return secondNumberEditText.getText().toString();
    }

    public void setSearchButtonOnClickListener(View.OnClickListener listener) {
        searchButton.setOnClickListener(listener);
    }

    public void addToList(BuildingResult building) {
        tableListAdapter.add(building);
        listAdapter.add(building);
    }

    public void setListImage(int position, Bitmap image) {
        tableListAdapter.setImage(position, image);
        listAdapter.setImage(position, image);
    }

    public void clearListView() {
        listAdapter.clear();
        tableListAdapter.clear();
    }

    public void setListItemOnClickListener(BuildingListItemOnClickListener listener) {
        itemOnClickListener = listener;
    }

    public static interface BuildingListItemOnClickListener {
        public void onItemClicked(int position, BuildingResult building);
    }

}
