package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.mjict.signboardsurvey.handler.AddressSearchActivityHandler;
import com.mjict.signboardsurvey.handler.SummaryActivityHandler;
import com.mjict.signboardsurvey.sframework.SActivityHandler;

/**
 * Created by Junseo on 2017-02-07.
 */
public class ModeSelector extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean expertMode = sharedPreferences.getBoolean("expert_mode", false);

        if(expertMode)
            goToAddressSearch();
        else
            goToSummary();
    }

    private void goToSummary() {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra(SActivityHandler.HANDLER_CLASS, SummaryActivityHandler.class);
        this.startActivity(intent);
        this.finish();
    }

    private void goToAddressSearch() {
        Intent intent = new Intent(this, AddressSearchActivity.class);
        intent.putExtra(SActivityHandler.HANDLER_CLASS, AddressSearchActivityHandler.class);
        this.startActivity(intent);
        this.finish();
    }


}
