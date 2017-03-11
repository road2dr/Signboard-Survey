package com.mjict.signboardsurvey.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SignViewPagerAdapter;
import com.mjict.signboardsurvey.model.ui.SignInputData;
import com.mjict.signboardsurvey.widget.CircleIndicator;
import com.mjict.signboardsurvey.widget.DemolitionImageOptionDialog;
import com.mjict.signboardsurvey.widget.JExpandableLinearLayout;
import com.mjict.signboardsurvey.widget.SignImageOptionDialog;
import com.mjict.signboardsurvey.widget.SimpleSpinner;
import com.mjict.signboardsurvey.widget.TimePickerDialog;

import java.util.Date;

/**
 * Created by Junseo on 2017-02-10.
 */
public class SignPageActivity extends SABaseActivity {

    private ImageButton shopInfoExpandButton;
    private JExpandableLinearLayout shopInfoLayout;

    private ViewPager signViewPager;
    private SignViewPagerAdapter adapter;
    private CircleIndicator pagerIndicator;

    private ScrollView scrollView;
    private GestureDetector mGestureDetector;

    // shop information layout
    private EditText shopNameEditText;
    private EditText phoneNumberEditText;
    private EditText representativeEditText;
    private SimpleSpinner shopCategorySpinner;
    private RadioGroup shopStatusRadioGroup;
    private Button shopConfirmButton;

    private SignImageOptionDialog signImageOptionDialog;
    private TimePickerDialog timePickerDialog;
    private DemolitionImageOptionDialog demolitionImageOptionDialog;

//    private ConfirmButtonOnClickListener confirmButtonOnClickListener;

//    representative_edit_text
//    shop_category_spinner
//    status_radio_group


    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    protected void init() {
        super.init();

        this.setTitle(R.string.detail_sign_information);
        this.disableNavigation();

        shopInfoExpandButton = (ImageButton)this.findViewById(R.id.shop_info_expand_button);
        shopInfoLayout = (JExpandableLinearLayout)this.findViewById(R.id.shop_info_Layout);
        shopInfoExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JExpandableLinearLayout.ExpandAnimationListener listener = new JExpandableLinearLayout.ExpandAnimationListener() {
                    @Override
                    public void onExpandFinished() {
                        int resId = shopInfoLayout.isExpanded() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down;
                        shopInfoExpandButton.setImageResource(resId);
                    }
                };

                if(shopInfoLayout.isExpanded())
                    shopInfoLayout.foldWithAnimation(listener);
                else
                    shopInfoLayout.expandWithAnimation(listener);
            }
        });


        signViewPager = (ViewPager)this.findViewById(R.id.sign_information_view_pager);
        adapter = new SignViewPagerAdapter(this);
        signViewPager.setAdapter(adapter);

        scrollView = (ScrollView)this.findViewById(R.id.scroll_view);

        shopNameEditText = (EditText) this.findViewById(R.id.shop_name_edit_text);
        phoneNumberEditText = (EditText)this.findViewById(R.id.phone_number_edit_text);
        representativeEditText = (EditText)this.findViewById(R.id.representative_edit_text);
        shopCategorySpinner = (SimpleSpinner)this.findViewById(R.id.shop_category_spinner);
        shopStatusRadioGroup = (RadioGroup)this.findViewById(R.id.status_radio_group);
        shopConfirmButton = (Button)this.findViewById(R.id.shop_confirm_button);

        pagerIndicator = new CircleIndicator(SignPageActivity.this);
        adapter.registerDataSetObserver(pagerIndicator.getDataSetObserver());

        signImageOptionDialog = new SignImageOptionDialog(this);
        signImageOptionDialog.show();
        signImageOptionDialog.hide();
//        signImageOptionDialog.create();

        timePickerDialog = new TimePickerDialog(this);
        timePickerDialog.show();
        timePickerDialog.hide();
//        timePickerDialog.create();

        demolitionImageOptionDialog = new DemolitionImageOptionDialog(this);
        demolitionImageOptionDialog.show();
        demolitionImageOptionDialog.hide();
//        demolitionImageOptionDialog.create();

        addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                pagerIndicator.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                pagerIndicator.setVisibility(View.VISIBLE);
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        // test


    }

    @Override
    protected void onResume() {
        // add indicator on windowmanager

        int h = (int)getResources().getDimension(R.dimen.view_pager_indicator_height);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = h;

        int size = (int)getResources().getDimension(R.dimen.view_pager_indicator_size);
        pagerIndicator.setIndicatorSize(size);
        pagerIndicator.setViewPager(signViewPager);

        getWindowManager().addView(pagerIndicator, params);

        super.onResume();
    }

    @Override
    protected void onPause() {
        getWindowManager().removeView(pagerIndicator);

        super.onPause();
    }

//    @Override
//    protected int getMainContentView() {
//        return R.layout.activity_sign_information;
//    }

//    shopNameEditText = (EditText)this.findViewById(R.id.shop_name_edit_text);
//    phoneNumberEditText = (EditText)this.findViewById(R.id.phone_number_edit_text);
//    representativeEditText = (EditText)this.findViewById(R.id.representative_edit_text);
//    shopCategorySpinner = (SimpleSpinner)this.findViewById(R.id.shop_category_spinner);
//    shopStatusRadioGroup = (RadioGroup)this.findViewById(R.id.shop_status_spinner);

    public void setShopInfoViewEnabled(boolean enabled) {
        shopNameEditText.setEnabled(enabled);
        phoneNumberEditText.setEnabled(enabled);
        representativeEditText.setEnabled(enabled);
        shopCategorySpinner.setEnabled(enabled);
        for(int i=0; i<shopStatusRadioGroup.getChildCount(); i++) {
            View child = shopStatusRadioGroup.getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void addToPager(SignInputData data) {
        adapter.add(data);
    }

    public void addNewPageAtLast() {
        int idx = adapter.getCount();
        if(idx > 0)
            pagerIndicator.setIndicatorResource(idx-1, null);
        pagerIndicator.setIndicatorResource(idx, R.drawable.ic_indicator_new);
        adapter.add(new SignInputData());


    }

    public void setInputData(int page, SignInputData data) {
        adapter.setInputData(page, data);
    }

    public SignInputData getCurrentInputData(int page) {
        return adapter.getCurrentInputData(page);
    }

    public void addToTypeSpinner(SimpleSpinner.SpinnerItem item) {
        adapter.addToTypeSpinner(item);
    }

    public void addToStatsSpinner(SimpleSpinner.SpinnerItem item) {
        adapter.addToStatsSpinner(item);
    }

    public void addToLightSpinner(SimpleSpinner.SpinnerItem item) {
        adapter.addToLightSpinner(item);
    }

    public void addToReviewSpinner(SimpleSpinner.SpinnerItem item) {
        adapter.addToReviewSpinner(item);
    }

    public void addToInstallSideSpinner(SimpleSpinner.SpinnerItem item) {
        adapter.addToInstallSideSpinner(item);
    }

    public void addToUniquenessSpinner(SimpleSpinner.SpinnerItem item) {
        adapter.addToUniquenessSpinner(item);
    }

    public void addToResultSpinner(SimpleSpinner.SpinnerItem item) {
        adapter.addToResultSpinner(item);
    }


    public String getInputShopName() {
        return shopNameEditText.getText().toString();
    }

    public void setShopNameText(String text) {
        shopNameEditText.setText(text);
    }

    public String getInputPhoneNumber() {
        return phoneNumberEditText.getText().toString();
    }

    public void setPhoneNumberText(String text) {
        phoneNumberEditText.setText(text);
    }

    public String getInputRepresentative() {
        return representativeEditText.getText().toString();
    }

    public void setRepresentativeText(String text) {
        representativeEditText.setText(text);
    }

    public void addToShopCategorySpinner(Object id, Object data) {
        shopCategorySpinner.addSpinnerData(id, data);
    }

    public void setShopCategorySpinnerSelection(Object id) {
        shopCategorySpinner.setSpinnerSelection(id);
    }

    public void setShopStatusChecked(int index) {
        View child = shopStatusRadioGroup.getChildAt(index);
        if(child != null)
            ((RadioButton)child).setChecked(true);
    }

    public Object getSelectesShopCategory() {
        return shopCategorySpinner.getSelectedData();
    }

    public int getCheckedShopStatus() {
        int id = shopStatusRadioGroup.getCheckedRadioButtonId();
        int index = -1;
        if(id == R.id.normal_radio_button)
            index = 0;
        if(id == R.id.demolish_radio_button)
            index = 1;

        return index;
    }

    public void setShopStatusRadioOnCheckChangeListener(RadioGroup.OnCheckedChangeListener listener) {
        shopStatusRadioGroup.setOnCheckedChangeListener(listener);
    }

    public void setShopConfirmButtonOnClickListener(View.OnClickListener listener) {
        shopConfirmButton.setOnClickListener(listener);
    }

    public void setCurrentPage(int index) {
        signViewPager.setCurrentItem(index);
    }

    public void setConfirmButtonOnClickListener(SignViewPagerAdapter.ConfirmButtonOnClickListener listener) {
        adapter.setConfirmButtonOnClickListener(listener);
    }

    public void setAddSignImageButtonOnClickListener(SignViewPagerAdapter.AddSignImageButtonOnClickListener listener) {
        adapter.setAddImageButtonOnClickListener(listener);
    }

    public void setSignImageOnClickListener(SignViewPagerAdapter.SignImageOnClickListener listener) {
        adapter.setSignImageOnClickListener(listener);
    }

    public void setDateTextOnClickListener(SignViewPagerAdapter.DateTextOnClickListener listener) {
        adapter.setDateTextOnClickListener(listener);
    }

    public void setDemolitionImageOnClickListener(SignViewPagerAdapter.DemolitionImageOnClickListener listener) {
        adapter.setDemolitionImageOnClickListener(listener);
    }

    public void setAddDemolitionImageButtonOnClickListener(SignViewPagerAdapter.AddDemolitionImageButtonOnClickListener listener) {
        adapter.setAddDemolitionImageButtonOnClickListener(listener);
    }

//    public void setSizeEditTextOnClickListener(SignViewPagerAdapter.SizeEditTextOnClickListener listener) {
//        adapter.setSizeEditTextOnClickListener(listener);
//    }

    public void setLastContentEditTextFocusChangeListener(SignViewPagerAdapter.ContentTextFocusChangedListener listener) {
        adapter.setLastContentTextFocusChangedListener(listener);
    }

    public void setSignLengthText(int page, String text) {
        adapter.setSignLengthText(page,text);
    }

    public void setSignWidthText(int page, String text) {
        adapter.setSignWidthText(page, text);
    }

//    public void setSignImage(int page, Bitmap image) {
//        adapter.setSignImage(page, image);
//    }
//
//    public void setDemolishSignImage(int page, int resId) {
//        adapter.setDemolitionImage(page, resId);
//    }
//
//    public void setDemolishSignImage(int page, Bitmap image) {
//        adapter.setDemolitionImage(page, image);
//    }
//
//    public void setSignImage(int page, int resId) {
//        adapter.setSignImage(page, resId);
//    }

    public void showSignImageOptionDialog() {
        signImageOptionDialog.show();
    }

    public void setSignImageOptionDialogListener(SignImageOptionDialog.SignImageOptionDialogListener listener) {
        signImageOptionDialog.setSignImageOptionDialogListener(listener);
    }

    public void hideSignImageOptionDialog() {
        signImageOptionDialog.dismiss();
    }

    public void showTimePickerDialog() {
        timePickerDialog.show();
    }

    public void setTime(int page, Date time) {
        adapter.setDate(page, time);
    }

    public void setContentText(int page, String text) {
        adapter.setContentText(page, text);
    }

//    public void showAddDemolishImageButton(int page) {
//        adapter.setVisibleAddDemolishImageButton(page);
//    }

    public void hideTimePickerDialog() {
        timePickerDialog.dismiss();
    }

    public void setTimePickerConfirmButtonOnClickListener(TimePickerDialog.ConfirmButtonOnClickListener listener) {
        timePickerDialog.setConfirmButtonOnClickListener(listener);
    }

    public void setSignImage(int page, String path) {
        adapter.setSignImage(page, path);
    }

    public void setDemolishImage(int page, String path) {
        adapter.setDemolishImage(page, path);
    }

    public String currentSignImagePath(int page) {
        return adapter.getSignImagePath(page);
    }

    public String currentDemolishImagePath(int page) {
        return adapter.getDemolishImagePath(page);
    }

    public void showDemolitionImageOptionDialog() {
        demolitionImageOptionDialog.show();
    }

    public void hideDemolitionImageOptionDialog() {
        demolitionImageOptionDialog.dismiss();
    }

    public void setDemolitionImageOptionDailogListener(DemolitionImageOptionDialog.DemolitionImageOptionDialogListener listener) {
        demolitionImageOptionDialog.setDemolitionImageOptionDialogListener(listener);
    }

    public void setAutoJudgementButtonOnClickListener(SignViewPagerAdapter.AutoJudgementButtonOnClickListener listener) {
        adapter.setAutoJudgementButtonOnClickListener(listener);
    }

    public void setResultSpinnerSelection(int page, Object item) {
        adapter.setResultSpinnerSelection(page, item);
    }

}
