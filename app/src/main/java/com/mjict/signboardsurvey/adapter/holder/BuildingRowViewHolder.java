package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-12-06.
 */
public class BuildingRowViewHolder {
    private View parent;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView streetAddressTextView;
    private TextView houseAddressTextView;

    public BuildingRowViewHolder(View v) {
        parent = v;
    }

    public ImageView getImageView() {
        if(imageView == null)
            imageView = (ImageView)parent.findViewById(R.id.image_view);

        return imageView;
    }

    public TextView getTitleTextView() {
        if(titleTextView == null)
            titleTextView = (TextView)parent.findViewById(R.id.title_text_view);

        return titleTextView;
    }

    public TextView getStreetAddressTextView() {
        if(streetAddressTextView == null)
            streetAddressTextView = (TextView)parent.findViewById(R.id.street_address_text_view);

        return streetAddressTextView;
    }

    public TextView getHouseAddressTextView() {
        if(houseAddressTextView == null)
            houseAddressTextView = (TextView)parent.findViewById(R.id.house_address_text_view);

        return houseAddressTextView;
    }
}
