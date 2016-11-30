package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-30.
 */
public class RecentBuildingViewHolder {

    private View parent;
    private ImageView imageView;
    private TextView nameTextView;
    private TextView addressTextView;

    public RecentBuildingViewHolder(View view) {
        parent = view;
    }

    public ImageView getImageView() {
        if(imageView == null)
            imageView = (ImageView)parent.findViewById(R.id.image_view);

        return imageView;
    }

    public TextView getNameTextView() {
        if(nameTextView == null)
            nameTextView = (TextView)parent.findViewById(R.id.name_text_view);

        return nameTextView;
    }

    public TextView getAddressTextView() {
        if(addressTextView == null)
            addressTextView = (TextView)parent.findViewById(R.id.address_text_view);

        return addressTextView;
    }
}
