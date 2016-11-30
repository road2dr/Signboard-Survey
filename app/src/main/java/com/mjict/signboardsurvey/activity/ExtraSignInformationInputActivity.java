package com.mjict.signboardsurvey.activity;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-18.
 */
public class ExtraSignInformationInputActivity extends SABaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_extra_information;
    }

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();
    }
}
