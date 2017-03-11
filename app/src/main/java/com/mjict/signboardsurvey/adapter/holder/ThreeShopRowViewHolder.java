package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-12-07.
 */
public class ThreeShopRowViewHolder {
    private View parent;

    private View firstLayout;
    private TextView firstNameTextView;
    private TextView firstCategoryTextView;
    private TextView firstPhoneTextView;
    private View firstClosedView;
    private View firstPermissionView;
    private View secondLayout;
    private TextView secondNameTextView;
    private TextView secondCategoryTextView;
    private TextView secondPhoneTextView;
    private View secondClosedView;
    private View secondPermissionView;
    private View thirdLayout;
    private TextView thirdNameTextView;
    private TextView thirdCategoryTextView;
    private TextView thirdPhoneTextView;
    private View thirdClosedView;
    private View thirdPermissionView;

    public ThreeShopRowViewHolder(View v) {
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

    public View getThirdLayout() {
        if(thirdLayout == null)
            thirdLayout = parent.findViewById(R.id.third_layout);

        return thirdLayout;
    }

    public TextView getThirdNameTextView() {
        if(thirdNameTextView == null)
            thirdNameTextView = (TextView)parent.findViewById(R.id.third_name_text_view);

        return thirdNameTextView;
    }

    public TextView getThirdCategoryTextView() {
        if(thirdCategoryTextView == null)
            thirdCategoryTextView = (TextView)parent.findViewById(R.id.third_category_text_view);

        return thirdCategoryTextView;
    }

    public TextView getThirdPhoneTextView() {
        if(thirdPhoneTextView == null)
            thirdPhoneTextView = (TextView)parent.findViewById(R.id.third_phone_text_view);

        return thirdPhoneTextView;
    }

    public View getFirstClosedView() {
        if(firstClosedView == null)
            firstClosedView = parent.findViewById(R.id.first_closed_view);

        return firstClosedView;
    }

    public View getSecondClosedView() {
        if(secondClosedView == null)
            secondClosedView = parent.findViewById(R.id.second_closed_view);

        return secondClosedView;
    }

    public View getThirdClosedView() {
        if(thirdClosedView == null)
            thirdClosedView = parent.findViewById(R.id.third_closed_view);

        return thirdClosedView;
    }

    public View getFirstPermissionView() {
        if(firstPermissionView == null)
            firstPermissionView = parent.findViewById(R.id.first_permission_view);

        return firstPermissionView;
    }

    public View getSecondPermissionView() {
        if(secondPermissionView == null)
            secondPermissionView = parent.findViewById(R.id.second_permission_view);

        return secondPermissionView;
    }

    public View getThirdPermissionView() {
        if(thirdPermissionView == null)
            thirdPermissionView = parent.findViewById(R.id.third_permission_view);

        return thirdPermissionView;
    }
}
