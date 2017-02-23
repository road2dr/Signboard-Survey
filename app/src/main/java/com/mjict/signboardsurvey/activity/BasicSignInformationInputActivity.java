package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.ImageSpinnerAdapter;
import com.mjict.signboardsurvey.model.IconItem;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

/**
 * Created by Junseo on 2016-11-17.
 */
public class BasicSignInformationInputActivity extends SABaseActivity {

    private View nextButton;
    private ImageButton backImageButton;
    private ImageView signImageView;
    private ImageButton addSignImageButton;
    private ImageButton measureImageButton;
    private EditText contentEditText;
    private SimpleSpinner signTypeSpinner;
    private SimpleSpinner statusSpinner;
    private EditText widthEditText;
    private EditText lengthEditText;
    private Spinner lightSpinner;
    private ImageSpinnerAdapter lightSpinnerAdapter;
    private EditText placedFloorEditText;
    private EditText totalFloorEditText;
    private EditText heightEditText;
    private CheckBox frontCheckBox;
    private CheckBox intersectionCheckBox;
    private CheckBox frontBackCheckBox;

//    private LightSpinnerOnItemClickListener lightSpinnerOnItemClickListener;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_basic_information;
    }

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();

        nextButton = this.findViewById(R.id.next_button);
        backImageButton = (ImageButton)this.findViewById(R.id.back_image_button);
        signImageView = (ImageView)this.findViewById(R.id.sign_image_view);
        addSignImageButton = (ImageButton)this.findViewById(R.id.add_sign_image_button);
        measureImageButton = (ImageButton)this.findViewById(R.id.sign_measure_button);

        lightSpinner = (Spinner)this.findViewById(R.id.light_type_spinner);
        lightSpinnerAdapter = new ImageSpinnerAdapter(this);
        lightSpinner.setAdapter(lightSpinnerAdapter);

        signTypeSpinner = (SimpleSpinner)this.findViewById(R.id.sign_type_spinner);
        statusSpinner = (SimpleSpinner)this.findViewById(R.id.status_spinner);
        contentEditText = (EditText)this.findViewById(R.id.content_edit_text);
        widthEditText = (EditText)this.findViewById(R.id.width_edit_text);
        lengthEditText = (EditText)this.findViewById(R.id.length_edit_text);
        heightEditText = (EditText)this.findViewById(R.id.height_edit_text);

        placedFloorEditText = (EditText)this.findViewById(R.id.placed_floor_edit_text);
        totalFloorEditText = (EditText)this.findViewById(R.id.total_floor_edit_text);
        frontCheckBox = (CheckBox)this.findViewById(R.id.front_check_box);
        intersectionCheckBox = (CheckBox)this.findViewById(R.id.intersection_check_box);
        frontBackCheckBox = (CheckBox)this.findViewById(R.id.front_back_check_box);


//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BasicSignInformationInputActivity.this, ExtraSignInformationInputActivity.class);
//                intent.putExtra(SActivityHandler.HANDLER_CLASS, ExtraSignInformationInputActivityHandler.class);
//                startActivity(intent);
//            }
//        });
//
//        // test
//        spinnerAdapter.add(new IconItem(R.drawable.img_lgith1, "LED"));
//        spinnerAdapter.add(new IconItem(R.drawable.img_light2, "일반조명"));
//        spinnerAdapter.add(new IconItem(R.drawable.img_light3, "네온사인"));

    }

    public void setSignImage(Bitmap image) {
        signImageView.setImageBitmap(image);
    }

    public void setSignImage(int resId) {
        signImageView.setImageResource(resId);
    }

    public void setSignImageOnClickListener(View.OnClickListener listener) {
        signImageView.setOnClickListener(listener);
    }

    public void setAddSignImageButtonOnClickListener(View.OnClickListener listener) {
        addSignImageButton.setOnClickListener(listener);
    }

    public void setMeasureButtonOnclickListener(View.OnClickListener listener) {
        measureImageButton.setOnClickListener(listener);
    }

    public void setMeasureButtonVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        measureImageButton.setVisibility(visibility);
    }

    public void setContentText(String text) {
        contentEditText.setText(text);
    }

    public String getInputContent() {
        return contentEditText.getText().toString();
    }

    public void setWidthText(String text) {
        widthEditText.setText(text);
    }

    public String getInputWidth() {
        return widthEditText.getText().toString();
    }

    public void setLengthText(String text) {
        lengthEditText.setText(text);
    }

    public String getInputLength() {
        return lengthEditText.getText().toString();
    }

    public void setHeightText(String text) {
        heightEditText.setText(text);
    }

    public String getInputHeight() {
        return heightEditText.getText().toString();
    }

    public void addToLightSpinner(IconItem item) {
        lightSpinnerAdapter.add(item);
    }

    public IconItem getSelectedLight() {
        IconItem item = (IconItem)lightSpinner.getSelectedItem();
        return item;
    }

    public void setLightSpinnerSelection(int index) {
        lightSpinner.setSelection(index);
    }

//    public void setLightSpinnerOnItemClickListener(LightSpinnerOnItemClickListener listener) {
//        lightSpinnerOnItemClickListener = listener;
//        lightSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                IconItem item = lightSpinnerAdapter.getItem(position);
//                if(lightSpinnerOnItemClickListener != null)
//                    lightSpinnerOnItemClickListener.onItemClicked(position, item);
//            }
//        });
//    }

    public void addToSignTypeSpinner(Object id, Object data) {
        signTypeSpinner.addSpinnerData(id, data);
    }

    public Object getSelectedSignType() {
        return signTypeSpinner.getSelectedData();
    }

    public void setSignTypeSpinnerSelection(Object id) {
        signTypeSpinner.setSpinnerSelection(id);
    }

    public void addToStatusSpinner(Object id, Object data) {
        statusSpinner.addSpinnerData(id, data);
    }

    public Object getSelectedStatus() {
        return statusSpinner.getSelectedData();
    }

    public void setStatusSpinnerSelection(Object id) {
        statusSpinner.setSpinnerSelection(id);
    }

    public void setNextButtonOnClickListener(View.OnClickListener listener) {
        nextButton.setOnClickListener(listener);
    }

    public void setBackImageButtonOnClickListener(View.OnClickListener listener) {
        backImageButton.setOnClickListener(listener);
    }

//    private EditText placedFloorEditText;
//    private EditText totalFloorEditText;
//    private CheckBox frontCheckBox;
//    private CheckBox intersectionCheckBox;
//    private CheckBox frontBackCheckBox;

    public void setPlacedFloorText(CharSequence text) {
        placedFloorEditText.setText(text);
    }

    public String setInputPlacedFloor() {
        return placedFloorEditText.getText().toString();
    }

    public void setTotalFloorText(CharSequence text) {
        totalFloorEditText.setText(text);
    }

    public String getInputTotalFloor() {
        return totalFloorEditText.getText().toString();
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

//    public static interface LightSpinnerOnItemClickListener {
//        public void onItemClicked(int position, IconItem item);
//    }
}
