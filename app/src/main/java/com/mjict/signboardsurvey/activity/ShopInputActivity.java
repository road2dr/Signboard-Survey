package com.mjict.signboardsurvey.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

/**
 * Created by Junseo on 2017-01-04.
 */
public class ShopInputActivity extends SABaseActivity {

    private ImageButton backButton;
    private View doneButton;
    private EditText shopNameEditText;
    private EditText phoneEditText;
    private SimpleSpinner conditionSpinner;
    private SimpleSpinner categorySpinner;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shop_input;
    }

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();
        this.disableNavigation();

        backButton = (ImageButton)this.findViewById(R.id.back_button);
        doneButton = this.findViewById(R.id.done_button);
        shopNameEditText = (EditText)this.findViewById(R.id.name_edit_text);
        phoneEditText = (EditText)this.findViewById(R.id.phone_edit_text);
        conditionSpinner = (SimpleSpinner)this.findViewById(R.id.condition_spinner);
        categorySpinner = (SimpleSpinner)this.findViewById(R.id.category_spinner);
    }

    public void setBackButtonOnClickListener(View.OnClickListener listener) {
        backButton.setOnClickListener(listener);
    }

    public void setDoneButtonOnClickListener(View.OnClickListener listener) {
        doneButton.setOnClickListener(listener);
    }

    public void setShopNameText(String text){
        shopNameEditText.setText(text);
    }

    public String getInputShopName() {
        return shopNameEditText.getText().toString();
    }

    public void setPhoneText(String text) {
        phoneEditText.setText(text);
    }

    public String getInputPhone() {
        return phoneEditText.getText().toString();
    }

    public void addToConditionSpinner(int id, Object data) {
        conditionSpinner.addSpinnerData(id, data);
    }

    public Object getSelectedCondition() {
        return conditionSpinner.getSelectedData();
    }

    public void setConditionSelection(int id) {
        conditionSpinner.setSpinnerSelection(id);
    }

    public void addToCategorySpinner(int id, Object data) {
        categorySpinner.addSpinnerData(id, data);
    }

    public Object getSelectedCategory() {
        return categorySpinner.getSelectedData();
    }

    public void setCategorySelection(int id) {
        categorySpinner.setSpinnerSelection(id);
    }
}
