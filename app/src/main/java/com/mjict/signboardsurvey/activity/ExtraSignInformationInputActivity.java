package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

/**
 * Created by Junseo on 2016-11-18.
 */
public class ExtraSignInformationInputActivity extends SABaseActivity {
    private CheckBox collisionCheckBox;
    private EditText collisionWidthEditText;
    private EditText collisionLengthEditText;
    private SimpleSpinner installedSideSpinner;
    private SimpleSpinner uniquenessSpinner;
    private EditText memoEditText;
    private ImageView demolitionImageView;
    private ImageButton addPicButton;
    private TextView demolishDateTextView;
    private SimpleSpinner resultSpinner;
    private Button autoJudgementButton;

    private View demolitionLabel;
    private View demolitionLayout;

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

        collisionCheckBox = (CheckBox)this.findViewById(R.id.collision_check_box);
        collisionWidthEditText = (EditText)this.findViewById(R.id.collision_width_edit_text);
        collisionLengthEditText = (EditText)this.findViewById(R.id.collision_length_edit_text);
        installedSideSpinner = (SimpleSpinner)this.findViewById(R.id.installed_side_spinner);
        uniquenessSpinner = (SimpleSpinner)this.findViewById(R.id.uniqueness_spinner);
        memoEditText = (EditText)this.findViewById(R.id.memo_edit_text);
        demolitionImageView = (ImageView)this.findViewById(R.id.demolition_image_view);
        addPicButton = (ImageButton)this.findViewById(R.id.add_pic_image_button);
        demolishDateTextView = (TextView)this.findViewById(R.id.demolish_date_text_view);
        resultSpinner = (SimpleSpinner)this.findViewById(R.id.result_spinner);
        autoJudgementButton = (Button)this.findViewById(R.id.auto_judgement_button);

        demolitionLabel = this.findViewById(R.id.demolition_label);
        demolitionLayout = this.findViewById(R.id.demolition_layout);
//        private SimpleSpinner installedSideSpinner;
//        private SimpleSpinner uniquenessSpinner;
//        private EditText memoEditText;
//        private ImageView demolitionImageView;
//        private ImageButton addPicButton;
//        private TextView demolishDateTextView;
//        private SimpleSpinner resultSpinner;


        backButton = this.findViewById(R.id.back_button);
        nextButton = this.findViewById(R.id.next_button);
    }

//    public void setFrontChecked(boolean checked) {
//        frontCheckBox.setChecked(checked);
//    }
//
//    public boolean getFrontChecked() {
//        return frontCheckBox.isChecked();
//    }
//
//    public void setIntersectionChecked(boolean checked) {
//        intersectionCheckBox.setChecked(checked);
//    }
//
//    public boolean getIntersectionChecked() {
//        return intersectionCheckBox.isChecked();
//    }
//
//    public void setFrontBackChecked(boolean checked) {
//        frontBackCheckBox.setChecked(checked);
//    }
//
//    public boolean getFrontBackChecked() {
//        return frontBackCheckBox.isChecked();
//    }
//
//    public void setPlacedFloorText(String text) {
//        placedFloorEditText.setText(text);
//    }
//
//    public String getInputPlacedFloor() {
//        return placedFloorEditText.getText().toString();
//    }
//
//    public void setTotalFloorText(String text) {
//        totalFloorEditText.setText(text);
//    }
//
//    public String getInputTotalFloor() {
//        return totalFloorEditText.getText().toString();
//    }

    public void setCollisionChecked(boolean checked) {
        collisionCheckBox.setChecked(checked);
    }

    public boolean getCollisionChecked() {
        return collisionCheckBox.isChecked();
    }

    public void setCollisionOnCheckedChangListener(CompoundButton.OnCheckedChangeListener listener) {
        collisionCheckBox.setOnCheckedChangeListener(listener);
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

    public void setCollisionWidthEnabled(boolean enabled) {
        collisionWidthEditText.setEnabled(enabled);
    }

    public void setCollisionLengthEnabled(boolean enabled) {
        collisionLengthEditText.setEnabled(enabled);
    }

//    installedSideSpinner = (SimpleSpinner)this.findViewById(R.id.installed_side_spinner);
//    uniquenessSpinner = (SimpleSpinner)this.findViewById(R.id.uniqueness_spinner);
//    memoEditText = (EditText)this.findViewById(R.id.memo_edit_text);

    public void addToInstalledSideSpinner(int id, Object data) {
        installedSideSpinner.addSpinnerData(id, data);
    }

    public void setInstalledSideSpinnerSelection(int id) {
        installedSideSpinner.setSpinnerSelection(id);
    }

    public Object getSelectedInstalledSide() {
        return installedSideSpinner.getSelectedData();
    }

    public void addToUniquenessSpinner(int id, Object data) {
        uniquenessSpinner.addSpinnerData(id, data);
    }

    public void setUniquenessSpinnerSelection(int id) {
        uniquenessSpinner.setSpinnerSelection(id);
    }

    public Object getSelectedUniqueness() {
        return uniquenessSpinner.getSelectedData();
    }

    public void setMemoText(CharSequence text) {
        memoEditText.setText(text);
    }

    public String getInputMemo() {
        return memoEditText.getText().toString();
    }

    //    demolitionImageView = (ImageView)this.findViewById(R.id.demolition_image_view);
//    addPicButton = (ImageButton)this.findViewById(R.id.add_pic_image_button);
//    demolishDateTextView = (TextView)this.findViewById(R.id.demolish_date_text_view);
//    resultSpinner = (SimpleSpinner)this.findViewById(R.id.result_spinner);


    public void setDemolitionPicImage(Bitmap image) {
        demolitionImageView.setImageBitmap(image);
    }

    public void addPicButtonOnClickListener(View.OnClickListener listener) {
        addPicButton.setOnClickListener(listener);
    }

    public void setDemolishDateText(CharSequence text) {
        demolishDateTextView.setText(text);
    }

    public void setDemolishDateTextOnClickListener(View.OnClickListener listener) {
        demolishDateTextView.setOnClickListener(listener);
    }

    public void addToResultSpinner(int id, Object data) {
        resultSpinner.addSpinnerData(id, data);
    }

    public void setResultSpinnerSelection(int id) {
        resultSpinner.setSpinnerSelection(id);
    }

    public Object getSelectedResult() {
        return resultSpinner.getSelectedData();
    }

    public void setBackButtonOnClickListener(View.OnClickListener listener) {
        backButton.setOnClickListener(listener);
    }

    public void setNextButtonOnClickListener(View.OnClickListener listener) {
        nextButton.setOnClickListener(listener);
    }

    public void setDemolitionLayoutVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;

        demolitionLayout.setVisibility(visibility);
        demolitionLabel.setVisibility(visibility);
        demolishDateTextView.setVisibility(visibility);
    }

    public void setAutoJudgementButtonOnClickListener(View.OnClickListener listener) {
        autoJudgementButton.setOnClickListener(listener);
    }
}
