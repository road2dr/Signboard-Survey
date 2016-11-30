package com.mjict.signboardsurvey.activity;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-29.
 */
public class SplashActivity extends SABaseActivity {

    // TODO 나중에 화면에 progressbar 를 추가 하고 진행사항을 보여주는 것도 고려해 보자.

    @Override
    protected void init() {
        super.init();
        this.disableNavigation();
        this.hideToolBar();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_splash;
    }
}
