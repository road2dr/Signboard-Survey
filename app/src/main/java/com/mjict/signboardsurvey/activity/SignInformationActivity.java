package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
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
    private ImageView buildingLocationImageView;
    private EditText typeTextView;
    private EditText sizeTextView;
    private ImageView lightTypeImageView;
    private EditText statusTextView;

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
        buildingLocationImageView = (ImageView)this.findViewById(R.id.building_image_view);
        typeTextView = (EditText)this.findViewById(R.id.type_edit_text);
        sizeTextView = (EditText)this.findViewById(R.id.size_edit_text);
        lightTypeImageView = (ImageView)this.findViewById(R.id.light_type_image_view);
        statusTextView = (EditText)this.findViewById(R.id.status_edit_text);

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

    public void setBuildingLocationImage(int resId) {
        buildingLocationImageView.setImageResource(resId);
    }

    public void setTypeText(String text) {
        typeTextView.setText(text);
    }

    public void setSizeText(String text) {
        sizeTextView.setText(text);
    }

    public void setLightTypeImage(int resId) {
        lightTypeImageView.setImageResource(resId);
    }

    public void setStatusText(String text) {
        statusTextView.setText(text);
    }

    public static interface OnOptionMenuItemClickListener {
        public void onModifySignClicked();
    }
}
