package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ShopAndSignListAdapter;
import com.mjict.signboardsurvey.model.ui.ShopAndSign;
import com.mjict.signboardsurvey.model.ui.ShopInputData;
import com.mjict.signboardsurvey.widget.JExpandableLinearLayout;
import com.mjict.signboardsurvey.widget.ShopInformationDialog;
import com.mjict.signboardsurvey.widget.SignOptionDialog;

/**
 * Created by Junseo on 2017-02-07.
 */
public class BuildingTotalInformationActivity extends SABaseActivity {
    private ImageButton buildingExpandButton;
    private JExpandableLinearLayout buildingInfoLayout;
    private View buildingInfoView;
    private ViewGroup loadingImageLayout;
    private ImageView buildingImageView;
    private ImageButton prevImageButton;
    private ImageButton nextImageButton;
    private TextView pageTextView;
    private TextView streetAddressTextView;
    private TextView houseNumberAddressTextView;
    private TextView buildingNameTextView;
    private TextView areaTypeTextView;

    private TextView statusTextView;
    private ExpandableListView signListView;
    private ShopAndSignListAdapter adapter;

    private ShopInformationDialog shopInfoDlg;
//    private ShopOptionDialog shopOptDlg;
//    private PopupMenu shopOptionMenu;
    private SignOptionDialog signOptDlg;


    private ToolBarOptionMenuOnClickListener toolBarOptionMenuListener;
    private ShopOptionMenuOnClickListener shopOptionMenuOnClickListener;
    private SignListOnChildRowLongClickListener childRowLongClickListener;

    private int currentImg = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_building_total_information;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void init() {
        super.init();

        this.setTitle(R.string.shop_and_sign_information);

        statusTextView = (TextView)this.findViewById(R.id.status_text_view);
        signListView = (ExpandableListView)this.findViewById(R.id.sign_list_view);

        buildingExpandButton = (ImageButton)this.findViewById(R.id.building_info_expand_button);
        buildingInfoLayout = (JExpandableLinearLayout)this.findViewById(R.id.building_info_layout);
        buildingInfoView = this.findViewById(R.id.building_info_view);
        buildingImageView = (ImageView)this.findViewById(R.id.building_image_view);
        loadingImageLayout = (ViewGroup)this.findViewById(R.id.loading_image_layout);
        prevImageButton = (ImageButton)this.findViewById(R.id.prev_building_button);
        nextImageButton = (ImageButton)this.findViewById(R.id.next_building_button);
        pageTextView = (TextView)this.findViewById(R.id.page_text_view);
        streetAddressTextView = (TextView)this.findViewById(R.id.street_address_text_view);
        houseNumberAddressTextView = (TextView)this.findViewById(R.id.house_number_address_text_view);
        buildingNameTextView = (TextView)this.findViewById(R.id.building_name_text_view);
        areaTypeTextView = (TextView) this.findViewById(R.id.area_type_text_view);

        shopInfoDlg = new ShopInformationDialog(this);
        shopInfoDlg.show();
        shopInfoDlg.hide();
//        shopInfoDlg.create();

        signOptDlg = new SignOptionDialog(this);
        signOptDlg.show();
        signOptDlg.hide();
//        signOptDlg.create();


        buildingExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JExpandableLinearLayout.ExpandAnimationListener listener = new JExpandableLinearLayout.ExpandAnimationListener() {
                    @Override
                    public void onExpandFinished() {
                        int resId = buildingInfoLayout.isExpanded() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down;
                        buildingExpandButton.setImageResource(resId);
                    }
                };

                if(buildingInfoLayout.isExpanded())
                    buildingInfoLayout.foldWithAnimation(listener);
                else
                    buildingInfoLayout.expandWithAnimation(listener);
            }
        });


        signListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        signListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        adapter = new ShopAndSignListAdapter(this);

//        adapter.setSignPictureOnClickListener(new SignListAdapter.SignPictureOnClickListener() {
//            @Override
//            public void onPictureClicked(View view, int gpos, int cpos) {
//            }
//        });

        signListView.setAdapter(adapter);

        signListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long packedPosition =  signListView.getExpandableListPosition(position);
                int type = ExpandableListView.getPackedPositionType(packedPosition);

                if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
                    int groupPos = ExpandableListView.getPackedPositionGroup(packedPosition);
                    int childPos = ExpandableListView.getPackedPositionChild(packedPosition);

                    if(childRowLongClickListener != null)
                        childRowLongClickListener.onChildRowLongClicked(groupPos, childPos);

                } else if( type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPos = ExpandableListView.getPackedPositionGroup(packedPosition);
                }
                return true;
            }
        });

        this.inflateOptionMenu(R.menu.option_menu_building_information);
        this.showOptionButton();
        this.setOnOptionMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_shop:
                        if(toolBarOptionMenuListener != null)
                            toolBarOptionMenuListener.onAddShopMenuClicked();
                        break;
                }
                return true;
            }
        });

    }

    /////////////////
//    public void setChildRowLongClickListener() {
//        signListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return false;
//            }
//        });
//    }

    public void setBuildingInfoViewOnClickListener(View.OnClickListener listener) {
        buildingInfoView.setOnClickListener(listener);
    }

    public void showShopOptionMenu(View view, final int position, boolean deletable) {
        PopupMenu shopOptionMenu = new PopupMenu(BuildingTotalInformationActivity.this, view);
        shopOptionMenu.inflate(R.menu.option_menu_shop);
        shopOptionMenu.show();
        shopOptionMenu.getMenu().findItem(R.id.delete_shop).setVisible(deletable);
        shopOptionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(shopOptionMenuOnClickListener == null)
                    return false;

                switch (item.getItemId()) {
                    case R.id.add_sign:
                        shopOptionMenuOnClickListener.onAddSignMenuClicked(position);
                        break;
                    case R.id.modify_shop:
                        shopOptionMenuOnClickListener.onModifyShopMenuClicked(position);
                        break;
                    case R.id.delete_shop:
                        shopOptionMenuOnClickListener.onDeleteShopMenuClicked(position);
                        break;
                }
                return false;
            }
        });
    }

    public void setShopOptionMenuOnClickListener(ShopOptionMenuOnClickListener listener) {
        shopOptionMenuOnClickListener = listener;
    }

    public void setChildRowLongClickListener(SignListOnChildRowLongClickListener listener) {
        childRowLongClickListener = listener;
    }

    public void setSignPictureOnClickListener(ShopAndSignListAdapter.SignPictureOnClickListener listener) {
        adapter.setSignPictureOnClickListener(listener);
    }

    public void setSignListOnChildRowClickListener(final SignListOnChildRowClickListener listener) {
        signListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(listener != null)
                    listener.onChildRowClicked(groupPosition, childPosition);

                return false;
            }
        });
    }

    public void showSignOptionDialog(boolean deletable) {
        signOptDlg.show();
        signOptDlg.setDeleteButtonVisible(deletable);
    }

    public void hideSignOptionDialog() {
        signOptDlg.dismiss();
    }

    public void setSignOptionDialogButtonClickListener(SignOptionDialog.SignOptionDialogOnClickListener listener) {
        signOptDlg.setSignOptionDailogOnClickListener(listener);
    }

    public void setStatusText(String text) {
        statusTextView.setText(text);
    }

    public void setGroupOptionButtonOnclickListener(ShopAndSignListAdapter.GroupOptionButtonOnClickListener listener) {
        adapter.setGroupOptionButtonOnClickListener(listener);
    }

//    public void showShopOptionDialog() {
//        shopOptDlg.show();
//    }
//
//    public void hideShopOptionDialog() {
//        shopOptDlg.dismiss();
//    }

    public void deleteChildRow(int groupPos, int childPos) {
        adapter.removeChild(groupPos, childPos);
    }

    public void deleteGroupRow(int group) {
        adapter.removeGroup(group);
    }

    public int getChildCount(int group) {
        return adapter.getChildrenCount(group);
    }

//    public void setShopOptionDialogOnClickListener(ShopOptionDialog.ShopOptionDialogOnClickListener listener) {
//        shopOptDlg.setShopOptionDailogOnClickListener(listener);
//    }

    public void showLoadingBuildingImage() {
        buildingImageView.setVisibility(View.INVISIBLE);
        loadingImageLayout.setVisibility(View.VISIBLE);
    }

    public void setSignImage(int group, int child, Bitmap image) {
        adapter.setSignImage(group, child, image);
    }

    public void addToList(ShopAndSign info) {
        adapter.addShopInformation(info);
    }

    public ShopAndSign getListItem(int position) {
        return (ShopAndSign)adapter.getGroup(position);
    }

    public void setListData(int group, ShopAndSign info) {
        adapter.setShopInformation(group, info);
    }

    public int getListImteCount() {
        return adapter.getGroupCount();
    }

    public void clearList() {
        adapter.clear();
    }


//    public List<Boolean> getExpandedStatus() {
//        List<Boolean> expandedRows = new ArrayList<>();
//        for(int i=0; i<signListView.getCount(); i++) {
//            expandedRows.add(signListView.isGroupExpanded(i));
//        }
//
//        return expandedRows;
//    }

    public void setBuildingNameText(String name) {
        buildingNameTextView.setText(name);
    }

    public void setStreetAddressText(String address) {
        streetAddressTextView.setText(address);
    }

    public void setHouseNumberAddressText(String address) {
        houseNumberAddressTextView.setText(address);
    }

//    public void setAreaTypeText(String area) {
//        areaTypeTextView.setText(area);
//    }
//
//    public void addToAreaTypeSpinner(Object id, Object data) {
//        areaTypeSpinner.addSpinnerData(id, data);
//    }
//
//    public Object getSelectedAreaType() {
//        return areaTypeSpinner.getSelectedData();
//    }
//
//    public void setAreaTypeSpinnerSelection(Object id) {
//        areaTypeSpinner.setSpinnerSelection(id);
//    }
//
//    public void setAreaTypeSpinnerSelectionChangeListener(SimpleSpinner.OnItemSelectionChangedByTouchListener listener) {
//        areaTypeSpinner.setOnItemSelectionChangedByTouchListener(listener);
//    }

    public void setAreaTypeText(String text) {
        areaTypeTextView.setText(text);
    }

    public void setBuildingImages(Bitmap image) {
        loadingImageLayout.setVisibility(View.INVISIBLE);
        buildingImageView.setVisibility(View.VISIBLE);
        if(image == null)
            buildingImageView.setImageResource(R.drawable.ic_no_image);
        else
            buildingImageView.setImageBitmap(image);
    }

    public void setBuildingImages(int resId) {
        loadingImageLayout.setVisibility(View.INVISIBLE);
        buildingImageView.setVisibility(View.VISIBLE);

        buildingImageView.setImageResource(resId);
    }

    public void setBuildingImageViewOnClickListener(View.OnClickListener listener) {
        buildingImageView.setOnClickListener(listener);
    }

    public void setPageText(String text) {
        pageTextView.setText(text);
    }

    public void setPrevImageButtonOnClickListener(View.OnClickListener listener) {
        prevImageButton.setOnClickListener(listener);
    }

    public void setNextImageButtonOnClickListener(View.OnClickListener listener) {
        nextImageButton.setOnClickListener(listener);
    }

    public void setToolBarOptionMenuOnClickListener(ToolBarOptionMenuOnClickListener listener) {
        toolBarOptionMenuListener = listener;
    }

    public void showShopInformationDialog(ShopInformationDialog.ShopInformationDialogOnClickListener listener) {
        shopInfoDlg.setShopInformationDialogOnClickListener(listener);
        shopInfoDlg.show();
    }

    public void hideShopInformationDialog() {
        shopInfoDlg.hide();
    }

    public void setShopInputData(ShopInputData inputData) {
        shopInfoDlg.setInputData(inputData);
    }

//    public void setShopInformationDialogListener(ShopInformationDialog.ShopInformationDialogOnClickListener listener) {
//        shopInfoDlg.setShopInformationDialogOnClickListener(listener);
//    }

    public void addToShopCategorySpinner(Object id, Object data) {
        shopInfoDlg.addToCategorySpinner(id, data);
    }

    public interface SignListOnChildRowLongClickListener {
        public void onChildRowLongClicked(int group, int child);
    }

    public interface SignListOnChildRowClickListener {
        public void onChildRowClicked(int group, int child);
    }

    public interface ToolBarOptionMenuOnClickListener {
        public void onAddShopMenuClicked();
    }

    public interface ShopOptionMenuOnClickListener {
        public void onAddSignMenuClicked(int position);
        public void onModifyShopMenuClicked(int position);
        public void onDeleteShopMenuClicked(int position);
//        public void onShutdownShopClicked(int position);
    }


}
