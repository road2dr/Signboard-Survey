package com.mjict.signboardsurvey.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-18.
 */
public class ExtraSignInformationInputActivity extends SABaseActivity {
    private CheckBox frontCheckBox;
    private CheckBox intersectionCheckBox;
    private CheckBox frontBackCheckBox;
    private EditText placedFloorEditText;
    private EditText totalFloorEditText;
    private EditText collisionWidthEditText;
    private EditText collisionLengthEditText;

    private View backButton;
    private View nextButton;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_extra_information;
    }

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();

        frontCheckBox = (CheckBox)this.findViewById(R.id.front_check_box);
        intersectionCheckBox = (CheckBox)this.findViewById(R.id.intersection_check_box);
        frontBackCheckBox = (CheckBox)this.findViewById(R.id.front_back_check_box);
        placedFloorEditText = (EditText)this.findViewById(R.id.placed_floor_eidt_text);
        totalFloorEditText = (EditText)this.findViewById(R.id.total_floor_edit_text);
        collisionWidthEditText = (EditText)this.findViewById(R.id.collision_width_edit_text);
        collisionLengthEditText = (EditText)this.findViewById(R.id.collision_length_edit_text);
        backButton = this.findViewById(R.id.back_button);
        nextButton = this.findViewById(R.id.next_button);
    }

    public void setFrontChecked(boolean checked) {
        frontCheckBox.setChecked(checked);
    }

    public boolean getFrontChecked() {
        return frontCheckBox.isChecked();
    }

    public void setIntersectionChecked(boolean checked) {
        intersectionCheckBox.setChecked(checked);
    }

    public boolean getIntersectionChecked() {
        return intersectionCheckBox.isChecked();
    }

    public void setFrontBackChecked(boolean checked) {
        frontBackCheckBox.setChecked(checked);
    }

    public boolean getFrontBackChecked() {
        return frontBackCheckBox.isChecked();
    }

    public void setPlacedFloorText(String text) {
        placedFloorEditText.setText(text);
    }

    public String getInputPlacedFloor() {
        return placedFloorEditText.getText().toString();
    }

    public void setTotalFloorText(String text) {
        totalFloorEditText.setText(text);
    }

    public String getInputTotalFloor() {
        return totalFloorEditText.getText().toString();
    }

    public void setCollisionWidthText(String text) {
        collisionWidthEditText.setText(text);
    }

    public String getInputCollisionWidth() {
        return collisionWidthEditText.getText().toString();
    }

    public void setCollisionLengthText(String text) {
        collisionLengthEditText.setText(text);
    }

    public String getInputCollisionLength() {
        return collisionLengthEditText.getText().toString();
    }

    public void setBackButtonOnClickListener(View.OnClickListener listener) {
        backButton.setOnClickListener(listener);
    }

    public void setNextButtonOnClickListener(View.OnClickListener listener) {
        nextButton.setOnClickListener(listener);
    }
}
