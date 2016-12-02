package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-30.
 */
public class RecentSignViewHolder {
    private View parent;
    private ImageView imageView;
    private TextView contentTextView;
    private TextView typeTextView;
    private TextView resultTextView;

    public RecentSignViewHolder(View view) {
        parent = view;
    }

    public ImageView getImageView() {
        if(imageView == null)
            imageView = (ImageView)parent.findViewById(R.id.image_view);

        return imageView;
    }

    public TextView getContentTextView() {
        if(contentTextView == null)
            contentTextView = (TextView)parent.findViewById(R.id.content_text_view);

        return contentTextView;
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
}
