package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-08-16.
 */
public class SignImageOptionDialog extends Dialog {

    private View pictureChangeButton;
    private View changeAndMeasureButton;
    private View measureButton;
    private View showButton;

    private SignImageOptionDialogListener dialogListener;

    public SignImageOptionDialog(Context context) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_sign_image_option);

        pictureChangeButton = (View)this.findViewById(R.id.change_picture_button);
        changeAndMeasureButton = (View)this.findViewById(R.id.change_and_measure_button);
        measureButton = (View)this.findViewById(R.id.measure_button);
        showButton = (View)this.findViewById(R.id.show_picture_button);

        pictureChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onPictureChangeButtonClicked();
            }
        });

        changeAndMeasureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onChangeAndMeasureButtonClicked();
            }
        });

        measureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onMeasureButtonClicked();
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onShowPictureButtonClicked();
            }
        });
    }

    public void setSignImageOptionDialogListener(SignImageOptionDialogListener listener) {
        dialogListener = listener;
    }

    public interface SignImageOptionDialogListener {
        public void onPictureChangeButtonClicked();
        public void onChangeAndMeasureButtonClicked();
        public void onMeasureButtonClicked();
        public void onShowPictureButtonClicked();
    }
}
