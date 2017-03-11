package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingProfileActivity;
import com.mjict.signboardsurvey.activity.ShopInputActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.activity.SignListActivity;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ShopInformation;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.ui.ShopInfo;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.DeleteShopTask;
import com.mjict.signboardsurvey.task.LoadShopInformationByBuildingTask;
import com.mjict.signboardsurvey.task.LoadValidBuildingImageTask;
import com.mjict.signboardsurvey.task.ModifyShopTask;
import com.mjict.signboardsurvey.task.RegisterShopTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.widget.ShopOptionDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopListActivityHandler extends SABaseActivityHandler {

    private ShopListActivity activity;
    private Building currentBuilding;
    private List<ShopInformation> currentShops;

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

            @Override
            public void onShopItemLongClicked(int index) {
                shopItemLongClicked(index);
            }
        });

        activity.setBuildingInfoViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildingInfoViewClicked();
            }
        });

//        activity.setOptionMenuItemClickListener(new ShopListActivity.OnOptionMenuItemClickListener() {
//            @Override
//            public void addShopClicked() {
//                goToShopInput(null);
//            }
//        });

        activity.setAddShopButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShopInput(null);
            }
        });

        activity.setOnSortButtonClickListener(new ShopListActivity.OnSortButtonClickListener() {
            @Override
            public void onNameSortClicked(boolean reversed) {
                sortList(reversed);
            }
        });

        // init
        currentBuilding = (Building)activity.getIntent().getSerializableExtra(MJConstants.BUILDING);
        if(currentBuilding == null) {
            // TODO 메세지 추가
            activity.finish();
            return;
        }

        MJContext.addRecentBuilding(currentBuilding.getId());  // 최근 건물 목록에 추가

        // 빌딩 정보 표시
        String baseAddr = currentBuilding.getProvince() + " " + currentBuilding.getCounty() + " " + currentBuilding.getTown();
        String houseAddress = baseAddr;
        if(currentBuilding.getVillage().equals("") == false)
            houseAddress = baseAddr + currentBuilding.getVillage();
        if(currentBuilding.getHouseNumber().equals("") == false)
            houseAddress = baseAddr + currentBuilding.getHouseNumber();

        String buildingNumber = currentBuilding.getFirstBuildingNumber();
        if (!currentBuilding.getSecondBuildingNumber().equals("") && !currentBuilding.getSecondBuildingNumber().equals("0"))
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
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MJConstants.REQUEST_SHOP_INFORMATION) {
            if(resultCode == Activity.RESULT_OK) {
                Shop shop = (Shop) data.getSerializableExtra(MJConstants.SHOP);
                if(shop == null) {
                    // TODO something is wrong
                    return;
                }
                // 같은 이름 체크
                boolean haveSameName = false;
                for(int i=0; i<currentShops.size(); i++) {
                    Shop s = currentShops.get(i).shop;
                    if(s.getName().equals(shop.getName())) {
                        haveSameName = true;
                        break;
                    }
                }

                if(haveSameName) {
                    Toast.makeText(activity, R.string.cannot_register_same_name_shop, Toast.LENGTH_SHORT).show();
                    return;
                }

                // find out whether new or exist - 인덱스 찬기
                int index = -1;
                if(shop.getId() != -1) {
                    for (int i = 0; i < currentShops.size(); i++) {
                        Shop s = currentShops.get(i).shop;
                        if (s.getId() == shop.getId()) {
                            index = i;
                            break;
                        }
                    }
                }

                // 다른 정보 입력
                SimpleDateFormat syncTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
                String syncDate = syncTimeFormat.format(SyncConfiguration.getLastSynchronizeDate());

                shop.setBusinessCondition("4");
                shop.setSgCode(currentBuilding.getSgCode());
                shop.setBuildingId(currentBuilding.getId());
                shop.setAddressId(currentBuilding.getAddressId());
                shop.setSyncDate(syncDate);

                if(index == -1) {
                    // new shop
                    startToRegisterShop(shop);
                } else {
                    startToModifyShop(index, shop);
                }
            }
        } else if(requestCode == MJConstants.REQUEST_BUILDING_INFORMATION) {
            if(resultCode == Activity.RESULT_OK) {
                Building building = (Building) data.getSerializableExtra(MJConstants.BUILDING);
                if(building == null) {
                    // TODO something is wrong
                    return;
                }
                currentBuilding = building;
                // ui update 는 할게 없어 보임
            }
        }
    }

    private void buildingInfoViewClicked() {
        Intent intent = new Intent(activity, BuildingProfileActivity.class);
        intent.putExtra(HANDLER_CLASS, BuildingProfileActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, currentBuilding);
        activity.startActivityForResult(intent, MJConstants.REQUEST_BUILDING_INFORMATION);
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
        LoadShopInformationByBuildingTask task = new LoadShopInformationByBuildingTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<ShopInformation>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_shop_information);
            }

            @Override
            public void onTaskFinished(List<ShopInformation> shops) {
                activity.hideWaitingDialog();
                currentShops = shops;

                if(currentShops != null) {
                    Collections.sort(currentShops, new ShopNameComparator());   // 이름순 정렬
                    for(int i=0; i<currentShops.size(); i++) {
                        ShopInformation si = currentShops.get(i);
                        ShopInfo info = shopToShopInfo(si);

                        activity.addToList(info);
                    }
                }
            }
        });
        task.execute(currentBuilding.getId());
    }

    private void startToRegisterShop(final Shop shop) {
        RegisterShopTask task = new RegisterShopTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Long>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }
            @Override
            public void onTaskFinished(Long result) {
                activity.hideWaitingDialog();
                if(result == -1) {
                    Toast.makeText(activity, R.string.failed_to_save, Toast.LENGTH_SHORT).show();
                    return;
                }

                shop.setId(result);
                ShopInformation si = new ShopInformation(shop, new ArrayList<Sign>());
                currentShops.add(si);
                ShopInfo info = shopToShopInfo(si);
                activity.addToList(info);
            }
        });
        task.execute(shop);
    }

    private void startToModifyShop(final int index, final Shop shop) {
        Log.d("junseo", "startToModifyShop: "+shop.getBusinessCondition());
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

//                currentShops.set(index, shop);
                currentShops.get(index).shop = shop;
                ShopInfo info = shopToShopInfo(currentShops.get(index));
                activity.replaceListItem(index, info);
            }
        });
        task.execute(shop);
    }

    private void shopItemClicked(int index) {
        Shop shop = currentShops.get(index).shop;

        goToSignList(shop);
    }

    private void shopItemLongClicked(final int index) {
        final Shop shop = currentShops.get(index).shop;
        boolean deleteEnable = shop.isSynchronized() ? false : true;    // 앱에서 추가한 데이터만 삭제 할 수 있다.
        activity.showShopOptionDialog(new ShopOptionDialog.ShopOptionDialogOnClickListener() {
            @Override
            public void onModifyButtonClicked() {
                activity.hideShopOptionDialog();
                goToShopInput(shop);
            }
            @Override
            public void onAddSignButtonClicked() {
                activity.hideShopOptionDialog();
            }

            @Override
            public void onDeleteButtonClicked() {
                activity.hideShopOptionDialog();
                askAndDeleteShop(index);
            }
        }, deleteEnable);
    }

//    private void startToShutdown(final int index) {
//        final Shop shop = currentShops.get(index);
//        Log.d("junseo", "startToShutdown: "+shop.getBusinessCondition());
//        ModifyShopShutDownTask task = new ModifyShopShutDownTask(activity.getApplicationContext());
//        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Shop>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.saving);
//            }
//
//            @Override
//            public void onTaskFinished(Shop shop) {
//                activity.hideWaitingDialog();
//                int resId = (shop == null) ? R.string.failed_to_save : R.string.succeeded_to_save;
//                Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
//
//                Log.d("junseo", "onTaskFinished: "+shop.getBusinessCondition());
//
//                if(shop != null) {
//                    currentShops.set(index, shop);
//                    ShopInfo info = shopToShopInfo(shop);
//                    activity.replaceListItem(index, info);
//                }
//            }
//        });
//        task.execute(shop);
//    }

    private void startToDeleteShop(final int index) {
        final Shop shop = currentShops.get(index).shop;

        DeleteShopTask task = new DeleteShopTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.deleteing);
            }

            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
                int resId = result ? R.string.failed_to_delete : R.string.succeeded_to_delete;
                Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();

                if(result) {
                    currentShops.remove(shop);
                    activity.removeListItem(index);
                }
            }
        });
    }

    private void askAndDeleteShop(final int index) {
        final Shop shop = currentShops.get(index).shop;
        if(shop.isSynchronized() == true)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.delete_shop)
                .setMessage(R.string.do_you_want_to_delete_shop_data)
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        startToDeleteShop(index);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    private void sortList(boolean reversed) {
        if(currentShops == null)
            return;

        Collections.sort(currentShops, new ShopNameComparator());   // 이름순 정렬
        if(reversed)
            Collections.reverse(currentShops);

        activity.clearList();

        for(int i=0; i<currentShops.size(); i++) {
            ShopInformation si = currentShops.get(i);
            ShopInfo info = shopToShopInfo(si);

            activity.addToList(info);
        }

    }

    private void goToShopInput(Shop shop) {
        Intent intent = new Intent(activity, ShopInputActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopInputActivityHandler.class);
        intent.putExtra(MJConstants.SHOP, shop);
        activity.startActivityForResult(intent, MJConstants.REQUEST_SHOP_INFORMATION);
    }

    private void goToSignList(Shop shop) {
        Intent intent = new Intent(activity, SignListActivity.class);
        intent.putExtra(HANDLER_CLASS, SignListActivityHandler.class);
        intent.putExtra(MJConstants.SHOP, shop);
        activity.startActivity(intent);
    }

    private ShopInfo shopToShopInfo(ShopInformation shopInfo) {
        SettingDataManager smgr = SettingDataManager.getInstance();

        Shop shop = shopInfo.shop;
        List<Sign> signs = shopInfo.signs;
        String name = shop.getName();
        Setting categorySetting = smgr.getShopCategory(shop.getCategory());
        String category = categorySetting == null ? smgr.getDefaultShopCategoty() : categorySetting.getName();
        String phone = shop.getPhoneNumber();

        // 폐업 여부 체크 - 간판 하나라도 폐업이면 폐업 이라고 보여줘
        // 허가 정보 체크 - 하나라도 310을 간고 있으면 허가
        boolean demolished = false;
        boolean permitted = false;
        if(signs != null) {
            for (int i = 0; i<signs.size(); i++) {
                Sign s = signs.get(i);
                if(s.getStatsCode().equals("1")) {
                    demolished = true;
                }

                if(s.getTblNumber() == 310)
                    permitted = true;
            }
        }

        return new ShopInfo(name, phone, category, "", demolished, permitted);
    }

    private class ShopNameComparator implements Comparator<ShopInformation> {
        @Override
        public int compare(ShopInformation lhs, ShopInformation rhs) {
            return lhs.shop.getName().compareTo(rhs.shop.getName());
        }
    }

//    private class ShopDateComparator implements Comparator<Shop> {
//        @Override
//        public int compare(Shop lhs, Shop rhs) {
//            Date ld =
//            if(lhs == null || rhs == null)
//            return lhs.getName().compareTo(rhs.getName());
//        }
//    }
}
