package com.mjict.signboardsurvey.activity;

import android.support.v4.view.ViewPager;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-23.
 */
public class SignInformationActivity2 extends SABaseActivity {

    private ViewPager signInformationPager;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_information2;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_detail_information);
    }
}
