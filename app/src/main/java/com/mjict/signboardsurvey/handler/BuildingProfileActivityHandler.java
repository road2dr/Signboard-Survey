package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingProfileActivity;
import com.mjict.signboardsurvey.activity.PictureActivity;
import com.mjict.signboardsurvey.adapter.ImageViewPagerAdapter;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadBuildingPictureTask;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.LoadShopByBuildingTask;
import com.mjict.signboardsurvey.task.LoadSignsByShopTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-11-15.
 */
public class BuildingProfileActivityHandler extends SABaseActivityHandler {

    private BuildingProfileActivity activity;
    private Building currentBuilding;
    private List<Shop> shops;
    private List<Sign> signs;
    private ArrayList<BuildingPicture> pictures;



    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (BuildingProfileActivity)getActivity();

        // register listener
//        activity.setAddPicButtonOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addPictureButtonClicked();
//            }
//        });

        activity.setImagePageOnChageListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                activity.showLoading();

                startToLoadImage(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        activity.setImagePagerOnItemClickListener(new ImageViewPagerAdapter.OnPagerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                goToBuildingPicture();
            }
        });

        // init
        pictures = new ArrayList<>();
        currentBuilding = (Building)activity.getIntent().getSerializableExtra(MJConstants.BUILDING);
        if(currentBuilding == null) {
            // TODO 메세지 추가
            activity.finish();
            return;
        }

        // do first job
        startToLoadDetailInformation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MJConstants.REQUEST_BUILDING_PICTURE) {
            if(resultCode == Activity.RESULT_OK) {
                pictures = (ArrayList<BuildingPicture>)data.getSerializableExtra(MJConstants.BUILDING_PICTURE_LIST);

                activity.setImageCount(pictures.size());
                Log.d("junseo", "??2");
                if(pictures.size() > 0) {
                    activity.setCurrentPage(0);
//                    startToLoadImage(0);
                }
            }
        }
//        if(requestCode == MJConstants.REQUEST_TAKE_AND_SAVE) {
//            if(resultCode == Activity.RESULT_OK) {
//                Boolean result = data.getBooleanExtra(MJConstants.RESPONSE, false);
//                if(result == false) {
//                    // TODO 사진 저장 실패
//                } else {
//                    String path = data.getStringExtra(MJConstants.PATH);
//                    startToSavePictureInformation(path);
//                }
//            }
//        }
    }

//    private void startToSavePictureInformation(String path) {
//        final BuildingPicture bp = new BuildingPicture(0, currentBuilding.getId(), path, "");
//        InsertBuildingPictureTask task = new InsertBuildingPictureTask(activity.getApplicationContext());
//        task.setDefaultAsyncTaskListener(new AsyncTaskListener<Long, Boolean>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.saving);
//            }
//            @Override
//            public void onTaskProgressUpdate(Long... values) {
//                if(values != null && values.length > 0) {
//                    long id = values[0];
//                    bp.setId(id);
//                    pictures.add(bp);
//
//                    activity.setImageCount(pictures.size());
//                }
//            }
//
//            @Override
//            public void onTaskFinished(Boolean result) {
//                activity.hideWaitingDialog();
//            }
//        });
//        task.execute(bp);
//    }

    private void startToLoadDetailInformation() {
        LoadShopByBuildingTask task = new LoadShopByBuildingTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Shop>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_shop_and_sign_information);
            }
            @Override
            public void onTaskFinished(List<Shop> shopList) {
                activity.hideWaitingDialog();
                if(shopList == null) {
                    // TODO 에러 발생
                } else {
                    shops = shopList;
                    startToLoadSignList();
                }
            }
        });
        task.execute(currentBuilding.getId());
    }

    private void startToLoadSignList() {
        Shop[] shopArr = new Shop[shops.size()];
        shopArr = shops.toArray(shopArr);

        LoadSignsByShopTask task = new LoadSignsByShopTask(activity.getApplicationContext());
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<Sign, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_shop_and_sign_information);
                signs = new ArrayList<Sign>();
            }
            @Override
            public void onTaskProgressUpdate(Sign... values) {
                for(int i=0; i<values.length; i++)
                    signs.add(values[i]);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();

                //
                String buildingNumber = currentBuilding.getFirstBuildingNumber();
                if(currentBuilding.getSecondBuildingNumber().equals("") == false)
                    buildingNumber = buildingNumber + "_" +currentBuilding.getSecondBuildingNumber();

                String baseAddress = currentBuilding.getProvince()+" "+currentBuilding.getCounty()+" "+currentBuilding.getTown();
                String streetAddress = baseAddress + currentBuilding.getStreetName() + " "+buildingNumber;
                String houseAddress = baseAddress + " "+currentBuilding.getVillage() + currentBuilding.getHouseNumber();
                String title = currentBuilding.getName().equals("") ? buildingNumber : currentBuilding.getName();
                String signInfoText = activity.getString(R.string.number_of_case, signs.size());
                String shopInfoText = activity.getString(R.string.number_of_case, shops.size());

                activity.setBuildingName(title);
                activity.setHouseAddressText(houseAddress);
                activity.setStreetAddressText(streetAddress);
                activity.setSignInfoText(signInfoText);
                activity.setShopInfoText(shopInfoText);

                startToLoadPictureInformation();
            }
        });
        task.execute(shopArr);
    }

    private void startToLoadPictureInformation() {
        LoadBuildingPictureTask task = new LoadBuildingPictureTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<BuildingPicture>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_building_picture_list);
            }
            @Override
            public void onTaskFinished(List<BuildingPicture> buildingPictures) {
                activity.hideWaitingDialog();

                if(buildingPictures == null) {
                    // TODO 에러 발생
                }

                for(int i=0; i<buildingPictures.size(); i++)
                    pictures.add(buildingPictures.get(i));


                activity.setImageCount(pictures.size());
                Log.d("junseo", "??1");
                if(pictures.size() > 0) {
                    activity.setCurrentPage(0);
                    startToLoadImage(0);    // 버그 - viewpager 버그 같음. 첫번째 에는 setCurrentPage 에 의해 OnPageChangeListener 가 호출 안된다
                }
            }
        });
        task.execute(currentBuilding.getId());
    }

    private void startToLoadImage(int position) {
        BuildingPicture pic = pictures.get(position);
        String path = SyncConfiguration.getDirectoryForBuildingPicture()+pic.getPath();
        Log.d("junseo", "start to load image: "+path);
        LoadImageTask task = new LoadImageTask();
        task.setSampleSize(4);
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... image) {
                if(image != null) {
                    activity.showImage(image[0].image);
                }
            }
            @Override
            public void onTaskFinished(Boolean result) {
            }
        });
        task.execute(path);
    }

//    private void addPictureButtonClicked() {
//        Intent intent = new Intent(activity, CameraActivity.class);
//        intent.putExtra(HANDLER_CLASS, BuildingCameraActivityHandler.class);
//        intent.putExtra(MJConstants.BUILDING, currentBuilding);
//
//        activity.startActivityForResult(intent, MJConstants.REQUEST_TAKE_AND_SAVE);
//    }

    private void goToBuildingPicture() {
        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(HANDLER_CLASS, BuildingPictureActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING_PICTURE_LIST, pictures);
        intent.putExtra(MJConstants.BUILDING, currentBuilding);
        intent.putExtra(MJConstants.INDEX, activity.getCurrentPage());

        activity.startActivityForResult(intent, MJConstants.REQUEST_BUILDING_PICTURE);
    }
}
