package com.mjict.signboardsurvey.activity;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ImageViewPagerAdapter;
import com.mjict.signboardsurvey.widget.CircleIndicator;

/**
 * Created by Junseo on 2016-11-15.
 */
public class BuildingProfileActivity extends SABaseActivity {

    private TextView titleTextView;
    private ViewPager imagePager;
    private CircleIndicator imageIndicator;
    private TextView streetAddressTextView;
    private TextView houseAddressTextView;
    private TextView shopInfoTextView;
    private TextView signInfoTextView;
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
        streetAddressTextView = (TextView)this.findViewById(R.id.street_address_text_view);
        houseAddressTextView = (TextView)this.findViewById(R.id.house_address_text_view);
        shopInfoTextView = (TextView)this.findViewById(R.id.shop_info_text_view);
        signInfoTextView = (TextView) this.findViewById(R.id.sign_info_text_view);

        imageAdapter = new ImageViewPagerAdapter(this);
        imagePager.setAdapter(imageAdapter);
        imageIndicator.setViewPager(imagePager);
        imageAdapter.registerDataSetObserver(imageIndicator.getDataSetObserver());

        // test
        imageAdapter.addImage(R.drawable.test_building1);
        imageAdapter.addImage(R.drawable.test_building2);
        imageAdapter.addImage(R.drawable.test_building3);
        imageAdapter.addImage(R.drawable.ic_building);
        imageAdapter.addImage(R.drawable.test_building4);

//        (R.drawable.test_building1, "인천광역시 남동구 구월동 구월남로 94", "안천광역시 남동구 구월동 488-21", "동신 빌딩", "94")
        titleTextView.setText("동산 빌딩");
        streetAddressTextView.setText("인천광역시 남동구 구월동 구월남로 94");
        houseAddressTextView.setText("안천광역시 남동구 구월동 488-21");
        shopInfoTextView.setText("12 건");
        signInfoTextView.setText("23 건");
    }
}
