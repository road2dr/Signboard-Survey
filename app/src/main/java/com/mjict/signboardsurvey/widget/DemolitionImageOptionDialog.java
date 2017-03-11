package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-08-16.
 */
public class DemolitionImageOptionDialog extends Dialog {

    private View dialogView;
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
        lpWindow.gravity = Gravity.CENTER;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_demolition_image_option);

        dialogView = this.findViewById(R.id.dialogLayout);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( event.getAction () == MotionEvent.ACTION_UP ) {
            // create a rect for storing the window rect
            Rect r = new Rect();
            // retrieve the windows rect
            dialogView.getHitRect(r);
            // check if the event position is inside the window rect
            boolean intersects = r.contains((int) event.getX(), (int) event.getY());

            if(!intersects) {
                hide();
            }
        }
        return super.onTouchEvent ( event );
    }

    public void setDemolitionImageOptionDialogListener(DemolitionImageOptionDialogListener listener) {
        dialogListener = listener;
    }

    public interface DemolitionImageOptionDialogListener {
        public void onPictureChangeButtonClicked();
        public void onShowPictureButtonClicked();
    }
}
