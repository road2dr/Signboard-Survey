package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.LocationInformation;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

/**
 * Created by Junseo on 2016-07-27.
 */
public class MJMapAdapter implements CalloutBalloonAdapter {
    private final View mCalloutBalloon;

    public MJMapAdapter(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCalloutBalloon = inflater.inflate(R.layout.map_marker_building, null);
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        if((mapPOIItem instanceof LocationInformation) == false)
            return null;

        LocationInformation info = (LocationInformation)mapPOIItem;

        if(info.type == LocationInformation.MY_LOCATION)
            return null;    // 내 위치일 경우 Balloon view 없음

        ImageView buildingImageView = (ImageView)mCalloutBalloon.findViewById(R.id.building_image_view);
        TextView addressTextView = (TextView)mCalloutBalloon.findViewById(R.id.address_text_view);
        TextView buildingNameTextView = (TextView)mCalloutBalloon.findViewById(R.id.building_name_text_view);
        TextView signInfoTextView = (TextView)mCalloutBalloon.findViewById(R.id.sign_info_text_view);

        addressTextView.setText(info.address);
        buildingNameTextView.setText(info.name);
        signInfoTextView.setText(info.information);

        if(info.image != null)
            buildingImageView.setImageBitmap(info.image);
        else
            buildingImageView.setImageResource(R.drawable.ic_building);

        mCalloutBalloon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("junseo", "mCalloutBalloon - onTouch");
                return true;
            }
        });

        mCalloutBalloon.setClickable(false);


        return mCalloutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }
}
