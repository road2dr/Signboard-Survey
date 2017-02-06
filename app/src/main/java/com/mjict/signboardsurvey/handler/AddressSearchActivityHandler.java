package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.AddressSearchActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ShopAndSign;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.ui.BuildingResult;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadConsonantTask;
import com.mjict.signboardsurvey.task.LoadShopAndSignByBuildingTask;
import com.mjict.signboardsurvey.task.LoadStreetAddressTask;
import com.mjict.signboardsurvey.task.LoadTownListTask;
import com.mjict.signboardsurvey.task.LoadValidBuildingImageTask;
import com.mjict.signboardsurvey.task.SearchBuildingByAddressTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

import java.util.List;

/**
 * Created by Junseo on 2016-07-21.
 */
public class AddressSearchActivityHandler extends SABaseActivityHandler {
    private AddressSearchActivity activity;
    private LoadValidBuildingImageTask imageLoadTask;
    private List<Building> buildingList;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (AddressSearchActivity)getActivity();

        //
        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();


        activity.setCountySpinnerOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object id, Object data) {
                if(position == -1)
                    return;

                countySpinnerItemChanged(position, (String)data);
            }
        });

        activity.setTownSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object id, Object data) {
                if(position == -1)
                    return;

                townSpinnerItemChanged(position, (String)data);
            }
        });

        activity.setConsonantSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object id, Object data) {
                if(position == -1)
                    return;

                consonantSpinnerItemChanged(position, (String)data);
            }
        });

        activity.setStreetAddressSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findStreetAddress();
            }
        });

        activity.setHouseAddressSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findHouseAddress();
            }
        });

//        activity.setStreetSpinnerrOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
//            @Override
//            public void onItemSelectionChanged(int position, Object data) {
//                streetSpinnerItemChanged(position, (String)data);
//            }
//        });

        activity.setOnListItemClickListener(new AddressSearchActivity.BuildingListItemOnClickListener() {
            @Override
            public void onListItemClicked(int position, BuildingResult info) {
                listItemClicked(position, info);
            }
        });



        activity.addCountyToSpinner(county);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /////////////////////// TODO
//        if(requestCode == REQUEST_BUILDING_INFO) {
//            if(resultCode == Activity.RESULT_OK) {
//                Building building = (Building) data.getSerializableExtra(BuildingInformationActivityHandler.BUILDING_INFORMATION);
//                if(building != null) {
//                    activity.setListItem(requestBuildingIndex, building);
//
//                    requestBuildingIndex = 0;
//                }
//            }
//        }
    }

    private int requestBuildingIndex = 0;
    private void listItemClicked(int position, BuildingResult info) {
        if(imageLoadTask != null && imageLoadTask.getStatus() == AsyncTask.Status.RUNNING)
            imageLoadTask.cancel(true);

        requestBuildingIndex = position;

        Building building = buildingList.get(position);

        Intent intent = new Intent(activity, ShopListActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopListActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, building);

        activity.startActivity(intent);
//        activity.startActivityForResult(intent, REQUEST_BUILDING_INFO);
    }

    private void findStreetAddress() {
        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();
        String town = (String) activity.getSelectedTownSpinnerItem();
        String street = (String) activity.getSelectedStreetSpinnerItem();
        String firstNumber = activity.getInputFirstBuildingNumber();
        String secondNumber = activity.getInputSecondBuildingNumber();

        Address cond = new Address(province, county,
                town, street, firstNumber, secondNumber, null, null);

        findAddressAndShow(cond);
    }

    private void findHouseAddress() {
        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();
        String town = (String) activity.getSelectedTownSpinnerItem();
        String village = activity.getInputVillage();
        String houseNumber = activity.getInputHouseNumber();

        Address cond = new Address(province, county,
                town, null, null, null, village, houseNumber);

        findAddressAndShow(cond);
    }

    private void findAddressAndShow(Address cond) {
        SearchBuildingByAddressTask task = new SearchBuildingByAddressTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Building>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.searching_building);
                activity.clearBuildingList();
                buildingList = null;
                requestBuildingIndex = 0;
            }

            @Override
            public void onTaskFinished(List<Building> result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_search_building);
                    return;
                }

                buildingList = result;

                if(result.size() <= 0)
                    Toast.makeText(activity, R.string.there_are_no_such_buildings, Toast.LENGTH_SHORT).show();

                startToLoadShopAndSignInformation();
            }
        });
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cond);
    }

    private void startToLoadShopAndSignInformation() {
        Building[] buildings = new Building[buildingList.size()];
        buildings = buildingList.toArray(buildings);

        LoadShopAndSignByBuildingTask task = new LoadShopAndSignByBuildingTask(activity.getApplicationContext());
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<ShopAndSign, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_shop_and_sign_information);
            }

            @Override
            public void onTaskProgressUpdate(ShopAndSign... values) {
                if(values == null)
                    return;

                for(int i=0; i<values.length; i++) {
                    ShopAndSign sas = values[i];
                    Building b = buildingList.get(sas.index);
                    BuildingResult br = buildingToBuildingResult(b, sas.shops, sas.signs);
                    activity.addToList(br);
                }
            }

            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();

                startToLoadBuildingImage();
            }
        });
        task.execute(buildings);
    }

    private void startToLoadBuildingImage() {
        if(imageLoadTask != null && imageLoadTask.getStatus() == AsyncTask.Status.RUNNING)
            imageLoadTask.cancel(true);

        Building[] buildings = new Building[buildingList.size()];
        buildings = buildingList.toArray(buildings);

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
                    IndexBitmap li = values[i];
                    activity.setListImage(li.index, li.image);
                }
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
                // nothing to do
            }
        });

        imageLoadTask.execute(buildings);
    }



    private void countySpinnerItemChanged(int position, String item) {
        String province = SyncConfiguration.getProvinceForSync();
        String county = item;

        LoadTownListTask task = new LoadTownListTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<String[]>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_town_list);
                activity.clearTownSpinner();
            }
            @Override
            public void onTaskFinished(String[] result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_load_address_data);
                    return;
                }
                for(int i=0; i<result.length; i++)
                    activity.addTownToSpinner(result[i]);
            }
        });
        task.execute(province, county);
    }

    private void townSpinnerItemChanged(int position, String data) {
        String town = data;
        LoadConsonantTask task = new LoadConsonantTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<String[]>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_street_address_list);
                activity.clearConsonantSpinner();
            }
            @Override
            public void onTaskFinished(String[] result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_load_address_data);
                    return;
                }
                for(int i=0; i<result.length; i++)
                    activity.addConsonantToSpinner(result[i]);
            }
        });
        task.execute(town);
    }

    private void consonantSpinnerItemChanged(int position, String data) {
        String consonant = data;
        String town = (String)activity.getSelectedTownSpinnerItem();

        LoadStreetAddressTask task = new LoadStreetAddressTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<StreetAddress>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_street_address_list);
                activity.clearStreetSpinner();
            }
            @Override
            public void onTaskFinished(List<StreetAddress> result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_load_address_data);
                    return;
                }

                for(int i=0; i<result.size(); i++)
                    activity.addStreetToSpinner(result.get(i).getStreet());
            }
        });
        task.execute(town, consonant);
    }

    private BuildingResult buildingToBuildingResult(Building b, List<Shop> shopList, List<Sign> signList) {
//        Bitmap image, String name, String streetAddress, String houseAddress, int shopCount, int signCount
        String buildingNumber = b.getFirstBuildingNumber();
        if(b.getSecondBuildingNumber().equals("") == false)
            buildingNumber = buildingNumber +" "+ b.getSecondBuildingNumber();
        String baseAddress = b.getProvince() +" "+b.getCounty()+" "+b.getTown();
        String houseAddress = "";
        String name = b.getName().equals("") ? buildingNumber : b.getName();

        if(b.getVillage().equals(""))
            houseAddress = baseAddress + " " + b.getHouseNumber();
        else
            houseAddress = baseAddress + b.getVillage() +" "+b.getHouseNumber();

        String streetAddress = baseAddress + buildingNumber;

        int shopCount = shopList.size();
        int signCount = (signList == null) ? 0 : signList.size();

        String shopCountText = activity.getString(R.string.number_of_case, shopCount);
        String signCountText = activity.getString(R.string.number_of_case, signCount);

        return new BuildingResult(null, name, streetAddress, houseAddress, shopCountText, signCountText);
    }


}
