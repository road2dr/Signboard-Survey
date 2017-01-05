package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ThreeShopListAdapter;
import com.mjict.signboardsurvey.adapter.TwoShopListAdapter;
import com.mjict.signboardsurvey.model.ui.ShopInfo;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopListActivity extends SABaseActivity {

    private View buildingInfoView;
    private ImageView buildingImageView;
    private TextView buildingNameTextView;
    private TextView streetAddressTextView;
    private TextView houseAddressTextView;
    private ListView shopListView;
    private TwoShopListAdapter twoShopAdapter;
    private ThreeShopListAdapter threeShopListAdapter;
    private RadioGroup listOptionRadioGroup;


//    private ImageButton addButton;

    private OnOptionMenuItemClickListener optionMenuItemClickListener;
    private OnShopListItemClickListener listItemClickListener;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shop_list;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.shop_list);
        this.showOptionButton();
        this.inflateOptionMenu(R.menu.option_menu_building_information);
        this.setOnOptionMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_shop:
                        if(optionMenuItemClickListener != null)
                            optionMenuItemClickListener.addShopClicked();
                        break;
                }
                return true;
            }
        });

        buildingImageView = (ImageView)this.findViewById(R.id.building_image_view);
        buildingNameTextView = (TextView)this.findViewById(R.id.building_name_text_view);
        streetAddressTextView = (TextView)this.findViewById(R.id.street_address_text_view);
        houseAddressTextView = (TextView)this.findViewById(R.id.house_address_text_view);
        buildingInfoView = this.findViewById(R.id.building_info_view);
        shopListView = (ListView)this.findViewById(R.id.shop_list_view);
        twoShopAdapter = new TwoShopListAdapter(this);
        threeShopListAdapter = new ThreeShopListAdapter(this);
        shopListView.setAdapter(twoShopAdapter);

        listOptionRadioGroup = (RadioGroup)this.findViewById(R.id.list_option_radio_group);
        listOptionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_option_list:
                        shopListView.setAdapter(twoShopAdapter);
                        break;

                    case R.id.radio_option_table:
                        shopListView.setAdapter(threeShopListAdapter);
                        break;
                }
            }
        });

        twoShopAdapter.setOnColumnClickListener(new TwoShopListAdapter.OnColumnClickListener() {
            @Override
            public void onClick(int index) {
                if(listItemClickListener != null)
                    listItemClickListener.onShopItemClicked(index);
            }
        });

        threeShopListAdapter.setOnColumnClickListener(new ThreeShopListAdapter.OnColumnClickListener() {
            @Override
            public void onClick(int index) {
                if(listItemClickListener != null)
                    listItemClickListener.onShopItemClicked(index);
            }
        });

//        addButton = new ImageButton(this);
//        addButton.setImageResource(R.drawable.btn_plus_sel);
//        addButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        addButton.setBackground(null);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("junseo", "plus clicked");
//            }
//        });

//        buildingInfoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ShopListActivity.this, BuildingProfileActivity.class);
//                intent.putExtra(SActivityHandler.HANDLER_CLASS, BuildingProfileActivityHandler.class);
//                startActivity(intent);
//            }
//        });
//
//        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position == 0) {
//                    Intent intent = new Intent(ShopListActivity.this, SignListRecentStyleActivity.class);
//                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListRecentActivityHandler.class);
//                    startActivity(intent);
//                }
//
//                if(position == 1) {
//                    Intent intent = new Intent(ShopListActivity.this, SignListCoverFlowActivity.class);
//                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListCoverFlowActivityHandler.class);
//                    startActivity(intent);
//                }
//
//                if(position == 2) {
//                    Intent intent = new Intent(ShopListActivity.this, SignListActivity.class);
//                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListActivityHandler.class);
//                    startActivity(intent);
//                }
//
//                if(position == 3) {
//                    Intent intent = new Intent(ShopListActivity.this, SignListPagerActivity.class);
//                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignListPagerActivityHandler.class);
//                    startActivity(intent);
//                }
//            }
//        });


    }

    @Override
    protected void onResume() {
//        attachFloatingButton();
       super.onResume();
    }

    @Override
    protected void onPause() {
//        getWindowManager().removeView(addButton);
        super.onPause();
    }

    public void setBuildingInfoViewOnClickListener(View.OnClickListener listener) {
        buildingInfoView.setOnClickListener(listener);
    }
    public void setBuildingImage(Bitmap image) {
        buildingImageView.setImageBitmap(image);
    }

    public void setBuildingName(String name) {
        buildingNameTextView.setText(name);
    }

    public void setStreetAddress(String address) {
        streetAddressTextView.setText(address);
    }

    public void setHouseAddress(String address) {
        houseAddressTextView.setText(address);
    }

    public void setOptionMenuItemClickListener(OnOptionMenuItemClickListener listener) {
        optionMenuItemClickListener = listener;
    }

    public void addToList(ShopInfo info) {
        twoShopAdapter.add(info);
        threeShopListAdapter.add(info);
    }

    public void replaceListItem(int index, ShopInfo info) {
        twoShopAdapter.replaceItem(index, info);
        threeShopListAdapter.replaceItem(index, info);
    }

    public void clearList() {
        twoShopAdapter.clear();
        threeShopListAdapter.clear();
    }

    public void setShopListItemOnClickListener(OnShopListItemClickListener listener) {
        listItemClickListener = listener;
    }

//    private void attachFloatingButton() {
//        int size = getResources().getDimensionPixelSize(R.dimen.floating_button_size);
//        int margin = getResources().getDimensionPixelSize(R.dimen.floating_button_margin);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.type = WindowManager.LayoutParams.TYPE_TOAST;
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        params.format = PixelFormat.RGBA_8888;
//        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
//        params.width = size;
//        params.height = size;
//        params.x = params.x + margin;
//        params.y = params.y + margin;
//
//        getWindowManager().addView(addButton, params);
//    }





    public static interface OnOptionMenuItemClickListener {
        public void addShopClicked();
    }

    public static interface OnShopListItemClickListener {
        public void onShopItemClicked(int index);
    }
}
