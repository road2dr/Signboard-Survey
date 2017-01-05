package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingListActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.DetailBuildingBitmap;
import com.mjict.signboardsurvey.model.ui.BuildingResult;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.SearchDetailBuildingBitmapByKeywordTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-01-05.
 */
public class MoreBuildingResultActivityHandler extends SABaseActivityHandler {

    private BuildingListActivity activity;
    private String keyword;
    private List<DetailBuildingBitmap> resultBuildings;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (BuildingListActivity)getActivity();
        activity.setTitle(R.string.more_building_search_result);

        // init
        resultBuildings = new ArrayList<>();
        Intent intent = activity.getIntent();
        keyword = intent.getStringExtra(MJConstants.KEYWORD);
        if(keyword == null) {
            // TODO something is wrong
            activity.finish();
            return;
        }

        // registerListener
        activity.setListOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Building building = resultBuildings.get(position).building;
                goToShopList(building);
            }
        });

        // do first job
        activity.setStatusTextVisible();
        startToSearchBuilding();
    }

    private void startToSearchBuilding() {
        SearchDetailBuildingBitmapByKeywordTask task = new SearchDetailBuildingBitmapByKeywordTask(activity.getApplicationContext());
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<DetailBuildingBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                String text = activity.getString(R.string.searching_building);
                activity.setStatusText(text);
                activity.setFinishWithBackButton(false);
            }

            @Override
            public void onTaskProgressUpdate(DetailBuildingBitmap... values) {
                if(values == null)
                    return;

                for(int i=0; i<values.length; i++) {
                    resultBuildings.add(values[i]);
                    BuildingResult br = detailBuildingToBuildingResult(values[i]);
                    activity.addToList(br);
                }
            }

            @Override
            public void onTaskFinished(Boolean result) {
                String text = activity.getString(R.string.search_result_count, resultBuildings.size());
                activity.setStatusText(text);
                activity.setFinishWithBackButton(true);
            }
        });
        task.execute(keyword);
    }

//    private void startToSearchShop() {
//        SearchShopByKeywordTask task = new SearchShopByKeywordTask(activity.getApplicationContext());
//        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Shop>>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.searching);
//            }
//
//            @Override
//            public void onTaskFinished(List<Shop> shops) {
//                activity.hideWaitingDialog();
//
//                resultShops = shops;
//                if(resultShops == null) {
//                    // TODO something is wrong
//                    return;
//                }
//                int n = resultShops.size();
//                for(int i=0; i<n; i++) {
//                    Shop shop = resultShops.get(i);
//                    String text = shop.getName()+" "+shop.getPhoneNumber();
//                    int index = text.indexOf(keyword);  // TODO StreetAddress 에서 DB 구까지 내용이 있어야 할 듯.
//                    SpannableStringBuilder ssb = new SpannableStringBuilder(text);
//                    ssb.setSpan(new AbsoluteSizeSpan(40), index, index+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    activity.addToList(ssb);
//                }
//            }
//        });
//        task.execute(keyword);
//    }

    private BuildingResult detailBuildingToBuildingResult(DetailBuildingBitmap detailBuilding) {
        Building building = detailBuilding.building;

        String buildingNumber = building.getFirstBuildingNumber();
        if(building.getSecondBuildingNumber().equals("") == false)
            buildingNumber = buildingNumber + "_" +building.getSecondBuildingNumber();
        String baseAddress = building.getProvince()+" "+building.getCounty()+" "+building.getTown();
        String streetAddress = baseAddress + building.getStreetName() + " "+buildingNumber;
        String houseAddress = baseAddress + " "+building.getVillage() + building.getHouseNumber();
        String title = building.getName().equals("") ? buildingNumber : building.getName();

        Bitmap image = detailBuilding.image;
        String name = title;

        int shopCount = detailBuilding.shops.size();
        int signCount = detailBuilding.signs.size();
        String shopCountText = activity.getString(R.string.number_of_case, shopCount);
        String signCountText = activity.getString(R.string.number_of_case, signCount);


        BuildingResult br = new BuildingResult(image, name, streetAddress, houseAddress, shopCountText, signCountText);
        return br;
    }

    private void goToShopList(Building building) {
        Intent intent = new Intent(activity, ShopListActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopListActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, building);
        activity.startActivity(intent);
    }

}
