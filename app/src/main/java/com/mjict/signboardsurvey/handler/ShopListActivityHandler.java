package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingProfileActivity;
import com.mjict.signboardsurvey.activity.ShopInputActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.activity.SignListActivity;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ui.ShopInfo;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadShopByBuildingTask;
import com.mjict.signboardsurvey.task.LoadValidBuildingImageTask;
import com.mjict.signboardsurvey.task.ModifyShopTask;
import com.mjict.signboardsurvey.task.RegisterShopTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;

import java.util.List;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopListActivityHandler extends SABaseActivityHandler {

    private ShopListActivity activity;
    private Building currentBuilding;
    private List<Shop> currentShops;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (ShopListActivity)getActivity();

        // register listener
        activity.setShopListItemOnClickListener(new ShopListActivity.OnShopListItemClickListener() {
            @Override
            public void onShopItemClicked(int index) {
                shopItemClicked(index);
            }
        });

        activity.setBuildingInfoViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildingInfoViewClicked();
            }
        });

        activity.setOptionMenuItemClickListener(new ShopListActivity.OnOptionMenuItemClickListener() {
            @Override
            public void addShopClicked() {
                goToShopInput(null);
            }
        });

        // init
        currentBuilding = (Building)activity.getIntent().getSerializableExtra(MJConstants.BUILDING);
        if(currentBuilding == null) {
            // TODO 메세지 추가
            activity.finish();
            return;
        }

        // 빌딩 정보 표시
        String baseAddr = currentBuilding.getProvince() + " " + currentBuilding.getCounty() + " " + currentBuilding.getTown();
        String houseAddress = baseAddr;
        if(currentBuilding.getVillage().equals("") == false)
            houseAddress = baseAddr + currentBuilding.getVillage();
        if(currentBuilding.getHouseNumber().equals("") == false)
            houseAddress = baseAddr + currentBuilding.getHouseNumber();

        String buildingNumber = currentBuilding.getFirstBuildingNumber();
        if (currentBuilding.getSecondBuildingNumber() != null && currentBuilding.getSecondBuildingNumber().equals("") == false)
            buildingNumber = buildingNumber + "-" + currentBuilding.getSecondBuildingNumber();

        String streetAddress = baseAddr + currentBuilding.getStreetName() + buildingNumber;
        String name = currentBuilding.getName().equals("") ? buildingNumber : currentBuilding.getName();

        activity.setBuildingName(name);
        activity.setHouseAddress(houseAddress);
        activity.setStreetAddress(streetAddress);

        // do first job
        startLoadBuildingImage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MJConstants.REQUEST_SHOP_INFORMATION) {
            if(resultCode == Activity.RESULT_OK) {
                Shop shop = (Shop) data.getSerializableExtra(MJConstants.SHOP);
                if(shop == null) {
                    // TODO something is wrong
                    return;
                }

                // find out whether new or exist
                int index = -1;
                for(int i=0; i<currentShops.size(); i++) {
                    Shop s = currentShops.get(i);
                    if(s.getId().equals(shop.getId())) {
                        index = i;
                        break;
                    }
                }

                if(index == -1) {
                    // new shop
                    startToRegisterShop(shop);
                } else {
                    startToModifyShop(index, shop);
                }
            }
        }
    }

    private void buildingInfoViewClicked() {
        Intent intent = new Intent(activity, BuildingProfileActivity.class);
        intent.putExtra(HANDLER_CLASS, BuildingProfileActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, currentBuilding);
        activity.startActivity(intent);
    }

    private void startLoadBuildingImage() {
        LoadValidBuildingImageTask task = new LoadValidBuildingImageTask(activity.getApplicationContext());
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_building_image);
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                if(values != null && values.length >= 0)
                    activity.setBuildingImage(values[0].image);
            }

            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
                startLoadShopInformation();
            }
        });
        task.execute(currentBuilding);
    }

    private void startLoadShopInformation() {
        LoadShopByBuildingTask task = new LoadShopByBuildingTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Shop>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_shop_information);
            }

            @Override
            public void onTaskFinished(List<Shop> shops) {
                activity.hideWaitingDialog();
                currentShops = shops;
                SettingDataManager smgr = SettingDataManager.getInstance();
                if(shops != null) {
                    for(int i=0; i<shops.size(); i++) {
                        Shop shop = shops.get(i);
                        ShopInfo info = shopToShopInfo(shop);

                        activity.addToList(info);
                    }
                }
            }
        });
        task.execute(currentBuilding.getId());
    }

    private void startToRegisterShop(final Shop shop) {
        shop.setBuildingId(currentBuilding.getId());
        shop.setAddressId(currentBuilding.getAddressId());
        RegisterShopTask task = new RegisterShopTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
                if(result == false) {
                    Toast.makeText(activity, R.string.failed_to_save, Toast.LENGTH_SHORT).show();
                    return;
                }

                currentShops.add(shop);
                ShopInfo info = shopToShopInfo(shop);
                activity.addToList(info);
            }
        });
        task.execute(shop);
    }

    private void startToModifyShop(final int index, final Shop shop) {
        shop.setBuildingId(currentBuilding.getId());
        shop.setAddressId(currentBuilding.getAddressId());
        ModifyShopTask task = new ModifyShopTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
                if(result == false) {
                    Toast.makeText(activity, R.string.failed_to_modify, Toast.LENGTH_SHORT).show();
                    return;
                }

                currentShops.set(index, shop);
                ShopInfo info = shopToShopInfo(shop);
                activity.replaceListItem(index, info);
            }
        });
        task.execute(shop);
    }

    private void shopItemClicked(int index) {
        Shop shop = currentShops.get(index);

        Intent intent = new Intent(activity, SignListActivity.class);
        intent.putExtra(HANDLER_CLASS, SignListActivityHandler.class);
        intent.putExtra(MJConstants.SHOP, shop);
        activity.startActivity(intent);
    }

    private void goToShopInput(Shop shop) {
        Intent intent = new Intent(activity, ShopInputActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopInputActivityHandler.class);
        intent.putExtra(MJConstants.SHOP, shop);
        activity.startActivityForResult(intent, MJConstants.REQUEST_SHOP_INFORMATION);
    }

    private ShopInfo shopToShopInfo(Shop shop) {
        SettingDataManager smgr = SettingDataManager.getInstance();
        String name = shop.getName();
        Setting categorySetting = smgr.getShopCategory(shop.getCategory());
        String category = categorySetting == null ? smgr.getDefaultShopCategoty() : categorySetting.getName();
        String phone = shop.getPhoneNumber();

        return new ShopInfo(name, phone, category);
    }
}
