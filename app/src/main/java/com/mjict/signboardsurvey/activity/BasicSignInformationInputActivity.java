package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.View;
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
    private Spinner lightSpinner;
    private ImageSpinnerAdapter lightSpinnerAdapter;
    private SimpleSpinner statusSpinner;
    private EditText contentEditText;
    private EditText widthEditText;
    private EditText lengthEditText;
    private EditText heightEditText;

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

        lightSpinner = (Spinner)this.findViewById(R.id.light_type_spinner);
        lightSpinnerAdapter = new ImageSpinnerAdapter(this);
        lightSpinner.setAdapter(lightSpinnerAdapter);

        statusSpinner = (SimpleSpinner)this.findViewById(R.id.status_spinner);
        contentEditText = (EditText)this.findViewById(R.id.content_edit_text);
        widthEditText = (EditText)this.findViewById(R.id.width_edit_text);
        lengthEditText = (EditText)this.findViewById(R.id.length_edit_text);
        heightEditText = (EditText)this.findViewById(R.id.height_edit_text);



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

    public void addToStatusSpinner(int id, Object data) {
        statusSpinner.addSpinnerData(id, data);
    }

    public Object getSelectedStatus() {
        return statusSpinner.getSelectedData();
    }

    public void setStatusSpinnerSelection(int id) {
        statusSpinner.setSpinnerSelection(id);
    }

    public void setNextButtonOnClickListener(View.OnClickListener listener) {
        nextButton.setOnClickListener(listener);
    }

    public void setBackImageButtonOnClickListener(View.OnClickListener listener) {
        backImageButton.setOnClickListener(listener);
    }



//    public static interface LightSpinnerOnItemClickListener {
//        public void onItemClicked(int position, IconItem item);
//    }
}
