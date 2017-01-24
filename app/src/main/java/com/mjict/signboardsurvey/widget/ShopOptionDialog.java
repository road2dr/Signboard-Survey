package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

import static com.mjict.signboardsurvey.R.*;


/**
 * Created by Junseo on 2016-07-19.
 */
public class ShopOptionDialog extends Dialog {

    private TextView titleTextView;
    private View modifyButton;
    private View addSignButton;
    private View shutDownButton;
    private View deleteShopButton;

    private ShopOptionDialogOnClickListener dialogListener;

    public ShopOptionDialog(Context context) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_shop_option);

        titleTextView = (TextView)this.findViewById(R.id.title_text_view);
        modifyButton = (View)this.findViewById(R.id.modify_shop_button);
        addSignButton = (View)this.findViewById(R.id.add_sign_button);
        shutDownButton = (View)this.findViewById(id.shut_down_shop_button);

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onModifyButtonClicked();
            }
        });

        addSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onAddSignButtonClicked();
            }
        });

        shutDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogListener != null)
                    dialogListener.onShutDownButtonClicked();
            }
        });
    }

    public void setTietleViewText(String text) {
        titleTextView.setText(text);
    }

    public void setShopOptionDailogOnClickListener(ShopOptionDialogOnClickListener listener) {
        dialogListener = listener;
    }

    public interface ShopOptionDialogOnClickListener {
        public void onModifyButtonClicked();
        public void onAddSignButtonClicked();//
        public void onShutDownButtonClicked();
    }
}
