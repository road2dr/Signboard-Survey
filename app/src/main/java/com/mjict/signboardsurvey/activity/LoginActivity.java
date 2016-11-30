package com.mjict.signboardsurvey.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

/**
 * Created by Junseo on 2016-11-09.
 */
public class LoginActivity extends SABaseActivity {

    private SimpleSpinner idSpinner;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();

        loginButton = (Button)this.findViewById(R.id.login_button);
        idSpinner = (SimpleSpinner)this.findViewById(R.id.id_spinner);
        passwordEditText = (EditText)this.findViewById(R.id.password_edit_text);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    public void setLoginButtonOnClickListener(View.OnClickListener listener) {
        loginButton.setOnClickListener(listener);
    }

    public void addToUserSpinner(int id, Object data) {
        idSpinner.addSpinnerData(id, data);
    }

    public Object getSelectedUser() {
        return idSpinner.getSelectedData();
    }

    public String getInputPassword() {
        return passwordEditText.getText().toString();
    }
}
