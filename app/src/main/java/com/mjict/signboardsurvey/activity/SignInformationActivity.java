package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.view.View;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.handler.BasicSignInformationInputActivityHandler;
import com.mjict.signboardsurvey.sframework.SActivityHandler;

/**
 * Created by Junseo on 2016-11-17.
 */
public class SignInformationActivity extends SABaseActivity {

    private View signImageCardView;
    private View contentCardView;
    private View placedLocationCardView;
    private View buildingLocationCardView;
    private View signTypeCardView;
    private View sizeCardView;
    private View lightTypeCardView;
    private View signStatusCardView;

//    private View nextButton;



    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_information;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_detail_information);

        signImageCardView = this.findViewById(R.id.sign_image_card_view);
        contentCardView = this.findViewById(R.id.content_card_view);
        placedLocationCardView = this.findViewById(R.id.placed_location_card_view);
        buildingLocationCardView = this.findViewById(R.id.building_location_card_view);
        signTypeCardView = this.findViewById(R.id.sign_type_card_view);
        sizeCardView = this.findViewById(R.id.size_card_view);
        lightTypeCardView = this.findViewById(R.id.light_type_card_view);
        signStatusCardView = this.findViewById(R.id.status_card_view);

        signImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInformationActivity.this, BasicSignInformationInputActivity.class);
                intent.putExtra(SActivityHandler.HANDLER_CLASS, BasicSignInformationInputActivityHandler.class);
                startActivity(intent);
            }
        });

        contentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        placedLocationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buildingLocationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signTypeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sizeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lightTypeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signStatusCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
