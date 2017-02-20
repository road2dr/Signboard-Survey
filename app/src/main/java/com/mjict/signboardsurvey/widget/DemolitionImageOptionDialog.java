package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-08-16.
 */
public class DemolitionImageOptionDialog extends Dialog {

    private View pictureChangeButton;
    private View showButton;

    private DemolitionImageOptionDialogListener dialogListener;

    public DemolitionImageOptionDialog(Context context) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_demolition_image_option);

        pictureChangeButton = (View)this.findViewById(R.id.change_picture_button);
        showButton = (View)this.findViewById(R.id.show_picture_button);

        pictureChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onPictureChangeButtonClicked();
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

    public void setDemolitionImageOptionDialogListener(DemolitionImageOptionDialogListener listener) {
        dialogListener = listener;
    }

    public interface DemolitionImageOptionDialogListener {
        public void onPictureChangeButtonClicked();
        public void onShowPictureButtonClicked();
    }
}
