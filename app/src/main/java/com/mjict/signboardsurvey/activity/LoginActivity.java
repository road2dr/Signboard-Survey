package com.mjict.signboardsurvey.activity;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

        // TODO DPI 테스트
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mgr = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        mgr.getDefaultDisplay().getMetrics(metrics);

        int deviceWidth = metrics.widthPixels;

        int deviceHeight = metrics.heightPixels;

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int dipWidth  = (int) (120  * metrics.density);

        int dipHeight = (int) (90 * metrics.density);

        Log.d("junseo", "displayMetrics.density : " + metrics.density);
        Log.d("junseo", "dip width: "+dipWidth+" dip height: "+dipHeight);
        Log.d("junseo", "densityDPI: "+metrics.densityDpi);
        Log.d("junseo", "deviceWidth : " + deviceWidth +", deviceHeight : "+deviceHeight);

        Log.d("junseo", "model: "+ Build.MODEL);
        Log.d("junseo", "device: "+ Build.DEVICE);
        Log.d("junseo", "product: "+ Build.PRODUCT);


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
