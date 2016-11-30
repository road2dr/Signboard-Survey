package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ShopListAdapter;
import com.mjict.signboardsurvey.adapter.ShopTableListAdapter;
import com.mjict.signboardsurvey.handler.SignListRecentActivityHandler;
import com.mjict.signboardsurvey.handler.BuildingProfileActivityHandler;
import com.mjict.signboardsurvey.handler.SignListActivityHandler;
import com.mjict.signboardsurvey.handler.SignListCoverFlowActivityHandler;
import com.mjict.signboardsurvey.handler.SignListPagerActivityHandler;
import com.mjict.signboardsurvey.model.ShopInfo;
import com.mjict.signboardsurvey.model.ThreeShopInfo;
import com.mjict.signboardsurvey.sframework.SActivityHandler;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopListActivity extends SABaseActivity {

    private View buildingInfoView;
    private ListView shopListView;
    private ShopListAdapter listAdapter;
    private ShopTableListAdapter tableAdapter;

    private RadioGroup listOptionRadioGroup;
    private ImageButton addButton;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shop_list;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.shop_list);

        buildingInfoView = this.findViewById(R.id.building_info_view);
        shopListView = (ListView)this.findViewById(R.id.shop_list_view);
        listAdapter = new ShopListAdapter(this);
        tableAdapter = new ShopTableListAdapter(this);
        shopListView.setAdapter(listAdapter);

        listOptionRadioGroup = (RadioGroup)this.findViewById(R.id.list_option_radio_group);
        listOptionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_option_list:
                        shopListView.setAdapter(listAdapter);
                        break;

                    case R.id.radio_option_table:
                        shopListView.setAdapter(tableAdapter);
                        break;
                }
            }
        });

        addButton = new ImageButton(this);
        addButton.setImageResource(R.drawable.btn_plus_sel);
        addButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addButton.setBackground(null);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("junseo", "plus clicked");
            }
        });

        buildingInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopListActivity.this, BuildingProfileActivity.class);
                intent.putExtra(SActivityHandler.HANDLER_CLASS, BuildingProfileActivityHandler.class);
                startActivity(intent);
            }
        });

        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    Intent intent = new Intent(ShopListActivity.this, SignListRecentStyleActivity.class);
                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListRecentActivityHandler.class);
                    startActivity(intent);
                }

                if(position == 1) {
                    Intent intent = new Intent(ShopListActivity.this, SignListCoverFlowActivity.class);
                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListCoverFlowActivityHandler.class);
                    startActivity(intent);
                }

                if(position == 2) {
                    Intent intent = new Intent(ShopListActivity.this, SignListActivity.class);
                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListActivityHandler.class);
                    startActivity(intent);
                }

                if(position == 3) {
                    Intent intent = new Intent(ShopListActivity.this, SignListPagerActivity.class);
                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListPagerActivityHandler.class);
                    startActivity(intent);
                }
            }
        });

        // test
        ShopInfo[] testData = {
            new ShopInfo("희래동", "02-3902-1123", "음식점"),
            new ShopInfo("GS25", "02-6881-3987", "편의점"),
            new ShopInfo("쌍쌍 노래방", "02-1567-3363", "노래방"),
            new ShopInfo("할매국밥", "02-5587-2589", "음식점"),
            new ShopInfo("대전미용실", "02-325-0110", "미용실"),
            new ShopInfo("한돈", "02-554-0776", "음식점"),
            new ShopInfo("가족", "02-8898-8554", "음식점"),
            new ShopInfo("명진횟집", "02-3266-3230", "음식점"),
            new ShopInfo("조선곱창", "02-4475-8654", "음식점"),
            new ShopInfo("엔터PC", "02-6633-3578", "기타"),
            new ShopInfo("문치과", "02-9336-9510", "병의원"),
            new ShopInfo("작살이비인후과", "02-8520-8562", "병의원"),
            new ShopInfo("살살소아과", "02-0147-3265", "병의원"),
            new ShopInfo("진짜약국", "02-9630-4521", "병의원"),
            new ShopInfo("싸다휴대폰", "02-9713-0087", "기타"),
        };

        for(int i=0; i<testData.length; i++) {
            listAdapter.add(testData[i]);

            if(i % 3 == 0) {
                ShopInfo first = testData[i];
                ShopInfo second = (i+1 >= testData.length) ? null : testData[i+1];
                ShopInfo third = (i+2 >= testData.length) ? null : testData[i+2];
                tableAdapter.add(new ThreeShopInfo(first, second, third));
            }
        }
        tableAdapter.add(new ThreeShopInfo(null, null, null));
    }

    private void attachFloatingButton() {
        int size = getResources().getDimensionPixelSize(R.dimen.floating_button_size);
        int margin = getResources().getDimensionPixelSize(R.dimen.floating_button_margin);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.width = size;
        params.height = size;
        params.x = params.x + margin;
        params.y = params.y + margin;

        getWindowManager().addView(addButton, params);
    }



    @Override
    protected void onResume() {
        attachFloatingButton();

        super.onResume();
    }

    @Override
    protected void onPause() {
        getWindowManager().removeView(addButton);

        super.onPause();
    }
}
