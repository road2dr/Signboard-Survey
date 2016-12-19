package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-12-06.
 */
public class TwoBuildingRowViewHolder {

    private View parent;
    private View firstLayout;
    private ImageView firstImageView;
    private TextView firstTitleTextView;
    private TextView firstStreetAddressTextView;
    private TextView firstHouseAddressTextView;
    private View secondLayout;
    private ImageView secondImageView;
    private TextView secondTitleTextView;
    private TextView secondStreetAddressTextView;
    private TextView secondHouseAddressTextView;

    public TwoBuildingRowViewHolder(View v) {
        parent = v;
    }

    public View getFirstLayout() {
        if(firstLayout == null)
            firstLayout = parent.findViewById(R.id.first_layout);

        return firstLayout;
    }

    public ImageView getFirstImageView() {
        if(firstImageView == null)
            firstImageView = (ImageView)parent.findViewById(R.id.first_image_view);

        return firstImageView;
    }

    public TextView getFirstTitleTextView() {
        if(firstTitleTextView == null)
            firstTitleTextView = (TextView)parent.findViewById(R.id.first_title_text_view);

        return firstTitleTextView;
    }

    public View getSecondLayout() {
        if(secondLayout == null)
            secondLayout = parent.findViewById(R.id.second_layout);

        return secondLayout;
    }

    public ImageView getSecondImageView() {
        if(secondImageView == null)
            secondImageView = (ImageView) parent.findViewById(R.id.second_image_view);

        return secondImageView;
    }

    public TextView getSecondTitleTextView() {
        if(secondTitleTextView == null)
            secondTitleTextView = (TextView)parent.findViewById(R.id.second_title_text_view);

        return secondTitleTextView;
    }

    public TextView getFirstStreetAddressTextView() {
        if(firstStreetAddressTextView == null)
            firstStreetAddressTextView = (TextView)parent.findViewById(R.id.first_street_address_text_view);

        return firstStreetAddressTextView;
    }

    public TextView getFirstHouseAddressTextView() {
        if(firstHouseAddressTextView == null)
            firstHouseAddressTextView = (TextView)parent.findViewById(R.id.first_house_address_text_view);

        return firstHouseAddressTextView;
    }

    public TextView getSecondStreetAddressTextView() {
        if(secondStreetAddressTextView == null)
            secondStreetAddressTextView = (TextView)parent.findViewById(R.id.second_street_address_text_view);

        return secondStreetAddressTextView;
    }

    public TextView getSecondHouseAddressTextView() {
        if(secondHouseAddressTextView == null)
            secondHouseAddressTextView = (TextView)parent.findViewById(R.id.second_house_address_text_view);

        return secondHouseAddressTextView;
    }
}
