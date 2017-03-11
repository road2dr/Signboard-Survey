package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.LabelImageView;


/**
 * Created by Junseo on 2016-09-08.
 */
public class ReviewSignListRowHolder {
    private View parent;
    private LabelImageView signImageView;
    private TextView reviewTextView;
    private View permitView;
    private TextView contentTextView;
    private TextView shopNameTextView;
    private TextView addressTextView;
    private TextView resultTextView;
    private TextView sizeTextView;

    public ReviewSignListRowHolder(View view) {
        parent = view;
    }


    public View getPermitView() {
        if(permitView == null)
            permitView = parent.findViewById(R.id.permit_view);

        return permitView;
    }

    public TextView getReviewTextView() {
        if(reviewTextView == null)
            reviewTextView = (TextView)parent.findViewById(R.id.review_text_view);

        return reviewTextView;
    }
    public LabelImageView getSignImageView() {
        if(signImageView == null)
            signImageView = (LabelImageView)parent.findViewById(R.id.image_view);

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

//    public TextView getAddressTextView() {
//        if(addressTextView == null)
//            addressTextView = (TextView)parent.findViewById(R.id.address_text_view);
//
//        return addressTextView;
//    }

//    public TextView getTypeTextView() {
//        if(typeTextView == null)
//            typeTextView = (TextView)parent.findViewById(R.id.type_text_view);
//
//        return typeTextView;
//    }

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

    public TextView getAddressTextView() {
        if(addressTextView == null)
            addressTextView = (TextView)parent.findViewById(R.id.address_text_view);//

        return addressTextView;
    }

}
