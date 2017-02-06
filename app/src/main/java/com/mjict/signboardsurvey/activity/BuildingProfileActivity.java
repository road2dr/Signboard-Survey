package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ImageViewPagerAdapter;
import com.mjict.signboardsurvey.widget.CircleIndicator;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

/**
 * Created by Junseo on 2016-11-15.
 */
public class BuildingProfileActivity extends SABaseActivity {

    // TODO 건물 사진 갯수에 따라 다른 UI를 쓸 수도 있음. 확인 해볼 것
    private TextView titleTextView;
    private ViewPager imagePager;
    private View viewPagerLayout;
    private CircleIndicator imageIndicator;
    private TextView streetAddressTextView;
    private TextView houseAddressTextView;
    private TextView shopInfoTextView;
    private TextView signInfoTextView;
    private SimpleSpinner areaTypeSpinner;
    private Button applyButton;
    private ImageViewPagerAdapter imageAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_building_profile;
    }

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();

        titleTextView = (TextView)this.findViewById(R.id.title_text_view);
        imagePager = (ViewPager)this.findViewById(R.id.image_pager);
        imageIndicator = (CircleIndicator)this.findViewById(R.id.image_indicator);
//        addPicButton = (ImageButton)this.findViewById(R.id.add_pic_button);
        streetAddressTextView = (TextView)this.findViewById(R.id.street_address_text_view);
        houseAddressTextView = (TextView)this.findViewById(R.id.house_address_text_view);
        shopInfoTextView = (TextView)this.findViewById(R.id.shop_info_text_view);
        signInfoTextView = (TextView) this.findViewById(R.id.sign_info_text_view);
        areaTypeSpinner = (SimpleSpinner)this.findViewById(R.id.area_type_spinner);
        applyButton = (Button)this.findViewById(R.id.apply_button);
        viewPagerLayout = this.findViewById(R.id.view_page_layout);

        imageAdapter = new ImageViewPagerAdapter(this);
        imagePager.setAdapter(imageAdapter);
        imageIndicator.setViewPager(imagePager);
        imageAdapter.registerDataSetObserver(imageIndicator.getDataSetObserver());

        //
//        // test
//        imageAdapter.addImage(R.drawable.test_building1);
//        imageAdapter.addImage(R.drawable.test_building2);
//        imageAdapter.addImage(R.drawable.test_building3);
//        imageAdapter.addImage(R.drawable.ic_building);
//        imageAdapter.addImage(R.drawable.test_building4);
//
////        (R.drawable.test_building1, "인천광역시 남동구 구월동 구월남로 94", "안천광역시 남동구 구월동 488-21", "동신 빌딩", "94")
//        titleTextView.setText("동산 빌딩");
//        streetAddressTextView.setText("인천광역시 남동구 구월동 구월남로 94");
//        houseAddressTextView.setText("안천광역시 남동구 구월동 488-21");
//        shopInfoTextView.setText("12 건");
//        signInfoTextView.setText("23 건");
    }

    public void setBuildingName(String title) {
        titleTextView.setText(title);
    }

    public int getCurrentPage() {
        return imagePager.getCurrentItem();
    }

    public void setImagePageOnChageListener(ViewPager.OnPageChangeListener listener) {
        imagePager.addOnPageChangeListener(listener);
    }

    public void setCurrentPage(int page) {
//        imagePager.post(new Runnable() {
//            @Override
//            public void run() {
//                imagePager.setCurrentItem(page, true);
//            }
//        });
        imagePager.setCurrentItem(page);
    }

//    public void setImagePagerOnItemClickListener(final ImageViewPagerAdapter.OnPagerItemClickListener listener) {
////        imageAdapter.setOnItemClickListener(listener);
//        imagePager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int cur = imagePager.getCurrentItem();
//                listener.onItemClick(cur);
//            }
//        });
//    }

    public void setViewPagerLayoutOnClickListener(final View.OnClickListener listener) {
        viewPagerLayout.setOnClickListener(listener);
        imageAdapter.setOnItemClickListener(new ImageViewPagerAdapter.OnPagerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listener.onClick(viewPagerLayout);
            }
        });
    }

    public void showImage(Bitmap image) {
        imageAdapter.showImage(imagePager.getCurrentItem(), image);
    }

    public void showLoading() {
        imageAdapter.showLoading(imagePager.getCurrentItem());
    }

    public void setImageCount(int count) {
        imageAdapter.setCount(count);
    }

//    public void setAddPicButtonOnClickListener(View.OnClickListener listener) {
//        addPicButton.setOnClickListener(listener);
//    }

    public void setStreetAddressText(String text) {
        streetAddressTextView.setText(text);
    }

    public void setHouseAddressText(String text) {
        houseAddressTextView.setText(text);
    }

    public void setSignInfoText(String text) {
        signInfoTextView.setText(text);
    }

    public void setShopInfoText(String text) {
        shopInfoTextView.setText(text);
    }

    public void addToAreaTypeSpinner(Object id, Object data) {
        areaTypeSpinner.addSpinnerData(id, data);
    }

    public void setAreaTypeSelection(Object id) {
        areaTypeSpinner.setSpinnerSelection(id);
    }

    public Object getSelectedAreaType() {
        return areaTypeSpinner.getSelectedData();
    }

    public void setApplyButtonOnClickListener(View.OnClickListener listener) {
        applyButton.setOnClickListener(listener);
    }



}
