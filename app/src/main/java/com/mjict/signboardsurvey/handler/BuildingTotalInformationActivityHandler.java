package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingProfileActivity;
import com.mjict.signboardsurvey.activity.BuildingTotalInformationActivity;
import com.mjict.signboardsurvey.activity.PictureActivity;
import com.mjict.signboardsurvey.activity.SignPageActivity;
import com.mjict.signboardsurvey.adapter.ShopAndSignListAdapter;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Setting;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ShopInformation;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.ui.ShopAndSign;
import com.mjict.signboardsurvey.model.ui.ShopInfo;
import com.mjict.signboardsurvey.model.ui.ShopInputData;
import com.mjict.signboardsurvey.model.ui.SignInfo;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.DeleteShopTask;
import com.mjict.signboardsurvey.task.DeleteSignTask;
import com.mjict.signboardsurvey.task.LoadBuildingPictureTask;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.LoadShopInfoByBuildingTask;
import com.mjict.signboardsurvey.task.ModifyShopTask;
import com.mjict.signboardsurvey.task.RegisterShopTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;
import com.mjict.signboardsurvey.widget.ShopInformationDialog;
import com.mjict.signboardsurvey.widget.SignOptionDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Junseo on 2017-02-07.
 */
public class BuildingTotalInformationActivityHandler extends SABaseActivityHandler {
    private BuildingTotalInformationActivity activity;
    private Building currentBuilding;
    private List<ShopInformation> shopInformations;
    private ArrayList<BuildingPicture> buildingPictures = new ArrayList<BuildingPicture>();

    private int currentBuildingImageIndex;


    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (BuildingTotalInformationActivity)getActivity();

        currentBuilding = (Building)activity.getIntent().getSerializableExtra(MJConstants.BUILDING);

        if(currentBuilding == null) {
            activity.showAlertDialog(R.string.there_are_no_such_buildings_call_to_manager, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
            return;
        }

        startToLoadShopInformation();

        startToLoadBuildingPictureList();

        initSpinner();

        updateBuildingInfo();


        // listener
//        activity.setAreaTypeSpinnerSelectionChangeListener(new SimpleSpinner.OnItemSelectionChangedByTouchListener() { // TODO 얘는 일단 고려
//            @Override
//            public void onItemSelectionChanged(int position, Object data) {
//                Setting s = (Setting)data;
//                areaSpinnerChanged(s);
//            }
//        });

        activity.setBuildingInfoViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBuildingProfile();
            }
        });

        activity.setPrevImageButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevImageButtonClicked();
            }
        });

        activity.setNextImageButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextImageButtonClicked();
            }
        });

        activity.setBuildingImageViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBuildingPicture();
            }
        });

        activity.setSignListOnChildRowClickListener(new BuildingTotalInformationActivity.SignListOnChildRowClickListener() {
            @Override
            public void onChildRowClicked(int group, int child) {
                goToSignPage(group, child);
            }
        });

        activity.setGroupOptionButtonOnclickListener(new ShopAndSignListAdapter.GroupOptionButtonOnClickListener() {
            @Override
            public void onOptionButtonClicked(View view, int position) {
                groupOptionButtonClicked(view, position);
            }
        });

        activity.setToolBarOptionMenuOnClickListener(new BuildingTotalInformationActivity.ToolBarOptionMenuOnClickListener() {
            @Override
            public void onAddShopMenuClicked() {
                addShopButtonClicked();
            }
        });

        activity.setSignPictureOnClickListener(new ShopAndSignListAdapter.SignPictureOnClickListener() {
            @Override
            public void onPictureClicked(View view, int gpos, int cpos) {
                goToSignPicture(gpos, cpos);
            }
        });

        activity.setChildRowLongClickListener(new BuildingTotalInformationActivity.SignListOnChildRowLongClickListener() {
            @Override
            public void onChildRowLongClicked(int group, int child) {
                childRowLongClicked(group, child);
            }
        });

        activity.setShopOptionMenuOnClickListener(new BuildingTotalInformationActivity.ShopOptionMenuOnClickListener() {
            @Override
            public void onAddSignMenuClicked(int position) {
                goToSignPage(position, -1);
            }
            @Override
            public void onModifyShopMenuClicked(int position) {
                modifyShopButtonClicked(position);
            }
            @Override
            public void onDeleteShopMenuClicked(int position) {
                askAndDeleteShop(position);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MJConstants.REQUEST_SIGN_LIST_MODIFY) {
            if(resultCode == Activity.RESULT_OK) {  // 바뀐게 있음
                Shop shop = (Shop)data.getSerializableExtra(MJConstants.SHOP);
                ArrayList<Sign> signs = (ArrayList<Sign>)data.getSerializableExtra(MJConstants.SIGN_LIST);

                findAndReplace(shop, signs);
            }
        } else if(requestCode == MJConstants.REQUEST_BUILDING_INFORMATION) {
            if (resultCode == Activity.RESULT_OK) {
                Building building = (Building) data.getSerializableExtra(MJConstants.BUILDING);
                if (building == null) {
                    // TODO something is wrong
                    return;
                }
                currentBuilding = building;

                startToLoadBuildingPictureList();

                updateBuildingInfo();
            }
        }

        // TODO
//        if(requestCode == REQUEST_MODIFY_DATA) {
//            if(resultCode == Activity.RESULT_OK) {
//                Shop shop = (Shop) data.getSerializableExtra(SignInformationActivityHandler.MODIFIED_SHOP);
//                ArrayList<Sign> signs = (ArrayList<Sign>)data.getSerializableExtra(SignInformationActivityHandler.MODIFIED_SIGNS);
//                ArrayList<SignWithImage> signWithImages = new ArrayList<>();
//                for(int i=0; i<signs.size(); i++)
//                    signWithImages.add(new SignWithImage(null, signs.get(i)));
//
//                ShopInformation info = new ShopInformation(shop, signWithImages);
//                activity.setListData(selectedGroupPosition, info);
//
//                startToLoadSignImage(selectedGroupPosition, info);
//
//                selectedGroupPosition = -1;
//
//                // update status text
//                updateCurrentStatus();
//            }
//        }
//
//        if(requestCode == REQUEST_MODIFY_BUILDING_PICTURE_DATA) {
//            startToLoadBuildingPictureList();
//        }
    }

    private void addShopButtonClicked() {
        final ShopInputData inputData = new ShopInputData("", "", "", "", "");
        activity.setShopInputData(inputData);
        activity.showShopInformationDialog(new ShopInformationDialog.ShopInformationDialogOnClickListener() {
            @Override
            public void onConfirmButtonClicked(ShopInputData input) {
                startToRegisterNewShop(input);
            }
            @Override
            public void onCancelButtonClicked() {
                // nothing to do
            }
        });
    }

    private void goToSignPicture(int groupPosition, int childPosition) {
//        Pair<View, String> pair = Pair.create((View) view, "image_transition");
//        ActivityOptionsCompat options = ActivityOptionsCompat
//                .makeSceneTransitionAnimation(activity , pair);

        ShopInformation si = shopInformations.get(groupPosition);
        Sign sign = si.signs.get(childPosition);

        String path = SyncConfiguration.getDirectoryForSingPicture(sign.isSignPicModified())+sign.getPicNumber();

        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(HANDLER_CLASS, SignPictureActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);

        activity.startActivity(intent);
    }

    private void childRowLongClicked(final int group, final int child) {
        final ShopInformation si = shopInformations.get(group);
        final Sign sign = si.signs.get(child);
        boolean deletable = sign.isSynchronized() ? false : true;

        activity.showSignOptionDialog(deletable);
        activity.setSignOptionDialogButtonClickListener(new SignOptionDialog.SignOptionDialogOnClickListener() {
            @Override
            public void onShowDetailButtonClicked() {
                activity.hideSignOptionDialog();

                // 상세 정보 보기로 이동
                goToSignPage(group, child);
            }
            @Override
            public void onDeleteButtonClicked() {
                activity.hideSignOptionDialog();
                //
                DeleteSignTask task = new DeleteSignTask(activity.getApplicationContext());
                task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
                    @Override
                    public void onTaskStart() {
                        activity.showWaitingDialog(R.string.deleteing);
                    }
                    @Override
                    public void onTaskFinished(Boolean result) {
                        activity.hideWaitingDialog();
                        int msg = result ? R.string.succeeded_to_delete : R.string.failed_to_delete;
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

                        if(result == false)
                            return;

                        si.signs.remove(child);

                        // update
                        activity.deleteChildRow(group, child);

                        updateCurrentStatus();
                    }
                });
                task.execute(sign);
            }
        });
    }

    private void groupOptionButtonClicked(View view, final int group) {
        Shop shop = shopInformations.get(group).shop;
        boolean deletable = shop.isSynchronized() ? false : true;    // 앱에서 추가한 데이터만 삭제 할 수 있다.
        activity.showShopOptionMenu(view, group, deletable);
    }

    private void prevImageButtonClicked() {
        if(buildingPictures == null)
            return;

        int n = buildingPictures.size();
        currentBuildingImageIndex--;
        currentBuildingImageIndex = (currentBuildingImageIndex % n + n) % n;    //        (i % N + N) % N;

        updateBuildingPicturePageText();
        startToLoadBuildingImage();
    }

    private void nextImageButtonClicked() {
        if(buildingPictures == null)
            return;

        int n = buildingPictures.size();
        currentBuildingImageIndex++;
        currentBuildingImageIndex = (currentBuildingImageIndex % n + n) % n;

        updateBuildingPicturePageText();
        startToLoadBuildingImage();
    }

    private void goToSignPage(int groupPosition, int childPosition) {
        Shop shop = shopInformations.get(groupPosition).shop;
        ArrayList<Sign> signs = new ArrayList<>(shopInformations.get(groupPosition).signs);

        Intent intent = new Intent(activity, SignPageActivity.class);
        intent.putExtra(HANDLER_CLASS, SignPageActivityHandler.class);
        intent.putExtra(MJConstants.SIGN_LIST, signs);
        intent.putExtra(MJConstants.SHOP, shop);
        intent.putExtra(MJConstants.INDEX, childPosition);
        intent.putExtra(MJConstants.BUILDING, currentBuilding);
        activity.startActivityForResult(intent, MJConstants.REQUEST_SIGN_LIST_MODIFY);
    }

    private void goToBuildingProfile() {
        Intent intent = new Intent(activity, BuildingProfileActivity.class);
        intent.putExtra(HANDLER_CLASS, BuildingProfileActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, currentBuilding);
        activity.startActivityForResult(intent, MJConstants.REQUEST_BUILDING_INFORMATION);
    }

    private void goToBuildingPicture() {
        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(HANDLER_CLASS, BuildingPictureActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING_PICTURE_LIST, buildingPictures);
        intent.putExtra(MJConstants.BUILDING, currentBuilding);
        intent.putExtra(MJConstants.INDEX, currentBuildingImageIndex);

        activity.startActivityForResult(intent, MJConstants.REQUEST_BUILDING_PICTURE);
    }

    private void modifyShopButtonClicked(final int groupPosition) {
        final Shop targetShop = shopInformations.get(groupPosition).shop;

        String name = targetShop.getName();
        String phone = targetShop.getPhoneNumber();
        String representative = targetShop.getRepresentative();
        String category = targetShop.getCategory();
        final ShopInputData inputData = new ShopInputData(name, phone, representative, category, "");

        activity.setShopInputData(inputData);
        activity.showShopInformationDialog(new ShopInformationDialog.ShopInformationDialogOnClickListener() {
            @Override
            public void onConfirmButtonClicked(ShopInputData input) {
                startToModifyShop(groupPosition, input);
            }
            @Override
            public void onCancelButtonClicked() {
                // nothing to do
            }
        });
    }

    private void askAndDeleteShop(final int index) {
        final Shop shop = shopInformations.get(index).shop;
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

    private void startToDeleteShop(final int group) {   ///////////////////////
        final Shop shop = shopInformations.get(group).shop;

        DeleteShopTask task = new DeleteShopTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.deleteing);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
                int msg = result ? R.string.succeeded_to_delete : R.string.failed_to_delete;
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

                if(result) {
                    shopInformations.remove(group);
                    activity.deleteGroupRow(group);
                    updateCurrentStatus();
                }
            }
        });
        task.execute(shop);
    }

    private void startToModifyShop(final int position, ShopInputData inputData) {
        if(inputData.name.equals("")) {
            Toast.makeText(activity, R.string.input_shop_name, Toast.LENGTH_SHORT).show();
            return;
        }

        final ShopInformation shopInformation = shopInformations.get(position);
        final Shop shop = shopInformation.shop;
        final Shop temp = new Shop(shop);

        temp.setName(inputData.name);
        temp.setPhoneNumber(inputData.phone);
        temp.setRepresentative(inputData.representative);
        temp.setBusinessCondition(inputData.stats);
        temp.setCategory(inputData.category);

        ModifyShopTask task = new ModifyShopTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();

                if (result == false) {
                    Toast.makeText(activity, R.string.error_occurred_while_saving_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                //
                shopInformation.shop = temp;

                //
                ShopAndSign sas = shopInformationToShopAndSign(shopInformation);
                activity.setListData(position, sas);

                updateCurrentStatus();

                Toast.makeText(activity, R.string.succeeded_to_save, Toast.LENGTH_SHORT).show();
            }
        });
        task.execute(temp);

    }

    private void startToRegisterNewShop(ShopInputData inputData) {
        if(inputData.name.equals("")) {
            Toast.makeText(activity, R.string.input_shop_name, Toast.LENGTH_SHORT).show();
            return;
        }

        // 업소명 중복 체크
        boolean haveSameName = false;
        for(int i=0; i<shopInformations.size(); i++) {
            Shop s = shopInformations.get(i).shop;
            if(s.getName().equals(inputData.name)) {
                haveSameName = true;
                break;
            }
        }
        if(haveSameName) {
            Toast.makeText(activity, R.string.cannot_register_same_name_shop, Toast.LENGTH_SHORT).show();
            return;
        }

        // start to save shop
        String currentTime = Utilities.getCurrentTimeAsString();
        long id = -1;
        String licenseNumber = "";
        String ssn = "";
        String name = inputData.name;
        String representative = inputData.representative;
        String phoneNumber = inputData.phone;
        String businessCondition = "4";
        String category = inputData.category;
        long buildingId = currentBuilding.getId();
        String inputter = MJContext.getCurrentUser().getUserId();;
        String inputDate = currentTime;
        int tblNumber = 510;
        int addressId = currentBuilding.getAddressId();
        boolean isDeleted = false;
        String sgCode = currentBuilding.getSgCode();
        boolean isSynchronized = false;
        SimpleDateFormat syncTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
        String syncDate = syncTimeFormat.format(SyncConfiguration.getLastSynchronizeDate());

        final Shop shop = new Shop(id, licenseNumber, ssn, name, representative, phoneNumber, businessCondition,
                category, buildingId, inputter, inputDate, tblNumber, addressId, isDeleted, sgCode,
                isSynchronized, syncDate);

        RegisterShopTask task = new RegisterShopTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Long>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }

            @Override
            public void onTaskFinished(Long id) {
                activity.hideWaitingDialog();
                if (id == -1) {
                    Toast.makeText(activity, R.string.error_occurred_while_saving_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                //
                ShopInformation si = new ShopInformation(shop, new ArrayList<Sign>());
                shopInformations.add(si);

                //
                ShopAndSign sas = shopInformationToShopAndSign(si);
                activity.addToList(sas);

                updateCurrentStatus();

                Toast.makeText(activity, R.string.succeeded_to_save, Toast.LENGTH_SHORT).show();
            }
        });
        task.execute(shop);
    }

    private void startToLoadShopInformation() {
        LoadShopInfoByBuildingTask task = new LoadShopInfoByBuildingTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<ShopInformation>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_shop_and_sign_information);
            }
            @Override
            public void onTaskFinished(List<ShopInformation> shopInformation) {
                activity.hideWaitingDialog();
                if(shopInformation == null) {
                    Toast.makeText(activity, R.string.error_occurred_while_load_shop_and_sign, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(shopInformation.size() <=0)
                    Toast.makeText(activity, R.string.there_are_no_shop_and_sign, Toast.LENGTH_SHORT).show();

                shopInformations = shopInformation;

                int numberOfSigns = 0;
                for(int i=0; i<shopInformations.size(); i++) {
                    ShopInformation info = shopInformation.get(i);
                    ShopAndSign sas = shopInformationToShopAndSign(info);
                    activity.addToList(sas);
                    numberOfSigns = numberOfSigns + info.signs.size();
                }

                String status = activity.getString(R.string.building_status, shopInformations.size(), numberOfSigns);
                activity.setStatusText(status);

                startToLoadAllSignImages();
            }
        });
        task.execute(currentBuilding.getId());
//        task.execute();
    }

    private void startToLoadBuildingPictureList() {
        LoadBuildingPictureTask task = new LoadBuildingPictureTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<BuildingPicture>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_building_picture_list);
                buildingPictures.clear();
            }
            @Override
            public void onTaskFinished(List<BuildingPicture> pictures) {
                activity.hideWaitingDialog();

                if(pictures == null) {  // 에러 발생
                    activity.setBuildingImages(R.drawable.ic_no_image);
                    return;
                }

                for(int i=0; i<pictures.size(); i++)
                    buildingPictures.add(pictures.get(i));
                currentBuildingImageIndex = 0;

                if(buildingPictures.size() <= 0) {  // 이미지 없음
                    updateBuildingPicturePageText();
                    activity.setBuildingImages(R.drawable.ic_no_image);
                } else {
                    updateBuildingPicturePageText();
                    startToLoadBuildingImage();
                }
            }
        });
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentBuilding.getId());
    }

    private void startToLoadBuildingImage() {
        BuildingPicture pic = buildingPictures.get(currentBuildingImageIndex);
        String path = SyncConfiguration.getDirectoryForBuildingPicture(pic.isSynchronized()) + pic.getPath();
        LoadImageTask task = new LoadImageTask();
        task.setSampleSize(8);
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showLoadingBuildingImage();
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... image) {
                if(image != null)
                    activity.setBuildingImages(image[0].image);
            }
            @Override
            public void onTaskFinished(Boolean result) {
            }
        });
        task.execute(path);
    }

    private void startToLoadAllSignImages() {
        if(shopInformations == null)
            return;

        // 사진 경로들 구하기
        List<String> pathList = new ArrayList<>();
        for(int i=0; i<shopInformations.size(); i++) {
            ShopInformation shopInformation = shopInformations.get(i);
            List<Sign> signs = shopInformation.signs;
            for(int j=0; j<signs.size(); j++) {
                Sign sign = signs.get(j);
                String path = null;
                if(sign.getPicNumber().equals("") == false) {
                    String dir = SyncConfiguration.getDirectoryForSingPicture(sign.isSignPicModified());
                    path = dir + sign.getPicNumber();
                }
                pathList.add(path);
            }
        }

        String[] paths = new String[pathList.size()];
        paths = pathList.toArray(paths);

        // 사진 불러오기
        LoadImageTask task = new LoadImageTask();
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                for(int i=0; i<values.length; i++) {
                    IndexBitmap indexBitmap = values[i];

                    // 인덱스 계산
                    int gp = 0;
                    int cp = 0;
                    int childCount = 0;
                    for( ; gp<shopInformations.size(); gp++) {
                        ShopInformation shopInformation = shopInformations.get(gp);
                        List<Sign> signs = shopInformation.signs;
                        if(signs.size() + childCount > indexBitmap.index) {
                            cp = indexBitmap.index - childCount;
                            break;
                        }
                        childCount = childCount + signs.size();
                    }
                    Log.d("junseo", "gp: "+gp +", cp: "+cp);
                    activity.setSignImage(gp, cp, indexBitmap.image);
                }
            }
            @Override
            public void onTaskFinished(Boolean result) {
            }
        });
        task.execute(paths);
    }

    private void initSpinner() {
        // 지역 타입 설정
        SettingDataManager sdm = SettingDataManager.getInstance();
//        Setting[] areaTypes = sdm.getAreaTypeCodes();
//        for(int i=0; i<areaTypes.length; i++) {
//            activity.addToAreaTypeSpinner(areaTypes[i].getCode(), areaTypes[i]);
//        }

        // 업소 스피너 설정
        Setting[] categories = sdm.getShopCategories();
        Setting[] status = sdm.getShopConditions();
        for(int i=0; i<categories.length; i++)
            activity.addToShopCategorySpinner(categories[i].getCode(), categories[i]);
    }

    private void updateBuildingInfo() {
        String name = currentBuilding.getName();
        String buildingNumber = currentBuilding.getFirstBuildingNumber();
        if(currentBuilding.getSecondBuildingNumber() != null && currentBuilding.getSecondBuildingNumber().equals("")==false )
            buildingNumber = buildingNumber +"-" + currentBuilding.getSecondBuildingNumber();

        String common = currentBuilding.getProvince()+" "+currentBuilding.getCounty()+" "+currentBuilding.getTown();
        String streetAddress = common + currentBuilding.getStreetName()+" "+buildingNumber;
        String houseAddress = (currentBuilding.getVillage() == null) ?
                common+" "+currentBuilding.getHouseNumber() : common+" "+currentBuilding.getVillage()+" "+currentBuilding.getHouseNumber();

        SettingDataManager sdm = SettingDataManager.getInstance();
        Setting areaTypeSetting = sdm.getAreaTypeCode(currentBuilding.getAreaType());
        String areaType = (areaTypeSetting == null) ? sdm.getDefaultAreaTypeName() : areaTypeSetting.getName();
        activity.setAreaTypeText(areaType);

        activity.setBuildingNameText(name);
        activity.setStreetAddressText(streetAddress);
        activity.setHouseNumberAddressText(houseAddress);
    }

    private void updateBuildingPicturePageText() {
        String pageText = (currentBuildingImageIndex+1)+"/"+buildingPictures.size();
        activity.setPageText(pageText);
    }

    private void findAndReplace(Shop shop, ArrayList<Sign> signs) {
        int index = -1;
        for(int i=0; i<shopInformations.size(); i++) {
            Shop s = shopInformations.get(i).shop;
            if(s.getId() == shop.getId()) {
                index = i;
                break;
            }
        }

        if(index == -1) {
            // 에러 발생
            return;
        }

        shopInformations.get(index).signs.clear();
        for(int i=0; i<signs.size(); i++) {
            Sign sign = signs.get(i);
            shopInformations.get(index).signs.add(sign);
        }

        activity.clearList();
        for(int i=0; i<shopInformations.size(); i++) {
            ShopInformation info = shopInformations.get(i);
            ShopAndSign sas = shopInformationToShopAndSign(info);
            activity.addToList(sas);
        }

        updateCurrentStatus();

        startToLoadAllSignImages();
    }

    private ShopAndSign shopInformationToShopAndSign(ShopInformation shopInformation) {
        Shop shop = shopInformation.shop;
        List<Sign> signs = shopInformation.signs;

        SettingDataManager smgr = SettingDataManager.getInstance();
        Setting shopCategorySetting = smgr.getShopCategory(shop.getCategory());
        String shopCategory = (shopCategorySetting == null) ? smgr.getDefaultShopCategoty() : shopCategorySetting.getName();
        String shopName = shop.getName();
        String phone = shop.getPhoneNumber();
        String representative = shop.getRepresentative();

        // 폐업, 허가정보 체크
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

        ShopInfo shopInfo = new ShopInfo(shopName, phone, shopCategory, representative, demolished, permitted);
        List<SignInfo> signInfos = new ArrayList<>();
        for(int i=0; i<signs.size(); i++) {
            Sign sign = signs.get(i);

            Bitmap image = null;
            String content = sign.getContent();
            Setting typeSetting = smgr.getSignType(sign.getType());
            String type = (typeSetting == null) ? smgr.getDefaultSignTypeName() : typeSetting.getName();
            String size = String.format("%.2f X %.2f", sign.getWidth(), sign.getLength());
            Setting statsSetting = smgr.getSignStatus(sign.getStatsCode());
            String status = (statsSetting == null) ? smgr.getDefaultSignStatus() : statsSetting.getName();
            Setting lightSetting = smgr.getLightType(sign.getLightType());
            String light = (lightSetting == null) ? smgr.getDefaultLightTypeName() : lightSetting.getName();
            String location = String.format("%d / %d", sign.getPlacedFloor(), sign.getTotalFloor());
            Setting resultSetting = smgr.getResult(sign.getInspectionResult());
            String result = (resultSetting == null) ? smgr.getDefaultResultName() : resultSetting.getName();

            // 철거, 철거예정, 폐업 레이블 색상 설정
            int labelColor = -1;
            boolean labelVisible = true;
            if(sign.getStatsCode().equals("1"))
                labelColor = ContextCompat.getColor(activity, R.color.postit_color_1);
            else if(sign.getStatsCode().equals("2"))
                labelColor = ContextCompat.getColor(activity, R.color.postit_color_2);
            else if(sign.getStatsCode().equals("3"))
                labelColor = ContextCompat.getColor(activity, R.color.postit_color_3);
            else {
                labelColor = Color.TRANSPARENT;
                labelVisible = false;
            }
            boolean signPermitted = (sign.getTblNumber() == 310);

            SignInfo signInfo = new SignInfo(image, content, type, size, status, light, location, result, labelColor, labelVisible, signPermitted);
            signInfos.add(signInfo);
        }

        return new ShopAndSign(shopInfo, signInfos);
    }

    private void updateCurrentStatus() {
        // update status text
        int shopCount = shopInformations.size();
        int numberOfSigns = 0;
        for(int i=0; i<shopCount; i++) {
            ShopInformation s = shopInformations.get(i);
            int sn = s.signs == null ? 0 : s.signs.size();
            numberOfSigns = numberOfSigns + sn;
        }

        String status = activity.getString(R.string.building_status, shopCount, numberOfSigns);
        activity.setStatusText(status);
    }
}
