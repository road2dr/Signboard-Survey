package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingSearchActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.ui.BuildingResult;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadValidBuildingImageTask;
import com.mjict.signboardsurvey.task.SearchBuildingByAddressTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;

import java.util.List;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingSearchActivityHandler extends SABaseActivityHandler {
    private BuildingSearchActivity activity;
    private Address address;

    private LoadValidBuildingImageTask imageLoadTask;
    private List<Building> buildings;
    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (BuildingSearchActivity)getActivity();

        // register listener
        activity.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });

        activity.setListItemOnClickListener(new BuildingSearchActivity.BuildingListItemOnClickListener() {
            @Override
            public void onItemClicked(int position, BuildingResult building) {
                listItemClicked(position, building);
            }
        });

        // init
        Intent intent = activity.getIntent();
        String province = intent.getStringExtra(MJConstants.PROVINCE);
        String county = intent.getStringExtra(MJConstants.COUNTY);
        String town = intent.getStringExtra(MJConstants.TOWN);
        String street = intent.getStringExtra(MJConstants.STREET);
        address = new Address(province, county, town, street, null, null, null, null);

        // do first job
    }

    @Override
    public void onActivityStart() {
        super.onActivityStart();
    }

    @Override
    public void onActivityStop() {
        super.onActivityStop();

        if(imageLoadTask != null && imageLoadTask.isCancelled() == false)
            imageLoadTask.cancel(true);
    }

    private void searchButtonClicked() {
        String firstNumber = activity.getInputFirstNumber();
        String secondNumber = activity.getInputSecondNumber();

        address.firstNumber = firstNumber;
        address.secondNumber = secondNumber;

        final SearchBuildingByAddressTask task = new SearchBuildingByAddressTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Building>>() {
            @Override
            public void onTaskStart() {
                activity.clearListView();
                activity.showWaitingDialog(R.string.searching);
                if(imageLoadTask != null && imageLoadTask.getStatus() == AsyncTask.Status.RUNNING)
                    imageLoadTask.cancel(true);
            }

            @Override
            public void onTaskFinished(List<Building> results) {
                buildings = results;
                activity.hideWaitingDialog();
                for(int i=0; i<results.size(); i++) {
                    Building b = results.get(i);
                    String baseAddr = b.getProvince() + " " + b.getCounty() + " " + b.getTown();
                    String houseAddress = baseAddr;
                    if(b.getVillage().equals("") == false)
                        baseAddr = baseAddr + b.getVillage();
                    if(b.getHouseNumber().equals("") == false)
                        baseAddr = baseAddr + b.getHouseNumber();

                    String buildingNumber = b.getFirstBuildingNumber();
                    if (b.getSecondBuildingNumber() != null && b.getSecondBuildingNumber().equals("") == false)
                        buildingNumber = buildingNumber + "-" + b.getSecondBuildingNumber();

                    String streetAddress = baseAddr + b.getStreetName() + buildingNumber;

                    String name = b.getName().equals("") ? buildingNumber : b.getName();

                    BuildingResult br = new BuildingResult(null, name, streetAddress, houseAddress, "", "");
                    activity.addToList(br);
                }

                startToLoadBuildingImages();
            }
        });
        task.execute(address);
    }

    private void listItemClicked(int position, BuildingResult buildingResult) {
        Building building = buildings.get(position);

        Intent intent = new Intent(activity, ShopListActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopListActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, building);
        ////////////

        activity.startActivity(intent);
    }

    private void startToLoadBuildingImages() {
        if(imageLoadTask != null && imageLoadTask.getStatus() == AsyncTask.Status.RUNNING)
            imageLoadTask.cancel(true);

        Building[] buildingArray = new Building[buildings.size()];
        buildingArray = buildings.toArray(buildingArray);

        imageLoadTask = new LoadValidBuildingImageTask(activity.getApplicationContext());
        imageLoadTask.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                // nothing to do
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                if(values == null)
                    return;
                for(int i=0; i<values.length; i++) {
                    IndexBitmap ib = values[i];
                    activity.setListImage(ib.index, ib.image);
                }
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
                // nothing to do
            }
        });

        imageLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, buildingArray);
    }
}
