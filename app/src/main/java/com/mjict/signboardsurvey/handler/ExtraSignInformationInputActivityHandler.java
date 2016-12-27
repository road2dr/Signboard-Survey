package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.ExtraSignInformationInputActivity;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.util.WrongNumberFormatException;
import com.mjict.signboardsurvey.util.WrongNumberScopeException;

/**
 * Created by Junseo on 2016-11-18.
 */
public class ExtraSignInformationInputActivityHandler extends SABaseActivityHandler {
    private static final int MAX_INPUT_VALUE = 999;

    private ExtraSignInformationInputActivity activity;
    private Sign currentSign;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        // init
        activity = (ExtraSignInformationInputActivity)getActivity();
        Intent intent = activity.getIntent();
        currentSign = (Sign)intent.getSerializableExtra(MJConstants.SIGN);
        if(currentSign == null) {
            // TODO 에러
            activity.setResult(Activity.RESULT_CANCELED);
            activity.finish();
            return;
        }

        // register listener
        activity.setBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
            }
        });

        activity.setNextButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonClicked();
            }
        });

        // do first job
        updateToUI();
    }

    private void nextButtonClicked() {
        String placedSide = activity.getFrontChecked() ? "N" : "Y"; // TODO 이거 값이 맞는 건지 나중에 확인 해봐야함
        String intersection = activity.getIntersectionChecked() ? "Y" : "N";
        boolean frontBack = activity.getFrontBackChecked();
        String placedFloorText = activity.getInputPlacedFloor();
        String totalFloorText = activity.getInputTotalFloor();
        String collisionWidthText = activity.getInputCollisionWidth();
        String collisionLengthText = activity.getInputCollisionLength();

        try {
            checkNumberValue(placedFloorText, totalFloorText, collisionWidthText, collisionLengthText);
        } catch (WrongNumberFormatException e) {
            String cause = activity.getString(R.string.wrong_number_type_input, e.getCauseValue());
            Toast.makeText(activity, cause, Toast.LENGTH_SHORT).show();
            return;
        } catch (WrongNumberScopeException e) {
            Toast.makeText(activity, R.string.wrong_number_scope_input, Toast.LENGTH_SHORT).show();
            return;
        }

        currentSign.setPlacedSide(placedSide);
        currentSign.setIsIntersection(intersection);
        currentSign.setFrontBackRoad(frontBack);
        currentSign.setPlacedFloor(Integer.parseInt(placedFloorText));
        currentSign.setTotalFloor(Integer.parseInt(totalFloorText));
        currentSign.setCollisionWidth(Float.parseFloat(collisionWidthText));
        currentSign.setCollisionLength(Float.parseFloat(collisionLengthText));

        Intent responseIntent = new Intent();
        responseIntent.putExtra(MJConstants.SIGN, currentSign);
        activity.setResult(Activity.RESULT_OK, responseIntent);
        activity.finish();
    }

    private void updateToUI() {
        boolean isFront = currentSign.getPlacedSide().equals("Y") ? false : true;   // TODO 이거 값이 맞는 건지 나중에 확인 해봐야함
        boolean isIntersection = currentSign.getIsIntersection().equals("Y") ? true : false;
        boolean isFrontBack = currentSign.isFrontBackRoad();
        int placedFloor = currentSign.getPlacedFloor();
        int totalFloor = currentSign.getTotalFloor();
        float collisionWidth = currentSign.getCollisionWidth();
        float collisionLength = currentSign.getCollisionLength();

        activity.setFrontChecked(isFront);
        activity.setIntersectionChecked(isIntersection);
        activity.setFrontBackChecked(isFrontBack);
        activity.setPlacedFloorText(String.valueOf(placedFloor));
        activity.setTotalFloorText(String.valueOf(totalFloor));
        activity.setCollisionWidthText(String.format("%.2f", collisionWidth));
        activity.setCollisionLengthText(String.format("%.2f", collisionLength));
    }


    private void checkNumberValue(String placedFloorText, String totalFloorText,
                                  String collisionWidthText, String collisionLengthText)
            throws WrongNumberFormatException, WrongNumberScopeException {
        int placedFloor = 0;
        int totalFloor = 0;
        float collisionWidth = 0;
        float collisionLength = 0;

        if(placedFloorText.equals("") == false)
            placedFloor = Utilities.stringToInt(placedFloorText);
        if(totalFloorText.equals("") == false)
            totalFloor = Utilities.stringToInt(totalFloorText);
        if(collisionWidthText.equals("") == false)
            collisionWidth = Utilities.stringToFloat(collisionWidthText);
        if(collisionLengthText.equals("") == false)
            collisionLength = Utilities.stringToFloat(collisionLengthText);

        if(placedFloor < 0 || totalFloor < 0 || collisionWidth < 0 || collisionLength < 0)
            throw new WrongNumberScopeException(-1);

        if(placedFloor > MAX_INPUT_VALUE || totalFloor > MAX_INPUT_VALUE || collisionWidth > MAX_INPUT_VALUE || collisionLength > MAX_INPUT_VALUE)
            throw new WrongNumberScopeException(-1);

    }

}
