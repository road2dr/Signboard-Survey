package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.BuildingListAdapter;
import com.mjict.signboardsurvey.adapter.BuildingTableListAdapter;
import com.mjict.signboardsurvey.handler.ShopListActivityHandler;
import com.mjict.signboardsurvey.model.BuildingResult;
import com.mjict.signboardsurvey.model.TwoBuildingResult;
import com.mjict.signboardsurvey.sframework.SActivityHandler;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingSearchActivity extends SABaseActivity {

    private ListView buildingListView;
    private BuildingListAdapter listAdapter;
    private BuildingTableListAdapter tableListAdapter;

    private RadioGroup listOptionRadioGroup;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_building_search;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.title_building_search);

        buildingListView = (ListView)this.findViewById(R.id.building_list_view);
        listAdapter = new BuildingListAdapter(this);
        tableListAdapter = new BuildingTableListAdapter(this);
        buildingListView.setAdapter(listAdapter);

        // test
        buildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BuildingSearchActivity.this, ShopListActivity.class);
                intent.putExtra(SActivityHandler.HANDLER_CLASS, ShopListActivityHandler.class);
                startActivity(intent);
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

        BuildingResult[] testData = {
                new BuildingResult(R.drawable.test_building1, "인천광역시 남동구 구월동 구월남로 94", "안천광역시 남동구 구월동 488-21", "동신 빌딩", "94"),
                new BuildingResult(R.drawable.ic_building, "인천광역시 남동구 구월동 구월남로 55", "안천광역시 남동구 구월동 32-1", "약초 빌딩", "55"),
                new BuildingResult(R.drawable.test_building2, "인천광역시 남동구 구월동 구월남로 1", "안천광역시 남동구 구월동 1706", "큰 빌딩", "1"),
                new BuildingResult(R.drawable.test_building3, "인천광역시 남동구 구월동 구월남로 17", "안천광역시 남동구 구월동 488-55", null, "17"),
                new BuildingResult(R.drawable.ic_building, "인천광역시 남동구 구월동 구월남로 24", "안천광역시 남동구 구월동 2560", "누누 빌딩", "24"),
                new BuildingResult(R.drawable.test_building4, "인천광역시 남동구 구월동 구월남로 32", "안천광역시 남동구 구월동 332", "남산 빌딩", "32"),
                new BuildingResult(R.drawable.ic_building, "인천광역시 남동구 구월동 구월남로 88", "안천광역시 남동구 구월동 33-88", null, "88")
        };

        for(int i=0; i<testData.length; i++) {
            listAdapter.add(testData[i]);
            if(i % 2 == 0) {
                BuildingResult first = testData[i];
                BuildingResult second = (i+1 >= testData.length) ? null : testData[i+1];
                tableListAdapter.add(new TwoBuildingResult(first, second));
            }

        }
    }
}
