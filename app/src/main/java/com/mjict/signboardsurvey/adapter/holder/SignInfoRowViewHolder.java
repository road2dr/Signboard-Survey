package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-12-16.
 */
public class SignInfoRowViewHolder {
    private View parent;
    private ImageView imageView;
    private TextView nameTextView;
    private TextView sizeTextView;
    private TextView resultTextView;

    public SignInfoRowViewHolder(View view) {
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

    public TextView getSizeTextView() {
        if(sizeTextView == null)
            sizeTextView = (TextView)parent.findViewById(R.id.name_text_view);

        return sizeTextView;
    }

    public TextView getResultTextView() {
        if(resultTextView == null)
            resultTextView = (TextView)parent.findViewById(R.id.result_text_view);

        return resultTextView;
    }
}
