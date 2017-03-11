package com.mjict.signboardsurvey.handler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.AddAddressActivity;
import com.mjict.signboardsurvey.activity.BuildingTotalInformationActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.ui.SimpleBuilding;
import com.mjict.signboardsurvey.task.LoadTownListTask;
import com.mjict.signboardsurvey.task.RegisterBuildingTask;
import com.mjict.signboardsurvey.task.SearchBuildingByAddressTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-03-07.
 */
public class AddAddressActivityHandler extends SABaseActivityHandler {

    private AddAddressActivity activity;
    private List<Building> addedBuildings;

    private boolean expertMode = false;
    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (AddAddressActivity) getActivity();

        //
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        expertMode = sharedPreferences.getBoolean("expert_mode", false);
        if(expertMode)
            activity.setFinishWithBackButton(false);

        String province = SyncConfiguration.getProvinceForSync();
        String county = SyncConfiguration.getCountyForSync();

        // register listener
        activity.setCountySpinnerOnItemSelectionChangedListener(new SimpleSpinner.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int position, Object id, Object data) {
                if(position == -1)
                    return;
                countySpinnerItemChanged(position, (String)data);
            }
        });

        activity.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAddress();
            }
        });

        activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToBuildingPage(position);
            }
        });

        //
        activity.addCountyToSpinner(county);

        addedBuildings = new ArrayList<>();

    }

    private void findAddress() {
        String county = (String)activity.getSelectedCountyItem();
        String town = (String) activity.getSelectedTownItem();
        String village = activity.getInputVillage();
        String houseNumber = activity.getInputHouseNumber();

        if(houseNumber.equals("")) {
            Toast.makeText(activity, R.string.input_house_number, Toast.LENGTH_SHORT).show();
            return;
        }

        String province = SyncConfiguration.getProvinceForSync();
        final Address cond = new Address(province, county, town, null, null, null, village, houseNumber);
        final SearchBuildingByAddressTask task = new SearchBuildingByAddressTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Building>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.searching_building);
            }

            @Override
            public void onTaskFinished(List<Building> result) {
                activity.hideWaitingDialog();
                if(result == null) {
                    activity.showAlertDialog(R.string.error_occurred_while_search_building);
                    return;
                }
                if(result.size() <= 0) {
                    askAdd(cond);
                }

            }
        });
        task.execute(cond);
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

    private void askAdd(final Address address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.quit)
                .setMessage(R.string.there_are_no_such_address_do_you_want_register)
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        startToSaveAddress(address);
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

    private void startToSaveAddress(final Address address) {
        final Building building = createNewBuilding(address);

        RegisterBuildingTask task = new RegisterBuildingTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Long>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }

            @Override
            public void onTaskFinished(Long result) {
                activity.hideWaitingDialog();
                int resId = (result == -1) ? R.string.failed_to_save : R.string.succeeded_to_save;
                Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();

                // TODO 지금 building 의 id 가 AUTO_INCREMENT 가 설정 되어 있지 않다.
                // 서버에서 수정 되면 여기 주석부분을 해제해서 id 를 체크 해야함.
//                if(result != -1) {
                    building.setId(result);
                    addedBuildings.add(building);

                    SimpleBuilding sb = buildingToSimpleBuilding(building);
                    activity.addToList(sb);
//                }
            }
        });
        task.execute(building);
    }

    private SimpleBuilding buildingToSimpleBuilding(Building b) {
        String address = b.getProvince() +" "+b.getCounty() + " "+b.getTown();
        if(b.getVillage().equals("") == false)
            address = address + " "+b.getVillage();
        address = address + " "+b.getHouseNumber();

        SimpleBuilding sb = new SimpleBuilding("", address);

        return sb;
    }

    private Building createNewBuilding(Address address) {
        long id = -1;
        String province = address.province;
        String county = address.county;
        String town = address.town;
        String village = address.village;
        String additionalAddress = "";
        String houseNumber = address.houseNumber;
        String streetName = "";
        String firstBuildingNumber = "";
        String secondBuildingNumber = "";
        String name = "";
        boolean isMountain = false;
        String plcd = "";
        int addressId = -1;
        String areaType = "0";
        double latitude = -1;
        double longitude = -1;
        String sgCode = "";
        boolean isSync = false;
        String syncDate = "";

        Building building = new Building(id, province, county, town, village, additionalAddress,
                houseNumber, streetName, firstBuildingNumber, secondBuildingNumber, name, isMountain,
                plcd, addressId, areaType, latitude, longitude, sgCode, isSync, syncDate);

        return building;
    }

    private void goToBuildingPage(int position) {
        Building building = addedBuildings.get(position);

        Intent intent = null;
        if(expertMode) {
            intent = new Intent(activity, BuildingTotalInformationActivity.class);
            intent.putExtra(HANDLER_CLASS, BuildingTotalInformationActivityHandler.class);
            intent.putExtra(MJConstants.BUILDING, building);
        } else {
            intent = new Intent(activity, ShopListActivity.class);
            intent.putExtra(HANDLER_CLASS, ShopListActivityHandler.class);
            intent.putExtra(MJConstants.BUILDING, building);
        }

        activity.startActivity(intent);
    }
}
