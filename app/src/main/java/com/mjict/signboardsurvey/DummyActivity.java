package com.mjict.signboardsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mjict.signboardsurvey.activity.SplashActivity;
import com.mjict.signboardsurvey.handler.SplashActivityHandler;
import com.mjict.signboardsurvey.sframework.SActivityHandler;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra(SActivityHandler.HANDLER_CLASS, SplashActivityHandler.class);
        this.startActivity(intent);

        this.finish();
    }
}
