package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Spinner;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ImageSpinnerAdapter;
import com.mjict.signboardsurvey.handler.ExtraSignInformationInputActivityHandler;
import com.mjict.signboardsurvey.model.IconItem;
import com.mjict.signboardsurvey.sframework.SActivityHandler;

/**
 * Created by Junseo on 2016-11-17.
 */
public class BasicSignInformationInputActivity extends SABaseActivity {

    private View nextButton;
    private Spinner lightSpinner;
    private ImageSpinnerAdapter spinnerAdapter;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_basic_information;
    }

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();

        nextButton = this.findViewById(R.id.next_button);

        lightSpinner = (Spinner)this.findViewById(R.id.light_type_spinner);
        spinnerAdapter = new ImageSpinnerAdapter(this);
        lightSpinner.setAdapter(spinnerAdapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicSignInformationInputActivity.this, ExtraSignInformationInputActivity.class);
                intent.putExtra(SActivityHandler.HANDLER_CLASS, ExtraSignInformationInputActivityHandler.class);
                startActivity(intent);
            }
        });

        // test
        spinnerAdapter.add(new IconItem(R.drawable.img_lgith1, "LED"));
        spinnerAdapter.add(new IconItem(R.drawable.img_light2, "일반조명"));
        spinnerAdapter.add(new IconItem(R.drawable.img_light3, "네온사인"));

    }
}
