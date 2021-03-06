package com.mjict.signboardsurvey.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-17.
 */
public class SignInformationActivity extends SABaseActivity {

    private View signImageLayout;
    private ImageView signImageView;
    private EditText contentTextView;
    private EditText displayLocationTextView;
//    private ImageView buildingLocationImageView;
    private CheckBox frontCheckBox;
    private CheckBox intersectionCheckBox;
    private CheckBox frontBackCheckBox;
    private EditText typeTextView;
    private EditText sizeTextView;
    private EditText lightTypeTextView;
    private EditText statusTextView;
    private EditText installSideTextView;

    private OnOptionMenuItemClickListener optionMenuItemClickListener;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_information;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_detail_information);

        this.showOptionButton();
        this.inflateOptionMenu(R.menu.option_menu_sign_information);
        this.setOnOptionMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modify_sign:
                        if(optionMenuItemClickListener != null)
                            optionMenuItemClickListener.onModifySignClicked();
                        break;
                }
                return true;
            }
        });

        signImageLayout = this.findViewById(R.id.sign_image_layout);
        signImageView = (ImageView)this.findViewById(R.id.sign_image_view);
        contentTextView = (EditText)this.findViewById(R.id.content_edit_text);
        displayLocationTextView = (EditText)this.findViewById(R.id.display_location_edit_text);
        frontCheckBox = (CheckBox)this.findViewById(R.id.front_check_box);
        intersectionCheckBox = (CheckBox)this.findViewById(R.id.intersection_check_box);
        frontBackCheckBox = (CheckBox)this.findViewById(R.id.front_back_check_box);
        typeTextView = (EditText)this.findViewById(R.id.type_edit_text);
        sizeTextView = (EditText)this.findViewById(R.id.size_edit_text);
        lightTypeTextView = (EditText)this.findViewById(R.id.light_type_text_view);
        statusTextView = (EditText)this.findViewById(R.id.status_edit_text);
        installSideTextView = (EditText)this.findViewById(R.id.placed_side_edit_text);

        frontCheckBox.setEnabled(false);
        intersectionCheckBox.setEnabled(false);
        frontBackCheckBox.setEnabled(false);

        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(typeTextView.getWindowToken(), 0);

//        signImageLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignInformationActivity.this, BasicSignInformationInputActivity.class);
//                intent.putExtra(SActivityHandler.HANDLER_CLASS, BasicSignInformationInputActivityHandler.class);
//                startActivity(intent);
//            }
//        });




    }

    public void setOptionMenuItemClickListener(SignInformationActivity.OnOptionMenuItemClickListener listener) {
        optionMenuItemClickListener = listener;
    }

    public void setSignImage(Bitmap image) {
        signImageView.setImageBitmap(image);
    }

    public void setSignImageViewOnClickListener(View.OnClickListener listener) {
        signImageLayout.setOnClickListener(listener);
    }

    public void setContentText(String text) {
        contentTextView.setText(text);
    }

    public void setDisplayLocationText(String text) {
        displayLocationTextView.setText(text);
    }

    public void setFrontChecked(boolean checked) {
        frontCheckBox.setChecked(checked);
    }

    public void setIntersectionChecked(boolean checked) {
        intersectionCheckBox.setChecked(checked);
    }

    public void setFrontBackChecked(boolean checked){
        frontBackCheckBox.setChecked(checked);
    }

    public void setTypeText(String text) {
        typeTextView.setText(text);
    }

    public void setSizeText(String text) {
        sizeTextView.setText(text);
    }

    public void setLightTypeText(String text) {
        lightTypeTextView.setText(text);
    }

    public void setStatusText(String text) {
        statusTextView.setText(text);
    }

    public void setInstallSideText(String text) {
        installSideTextView.setText(text);
    }

    public static interface OnOptionMenuItemClickListener {
        public void onModifySignClicked();
    }
}
