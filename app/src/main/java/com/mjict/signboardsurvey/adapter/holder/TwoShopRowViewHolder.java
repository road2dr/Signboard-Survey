package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-12-07.
 */
public class TwoShopRowViewHolder {
    private View parent;
    private View firstLayout;
    private TextView firstNameTextView;
    private TextView firstCategoryTextView;
    private TextView firstPhoneTextView;
    private View secondLayout;
    private TextView secondNameTextView;
    private TextView secondCategoryTextView;
    private TextView secondPhoneTextView;

    public TwoShopRowViewHolder(View v) {
        parent = v;
    }

    public View getFirstLayout() {
        if(firstLayout == null)
            firstLayout = parent.findViewById(R.id.first_layout);

        return firstLayout;
    }

    public TextView getFirstNameTextView() {
        if(firstNameTextView == null)
            firstNameTextView = (TextView)parent.findViewById(R.id.first_name_text_view);

        return firstNameTextView;
    }

    public TextView getFirstCategoryTextView() {
        if(firstCategoryTextView == null)
            firstCategoryTextView = (TextView)parent.findViewById(R.id.first_category_text_view);

        return firstCategoryTextView;
    }

    public TextView getFirstPhoneTextView() {
        if(firstPhoneTextView == null)
            firstPhoneTextView = (TextView)parent.findViewById(R.id.first_phone_text_view);

        return firstPhoneTextView;
    }

    public View getSecondLayout() {
        if(secondLayout == null)
            secondLayout = parent.findViewById(R.id.second_layout);

        return secondLayout;
    }

    public TextView getSecondNameTextView() {
        if(secondNameTextView == null)
            secondNameTextView = (TextView)parent.findViewById(R.id.second_name_text_view);

        return secondNameTextView;
    }

    public TextView getSecondCategoryTextView() {
        if(secondCategoryTextView == null)
            secondCategoryTextView = (TextView)parent.findViewById(R.id.second_category_text_view);

        return secondCategoryTextView;
    }

    public TextView getSecondPhoneTextView() {
        if(secondPhoneTextView == null)
            secondPhoneTextView = (TextView)parent.findViewById(R.id.second_phone_text_view);

        return secondPhoneTextView;
    }
}
