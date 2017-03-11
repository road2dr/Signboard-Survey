package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ThreeShopListAdapter;
import com.mjict.signboardsurvey.adapter.TwoShopListAdapter;
import com.mjict.signboardsurvey.model.ui.ShopInfo;
import com.mjict.signboardsurvey.widget.ShopOptionDialog;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopListActivity extends SABaseActivity {

    private View buildingInfoView;
    private ImageButton addShopButton;
    private ImageView buildingImageView;
    private TextView buildingNameTextView;
    private TextView streetAddressTextView;
    private TextView houseAddressTextView;
    private ListView shopListView;
    private TwoShopListAdapter twoShopAdapter;
    private ThreeShopListAdapter threeShopListAdapter;
    private RadioGroup listOptionRadioGroup;
    private ShopOptionDialog shopOptionDialog;

    private RadioGroup sortRadioGroup;
    private RadioButton nameSortRadio;
//    private RadioButton timeSortRadio;

    private OnShopListItemClickListener listItemClickListener;
    private OnSortButtonClickListener sortButtonClickListener;
//    private boolean checkChanged = false;
    private boolean nameSortReversed = false;
//    private boolean timeSortReversed = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shop_list;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.shop_list);
//        this.showOptionButton();
//        this.inflateOptionMenu(R.menu.option_menu_building_information);
//        this.setOnOptionMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.add_shop:
//                        if(optionMenuItemClickListener != null)
//                            optionMenuItemClickListener.addShopClicked();
//                        break;
//                }
//                return true;
//            }
//        });

        addShopButton = (ImageButton)this.findViewById(R.id.add_shop_button);
        buildingImageView = (ImageView)this.findViewById(R.id.building_image_view);
        buildingNameTextView = (TextView)this.findViewById(R.id.building_name_text_view);
        streetAddressTextView = (TextView)this.findViewById(R.id.street_address_text_view);
        houseAddressTextView = (TextView)this.findViewById(R.id.house_address_text_view);
        buildingInfoView = this.findViewById(R.id.building_info_view);
        shopListView = (ListView)this.findViewById(R.id.shop_list_view);
        twoShopAdapter = new TwoShopListAdapter(this);
        threeShopListAdapter = new ThreeShopListAdapter(this);
        shopListView.setAdapter(twoShopAdapter);

        sortRadioGroup = (RadioGroup)this.findViewById(R.id.sort_radio_group);
        nameSortRadio = (RadioButton)this.findViewById(R.id.name_sort_radio);
//        timeSortRadio = (RadioButton)this.findViewById(R.id.time_sort_radio);

        shopOptionDialog = new ShopOptionDialog(this);
        shopOptionDialog.show();
        shopOptionDialog.hide();
//        shopOptionDialog.create();

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

            @Override
            public void onLongClick(int index) {
                if(listItemClickListener != null)
                    listItemClickListener.onShopItemLongClicked(index);
            }
        });

        threeShopListAdapter.setOnColumnClickListener(new ThreeShopListAdapter.OnColumnClickListener() {
            @Override
            public void onClick(int index) {
                if(listItemClickListener != null)
                    listItemClickListener.onShopItemClicked(index);
            }

            @Override
            public void onLongClick(int index) {
                if(listItemClickListener != null)
                    listItemClickListener.onShopItemLongClicked(index);
            }
        });


//        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                checkChanged = true;
//            }
//        });

        nameSortRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameSortReversed = !nameSortReversed;
                int resId = nameSortReversed ? R.drawable.ic_sort_arrow_down : R.drawable.ic_sort_arrow_up;
                nameSortRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
                if(sortButtonClickListener != null)
                    sortButtonClickListener.onNameSortClicked(nameSortReversed);
            }
        });

//        timeSortRadio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(checkChanged) {
//                    checkChanged = false;
//                    return;
//                }
//                timeSortReversed = !timeSortReversed;
//                int resId = timeSortReversed ? R.drawable.ic_sort_arrow_down : R.drawable.ic_sort_arrow_up;
//                timeSortRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
//                if(sortButtonClickListener != null)
//                    sortButtonClickListener.onTimeSortClicked(timeSortReversed);
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

    @Override
    protected void onDestroy() {
        if(shopOptionDialog != null)
            shopOptionDialog.dismiss();

        super.onDestroy();
    }

    public void setAddShopButtonOnClickListener(View.OnClickListener listener) {
        addShopButton.setOnClickListener(listener);
    }

    public void setOnSortButtonClickListener(OnSortButtonClickListener listener) {
        sortButtonClickListener = listener;
    }

    public void setBuildingInfoViewOnClickListener(View.OnClickListener listener) {
        buildingInfoView.setOnClickListener(listener);
    }
    public void setBuildingImage(Bitmap image) {
        if(image != null)
            buildingImageView.setImageBitmap(image);
        else
            buildingImageView.setImageResource(R.drawable.ic_building);
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

//    public void setOptionMenuItemClickListener(OnOptionMenuItemClickListener listener) {
//        optionMenuItemClickListener = listener;
//    }

    public void addToList(ShopInfo info) {
        twoShopAdapter.add(info);
        threeShopListAdapter.add(info);
    }

    public void replaceListItem(final int index, final ShopInfo info) {
        twoShopAdapter.replaceItem(index, info);
        threeShopListAdapter.replaceItem(index, info);
    }

    public void removeListItem(int index) {
        twoShopAdapter.removeColumnItem(index);
        threeShopListAdapter.removeColumnItem(index);
    }

    public void clearList() {
        twoShopAdapter.clear();
        threeShopListAdapter.clear();
    }

    public void setShopListItemOnClickListener(OnShopListItemClickListener listener) {
        listItemClickListener = listener;
    }

    public void showShopOptionDialog(ShopOptionDialog.ShopOptionDialogOnClickListener listener, boolean delete) {
        shopOptionDialog.setShopOptionDailogOnClickListener(listener);
        shopOptionDialog.setDeleteButtonVisible(delete);
        shopOptionDialog.show();
    }

    public void hideShopOptionDialog() {
        shopOptionDialog.hide();
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
        public void onShopItemLongClicked(int index);
    }

    public interface OnSortButtonClickListener {
        public void onNameSortClicked(boolean reversed);
    }
}
