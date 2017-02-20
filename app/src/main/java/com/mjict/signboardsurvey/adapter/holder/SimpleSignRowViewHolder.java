package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2017-02-07.
 */
public class SimpleSignRowViewHolder {
    private View parent;

    private ImageView signImageView;
    private TextView typeTextView;
    private TextView sizeTextView;
    private TextView locationTextView;
    private TextView lightTextView;
    private TextView resultTextView;
    private ImageButton modifyButton;

    public SimpleSignRowViewHolder(View v) {
        parent = v;
    }

    public ImageView getSignImageView() {
        if(signImageView == null)
            signImageView = (ImageView)parent.findViewById(R.id.sign_image_view);

        return signImageView;
    }

    public TextView getTypeTextView() {
        if(typeTextView == null)
            typeTextView = (TextView)parent.findViewById(R.id.type_text_view);

        return typeTextView;
    }

    public TextView getSizeTextView() {
        if(sizeTextView == null)
            sizeTextView = (TextView)parent.findViewById(R.id.size_text_view);

        return sizeTextView;
    }

    public TextView getLocationTextView() {
        if(locationTextView == null)
            locationTextView = (TextView)parent.findViewById(R.id.location_text_view);

        return locationTextView;
    }

    public TextView getLightTextView() {
        if(lightTextView == null)
            lightTextView = (TextView)parent.findViewById(R.id.light_text_view);

        return lightTextView;
    }

    public TextView getResultTextView() {
        if(resultTextView == null)
            resultTextView = (TextView)parent.findViewById(R.id.result_text_view);

        return resultTextView;
    }

    public ImageButton getModifyButton() {
        if(modifyButton == null)
            modifyButton = (ImageButton)parent.findViewById(R.id.modify_button);

        return modifyButton;
    }
}
