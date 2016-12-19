package com.mjict.signboardsurvey.activity;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.ui.BuildingResult;

/**
 * Created by Junseo on 2016-11-10.
 */
public class SearchResultActivity extends SABaseActivity {

    private LinearLayout addressResultLayout;
    private TextView addressResultLabel;
    private TextView addressResultMoreButton;

    private LinearLayout buildingResultLayout;
    private TextView buildingResultLabel;
    private TextView buildingResultMoreButton;

    private LinearLayout shopResultLayout;
    private TextView shopResultLabel;
    private TextView shopResultMoreButton;

    private SearchResultItemOnClickListener searchResultItemOnClickListener;

    LayoutInflater inflater;

    @Override
    protected void init() {
        super.init();
        this.showToolBar();
        this.setTitle(R.string.search_result);

        inflater = LayoutInflater.from(this);

        //
        addressResultLayout = (LinearLayout)this.findViewById(R.id.address_result_layout);
        addressResultLabel = (TextView)this.findViewById(R.id.address_result_label);
        addressResultMoreButton = (TextView)this.findViewById(R.id.address_result_more_button);
        buildingResultLayout = (LinearLayout)this.findViewById(R.id.building_result_layout);
        buildingResultLabel = (TextView)this.findViewById(R.id.building_result_label);
        buildingResultMoreButton = (TextView)this.findViewById(R.id.building_result_more_button);
        shopResultLayout = (LinearLayout)this.findViewById(R.id.shop_result_layout);
        shopResultLabel = (TextView)this.findViewById(R.id.shop_result_label);
        shopResultMoreButton = (TextView)this.findViewById(R.id.shop_result_more_button);
    }

    public void addAddressResult(String result, int index, int length) {
        TextView tv = (TextView)inflater.inflate(R.layout.tv_search_result_address, null);
        SpannableStringBuilder ssb = new SpannableStringBuilder(result);
        ssb.setSpan(new AbsoluteSizeSpan(40), index, index+length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb);
        tv.setTag(addressResultLayout.getChildCount());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        if(addressResultLayout.getChildCount() > 0) {
            params.setMargins(0, 5, 0, 0);
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer)v.getTag();
                if(searchResultItemOnClickListener != null)
                    searchResultItemOnClickListener.onAddressItemClicked(index);
            }
        });
        addressResultLayout.addView(tv, params);
    }

    public void addBuildingResult(BuildingResult result) {
        View view = inflater.inflate(R.layout.list_row_search_result_building, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        TextView nameTextView = (TextView)view.findViewById(R.id.name_text_view);
        TextView addressTextView = (TextView)view.findViewById(R.id.address_text_view);
        TextView shopCountTextView = (TextView)view.findViewById(R.id.shop_count_text_view);
        TextView signCountTextView = (TextView)view.findViewById(R.id.sign_count_text_view);

        String shopCountText = getString(R.string.number_of_case, result.shopCount);
        String signCountText = getString(R.string.number_of_case, result.signCount);

        imageView.setImageBitmap(result.image);
        nameTextView.setText(result.name);
        addressTextView.setText(result.streetAddress);
        shopCountTextView.setText(shopCountText);
        signCountTextView.setText(signCountText);

        view.setTag(buildingResultLayout.getChildCount());

        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        if(buildingResultLayout.getChildCount() > 0) {
            params.setMargins(0, 5, 0, 0);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer)v.getTag();
                if(searchResultItemOnClickListener != null)
                    searchResultItemOnClickListener.onBuildingItemClicked(index);
            }
        });
        buildingResultLayout.addView(view, params);
    }

    public void addShopResult(String result, int index, int length) {
        TextView tv = (TextView)inflater.inflate(R.layout.tv_search_result_address, null);
        SpannableStringBuilder ssb = new SpannableStringBuilder(result);
        ssb.setSpan(new AbsoluteSizeSpan(40), index, index+length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb);
        tv.setTag(shopResultLayout.getChildCount());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        if(shopResultLayout.getChildCount() > 0) {
            params.setMargins(0, 5, 0, 0);
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer)v.getTag();
                if(searchResultItemOnClickListener != null)
                    searchResultItemOnClickListener.onShopItemClicked(index);
            }
        });
        shopResultLayout.addView(tv, params);
    }

    public void setAddressResultVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        addressResultLabel.setVisibility(visibility);
        addressResultLayout.setVisibility(visibility);
        addressResultMoreButton.setVisibility(visibility);
    }

    public void setBuildingResultVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        buildingResultLabel.setVisibility(visibility);
        buildingResultLayout.setVisibility(visibility);
        buildingResultMoreButton.setVisibility(visibility);
    }

    public void setShopResultVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        shopResultLabel.setVisibility(visibility);
        shopResultLayout.setVisibility(visibility);
        shopResultMoreButton.setVisibility(visibility);
    }

    public void setSearchResultItemOnClickListener(SearchResultItemOnClickListener listener) {
        searchResultItemOnClickListener = listener;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_result;
    }


    public static interface SearchResultItemOnClickListener {
        public void onAddressItemClicked(int index);
        public void onBuildingItemClicked(int index);
        public void onShopItemClicked(int index);
    }
}
