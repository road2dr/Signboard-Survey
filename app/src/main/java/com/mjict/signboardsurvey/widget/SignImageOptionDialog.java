package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-08-16.
 */
public class SignImageOptionDialog extends Dialog {

    private View dialogView;
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

        setContentView(R.layout.dialog_sign_image_option);

        Window window = getWindow();
        WindowManager.LayoutParams lpWindow = window.getAttributes();

        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        lpWindow.gravity = Gravity.CENTER;
        window.setAttributes(lpWindow);

        dialogView = (View)this.findViewById(R.id.dialogLayout);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( event.getAction () == MotionEvent.ACTION_UP ) {    // 다이얼로그 바깥 영역 터치하면 다잉얼로그 내려감
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
