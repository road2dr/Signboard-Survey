package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.ShopInputActivity;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.Utilities;

/**
 * Created by Junseo on 2017-01-04.
 */
public class ShopInputActivityHandler extends SABaseActivityHandler {
    private ShopInputActivity activity;
    private Shop currentShop;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (ShopInputActivity)getActivity();

        // init
        Intent intent = activity.getIntent();
        currentShop = (Shop) intent.getSerializableExtra(MJConstants.SHOP);
        if(currentShop == null)     // new shop
            currentShop = createNewShop();

        initSpinner();

        // register listener
        activity.setBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(Activity.RESULT_CANCELED);
            }
        });

        activity.setDoneButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonClicked();
            }
        });

        // do first job
        updateUI();

    }

    @Override
    public void onBackPressed() {
        activity.setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    private void doneButtonClicked() {
        if(currentShop == null)
            return;

        String shopName = activity.getInputShopName();
        String phone = activity.getInputPhone();
        Setting categorySetting = (Setting)activity.getSelectedCategory();

        if(shopName.equals("")) {
            Toast.makeText(activity, R.string.input_shop_name, Toast.LENGTH_SHORT).show();
            return;
        }

        currentShop.setName(shopName);
        currentShop.setPhoneNumber(phone);
        currentShop.setCategory(categorySetting.getCode());

        Intent responseIntent = new Intent();
        responseIntent.putExtra(MJConstants.SHOP, currentShop);
        activity.setResult(Activity.RESULT_OK, responseIntent);
        activity.finish();
    }

    private void initSpinner() {
        SettingDataManager smgr = SettingDataManager.getInstance();
        Setting[] categories = smgr.getShopCategories();

        for(int i=0; i<categories.length; i++)
            activity.addToCategorySpinner(categories[i].getCode(), categories[i]);
    }

    private void updateUI() {
        if(currentShop == null)
            return;

        activity.setShopNameText(currentShop.getName());
        activity.setPhoneText(currentShop.getPhoneNumber());
        activity.setCategorySelection(currentShop.getCategory());
    }

    private Shop createNewShop() {
        String currentTime = Utilities.getCurrentTimeAsString();
//        String id = String.valueOf(MJContext.getDeviceNumber()+Utilities.hash(currentTime));
//        String businessLicenseNumber = "";
//        String ssn = "";
//        String name = "";
//        String representative = "";
//        String phoneNumber = "";
//        int businessCondition = -1;
//        int category = -1;
//        long buildingId = -1;
//        String inputter = MJContext.getCurrentUser().getUserId();
//        String inputDate = currentTime;
//        int tblNumber = 510;
//        int addressId = -1;
//        boolean isDeleted = false;

        long id = -1;
        String licenseNumber = "";
        String ssn = "";
        String name = "";
        String representative = "";
        String phoneNumber = "";
        String businessCondition = "";
        String category = "";
        long buildingId = -1;
        String inputter = MJContext.getCurrentUser().getUserId();;
        String inputDate = currentTime;
        int tblNumber = 510;
        int addressId = -1;
        boolean isDeleted = false;
        String sgCode = "";
        boolean isSynchronized = false;
        String syncDate = "";

        Shop shop = new Shop(id, licenseNumber, ssn, name, representative, phoneNumber, businessCondition,
                category, buildingId, inputter, inputDate, tblNumber, addressId, isDeleted, sgCode,
                isSynchronized, syncDate);

        return shop;
    }
}
