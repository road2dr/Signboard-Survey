package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.LabelImageView;


/**
 * Created by Junseo on 2016-08-31.
 */
public class DemolishedSignListRowHolder {
    private View parent;
    private LabelImageView signImageView;
    private TextView contentTextView;
    private TextView shopNameTextView;
    private TextView addressTextView;
    private TextView typeTextView;
    private TextView resultTextView;
    private TextView sizeTextView;
    private TextView locationTextView;
    private ImageView demolitionImageView;
    private TextView demolitionDateTextView;

    public DemolishedSignListRowHolder(View view) {
        parent = view;
    }

    public LabelImageView getSignImageView() {
        if(signImageView == null)
            signImageView = (LabelImageView)parent.findViewById(R.id.sign_image_view);

        return signImageView;
    }

    public TextView getContentTextView() {
        if(contentTextView == null)
            contentTextView = (TextView)parent.findViewById(R.id.content_text_view);

        return contentTextView;
    }

    public TextView getShopNameTextView() {
        if(shopNameTextView == null)
            shopNameTextView = (TextView)parent.findViewById(R.id.shop_name_text_view);

        return shopNameTextView;
    }

    public TextView getAddressTextView() {
        if(addressTextView == null)
            addressTextView = (TextView)parent.findViewById(R.id.address_text_view);

        return addressTextView;
    }

    public TextView getTypeTextView() {
        if(typeTextView == null)
            typeTextView = (TextView)parent.findViewById(R.id.type_text_view);

        return typeTextView;
    }

    public TextView getResultTextView() {
        if(resultTextView == null)
            resultTextView = (TextView)parent.findViewById(R.id.result_text_view);

        return resultTextView;
    }

    public TextView getSizeTextView() {
        if(sizeTextView == null)
            sizeTextView = (TextView)parent.findViewById(R.id.size_text_view);

        return sizeTextView;
    }

    public TextView getLocationTextView() {
        if(locationTextView == null)
            locationTextView = (TextView)parent.findViewById(R.id.location_text_view);//

        return locationTextView;
    }

    public ImageView getDemolitionImageView() {
        if(demolitionImageView == null)
            demolitionImageView = (ImageView)parent.findViewById(R.id.demolished_sign_image_view);

        return demolitionImageView;
    }

    public TextView getDemolitionDateTextView() {
        if(demolitionDateTextView == null)
            demolitionDateTextView = (TextView)parent.findViewById(R.id.demolish_date_text_view);

        return demolitionDateTextView;
    }

}
