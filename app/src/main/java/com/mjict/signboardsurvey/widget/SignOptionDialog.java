package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-08-25.
 */
public class SignOptionDialog extends Dialog {
    private TextView titleTextView;
    private View showDetailButton;
    private View deleteButton;

    private SignOptionDialogOnClickListener dialogListener;

    public SignOptionDialog(Context context) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_sign_option);

        titleTextView = (TextView)this.findViewById(R.id.title_text_view);
        showDetailButton = (View)this.findViewById(R.id.show_detail_button);
        deleteButton = (View)this.findViewById(R.id.delete_button);

        showDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onShowDetailButtonClicked();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onDeleteButtonClicked();
            }
        });
    }

    public void setTietleViewText(String text) {
        titleTextView.setText(text);
    }

    public void setSignOptionDailogOnClickListener(SignOptionDialogOnClickListener listener) {
        dialogListener = listener;
    }

    public void setDeleteButtonVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        deleteButton.setVisibility(visibility);
    }

    public interface SignOptionDialogOnClickListener {
        public void onShowDetailButtonClicked();
        public void onDeleteButtonClicked();
    }
}
