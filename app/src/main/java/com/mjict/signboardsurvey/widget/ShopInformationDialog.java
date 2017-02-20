package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.ui.ShopInputData;


/**
 * Created by Junseo on 2016-07-19.
 */
public class ShopInformationDialog extends Dialog {

    private TextView titleTextView;
    private EditText shopNameEditText;
    private EditText phoneNumberEditText;
    private EditText representativeEditText;
    private SimpleSpinner shopCategorySpinner;
//    private SimpleSpinner shopStatusSpinner;
//    private RadioGroup statusRadioGroup;
//    private EditText floorCountEditText;

    private Button confirmButton;
    private Button cancelButton;

    private ShopInformationDialogOnClickListener dialogListener;

    public ShopInformationDialog(Context context) {
        super(context , R.style.AppTheme_Base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.dialog_shop_information);

        titleTextView = (TextView)this.findViewById(R.id.title_text_view);
        shopNameEditText = (EditText)this.findViewById(R.id.shop_name_edit_text);
        phoneNumberEditText = (EditText)this.findViewById(R.id.phone_number_edit_text);
        representativeEditText = (EditText)this.findViewById(R.id.representative_edit_text);
        shopCategorySpinner = (SimpleSpinner)this.findViewById(R.id.shop_category_spinner);
//        statusRadioGroup = (RadioGroup)this.findViewById(R.id.status_radio_group);
//        floorCountEditText = (EditText)this.findViewById(R.id.floor_count_edit_text);
//        shopStatusSpinner = (SimpleSpinner)this.findViewById(R.id.shop_status_spinner);

        confirmButton = (Button)this.findViewById(R.id.confirm_button);
        cancelButton = (Button)this.findViewById(R.id.cancel_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopName = shopNameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String representative = representativeEditText.getText().toString();
                String category = (String)shopCategorySpinner.getSelectedDataId();
//                int checkedId = statusRadioGroup.getCheckedRadioButtonId();
//                Object tag = statusRadioGroup.findViewById(checkedId).getTag();
//                int status = (tag != null) ? (Integer)tag : -1;
//                int status = shopStatusSpinner.getSelectedDataId();

//                int floorCount = 0;
//                try {
//                    floorCount = Integer.valueOf(floorCountEditText.getText().toString());
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), R.string.wrong_number_type_input, Toast.LENGTH_SHORT).show();
//                    return;
//                }

                ShopInputData input = new ShopInputData(shopName, phoneNumber, representative, category, "");

                hide();
                if(dialogListener != null)
                    dialogListener.onConfirmButtonClicked(input);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                if(dialogListener != null)
                    dialogListener.onCancelButtonClicked();
            }
        });
    }

    public void addToCategorySpinner(Object id, Object data) {
        shopCategorySpinner.addSpinnerData(id, data);
    }

//    public void addToStatusSpinner(int id, Object data) {
//        shopStatusSpinner.addSpinnerData(id, data);
//    }

//    public void setStatusTag(int index, int code) {
//        View child = statusRadioGroup.getChildAt(index);
//        if(child != null)
//            child.setTag(code);
//    }

    public void setInputData(ShopInputData data) {
        shopNameEditText.setText(data.name);
        phoneNumberEditText.setText(data.phone);
        representativeEditText.setText(data.representative);
        shopCategorySpinner.setSpinnerSelection(data.category);
//        floorCountEditText.setText(String.valueOf(data.floorCount));
//        for(int i=0; i<statusRadioGroup.getChildCount(); i++) {
//            View child = statusRadioGroup.getChildAt(i);
//            if(child != null) {
//                Object tag = child.getTag();
//                int status = (tag != null) ? (Integer)tag : -1;
//                if(status == data.status)
//                    ((RadioButton)child).setChecked(true);
//            }
//        }
    }

    public void setShopInformationDialogOnClickListener(ShopInformationDialogOnClickListener listener) {
        dialogListener = listener;
    }

    public static interface ShopInformationDialogOnClickListener {
        public void onConfirmButtonClicked(ShopInputData input);
        public void onCancelButtonClicked();
    }
}
