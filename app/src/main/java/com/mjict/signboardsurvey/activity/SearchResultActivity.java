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

    private View addressResultView;
    private LinearLayout addressResultLayout;
    private TextView addressResultMoreButton;

    private View buildingResultView;
    private LinearLayout buildingResultLayout;
    private TextView buildingResultMoreButton;

    private View shopResultView;
    private LinearLayout shopResultLayout;
    private TextView shopResultMoreButton;

    private TextView noResultTextView;

    private SearchResultItemOnClickListener searchResultItemOnClickListener;

    LayoutInflater inflater;

    @Override
    protected void init() {
        super.init();
        this.showToolBar();
        this.setTitle(R.string.search_result);

        inflater = LayoutInflater.from(this);

        //
        addressResultView = this.findViewById(R.id.address_result_view);
        addressResultLayout = (LinearLayout)this.findViewById(R.id.address_result_layout);
        addressResultMoreButton = (TextView)this.findViewById(R.id.address_result_more_button);

        buildingResultView = this.findViewById(R.id.building_result_view);
        buildingResultLayout = (LinearLayout)this.findViewById(R.id.building_result_layout);
        buildingResultMoreButton = (TextView)this.findViewById(R.id.building_result_more_button);

        shopResultView = this.findViewById(R.id.shop_result_view);
        shopResultLayout = (LinearLayout)this.findViewById(R.id.shop_result_layout);
        shopResultMoreButton = (TextView)this.findViewById(R.id.shop_result_more_button);
        noResultTextView = (TextView)this.findViewById(R.id.no_result_text_view);
    }

    public void addAddressResult(String result, int index, int length) {
        TextView tv = (TextView)inflater.inflate(R.layout.tv_search_result_address, null);
        SpannableStringBuilder ssb = new SpannableStringBuilder(result);
        ssb.setSpan(new AbsoluteSizeSpan(70), index, index+length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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

//        String shopCountText = getString(R.string.number_of_case, result.shopCount);
//        String signCountText = getString(R.string.number_of_case, result.signCount);
        String shopCountText = result.shopCountText;
        String signCountText = result.signCountText;

        if(result.image != null)
            imageView.setImageBitmap(result.image);
        else
            imageView.setImageResource(R.drawable.ic_no_image);
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

    public void addShopResult(String result, int index, int length, String streetAddress) {
        TextView tv = (TextView)inflater.inflate(R.layout.tv_search_result_address, null);
        SpannableStringBuilder ssb = new SpannableStringBuilder(result);
        ssb.setSpan(new AbsoluteSizeSpan(70), index, index+length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append("\n"+streetAddress);
        tv.setText(ssb);
        tv.setTag(shopResultLayout.getChildCount());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    public void clearAddressResult() {
//        int childCount = addressResultLayout.getChildCount();
//        for(int i=0; i<childCount; i++) {
//            View child = addressResultLayout.getChildAt(i);
//            addressResultLayout.removeView(child);
//        }
        addressResultLayout.removeAllViews();
//        addressResultLayout.requestLayout();
    }

    public void clearBuildingResult() {
//        int childCount = buildingResultLayout.getChildCount();
//        for(int i=0; i<childCount; i++) {
//            View child = buildingResultLayout.getChildAt(i);
//            buildingResultLayout.removeView(child);
//        }
//        buildingResultLayout.requestLayout();
        buildingResultLayout.removeAllViews();
    }

    public void clearShopResult() {
//        int childCount = shopResultLayout.getChildCount();
//        for(int i=0; i<childCount; i++) {
//            View child = shopResultLayout.getChildAt(i);
//            shopResultLayout.removeView(child);
//        }
        shopResultLayout.removeAllViews();
    }

    public void setAddressResultVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        addressResultView.setVisibility(visibility);

//        addressResultLabel.setVisibility(visibility);
//        addressResultLayout.setVisibility(visibility);
//        addressResultMoreButton.setVisibility(visibility);
    }

    public void setBuildingResultVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        buildingResultView.setVisibility(visibility);
//        buildingResultLabel.setVisibility(visibility);
//        buildingResultLayout.setVisibility(visibility);
//        buildingResultMoreButton.setVisibility(visibility);
    }

    public void setShopResultVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        shopResultView.setVisibility(visibility);
//        shopResultLabel.setVisibility(visibility);
//        shopResultLayout.setVisibility(visibility);
//        shopResultMoreButton.setVisibility(visibility);
    }

    public void setNoResultTextViewVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        noResultTextView.setVisibility(visibility);
    }

    public void setSearchResultItemOnClickListener(SearchResultItemOnClickListener listener) {
        searchResultItemOnClickListener = listener;
    }

    public void setAddressResultMoreButtonOnClickListener(View.OnClickListener listener) {
        addressResultMoreButton.setOnClickListener(listener);
    }

    public void setBuildingResultMoreButtonOnClickListener(View.OnClickListener listener) {
        buildingResultMoreButton.setOnClickListener(listener);
    }

    public void setShopResultMoreButtonOnClickListener(View.OnClickListener listener) {
        shopResultMoreButton.setOnClickListener(listener);
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
