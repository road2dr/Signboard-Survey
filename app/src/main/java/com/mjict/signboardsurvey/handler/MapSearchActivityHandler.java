package com.mjict.signboardsurvey.handler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.MapSearchActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.DetailBuildingBitmap;
import com.mjict.signboardsurvey.model.GpsStatus;
import com.mjict.signboardsurvey.model.LocationInformation;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadBuildingDetailTask;
import com.mjict.signboardsurvey.task.SearchAddressFromLocationTask;
import com.mjict.signboardsurvey.task.SearchAroundBuildingByLocation;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;

import java.util.List;

/**
 * Created by Junseo on 2016-07-15.
 */
public class MapSearchActivityHandler extends SABaseActivityHandler {

    private MapSearchActivity activity;

    private static MJLocationListener mainLocationListener;
    private static LocationManager locationManager;
    private static Location lastLocation;
    private static Address currentAddress;
    private static GpsStatus gpsStatus;

    private List<Building> searchResult;
//    private Building currentBuilding;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (MapSearchActivity)getActivity();

        activity.setSelectBuildingButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBuildingButtonClicked();
            }
        });

        activity.setMyLocationButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLocationButtonClicked();
            }
        });

//        Intent intent = activity.getIntent();
//        currentBuilding = (Building) intent.getSerializableExtra(MJConstants.BUILDING);

        //
        if(lastLocation != null) {
            activity.setCurrentMarkerLocation(lastLocation);
            activity.moveTo(lastLocation);
        }

        if (locationManager == null) {
            mainLocationListener = new MJLocationListener();
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, mainLocationListener);
        }

        // 인터넷 상태 체크
        boolean internetEnabled = checkInternet();
        if(internetEnabled == false) {
            Toast.makeText(activity, R.string.cannot_use_this_function_if_internet_disconnected, Toast.LENGTH_SHORT).show();
            activity.finish();
            return;
        }

        // gps 상태 체크
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gpsEnabled == false) {
            // TODO GPS 꺼져 있음.
            activity.setStatusText(R.string.gps_is_off_cannot_use_map_search);
            gpsStatus = GpsStatus.OFF;
        } else {
            if(lastLocation == null) {
                gpsStatus = GpsStatus.SEARCHING;
                activity.setStatusText(R.string.searching_around_buildings);
            } else {
                findAddress();
                gpsStatus = GpsStatus.FOUND_LOCATION;
            }
        }
    }

    private void startToFindAroundBuildings(final Location location, Address address) {

        SearchAroundBuildingByLocation task = new SearchAroundBuildingByLocation(activity.getApplicationContext());
        task.setTargetAddress(address);
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Building>>() {
            @Override
            public void onTaskStart() {
                Log.d("junseo", "search start");
                activity.showWaitingDialog(R.string.searching_around_buildings);
            }

            @Override
            public void onTaskFinished(List<Building> buildings) {
                activity.hideWaitingDialog();
                Log.d("junseo", "search finished");

                if(buildings == null) {
                    Toast.makeText(activity, R.string.error_occurred_while_search_buildings, Toast.LENGTH_SHORT);
                    return;
                }

                if(buildings.size() <= 0) {
                    Toast.makeText(activity, R.string.there_are_no_buildings_be_searched, Toast.LENGTH_SHORT);
                    return;
                }

                searchResult = buildings;
//                if(currentBuilding != null)
//                    searchResult.add(0, currentBuilding);

                startToFindBuildingImageAndInfo();
            }
        });
        task.execute(location);

    }

    int buildingMarkerCount = 0;
    LoadBuildingDetailTask buildingDetailTask = null;
    private void startToFindBuildingImageAndInfo() {
        if(buildingDetailTask != null && buildingDetailTask.getStatus() == AsyncTask.Status.RUNNING)
            buildingDetailTask.cancel(true);

        if(searchResult == null)
            return;

        buildingDetailTask = new LoadBuildingDetailTask(activity.getApplicationContext());
        buildingDetailTask.setDefaultAsyncTaskListener(new AsyncTaskListener<DetailBuildingBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.clearAllBuildingMarkers();
                buildingMarkerCount = 0;
            }
            @Override
            public void onTaskProgressUpdate(DetailBuildingBitmap... values) {
                if(values == null)
                    return;

                Log.d("junseo", "빌딩정보 받음");

                DetailBuildingBitmap info = values[0];
                Building b = info.building;

//                long id, Bitmap image, int type, String address, String name, String information, Building building
                String address = b.getProvince()+" "+b.getCounty()+" "+b.getTown()
                        +" "+b.getStreetName()+" "+b.getFirstBuildingNumber();

                if(!b.getSecondBuildingNumber().equals("") && !b.getSecondBuildingNumber().equals("0"))
                    address = address+"-"+b.getSecondBuildingNumber();

                String infoText = "간판: "+info.signs.size()+"개";

                LocationInformation li = new LocationInformation(buildingMarkerCount, info.image, LocationInformation.BUILDING_LOCATIOM,
                        address, "", infoText, info.lat, info.lon, b);

                activity.addBuildingMarker(li);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                // end of loading
                // nothing to do
            }
        });
        buildingDetailTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchResult);
    }

    private void myLocationButtonClicked() {
        if(lastLocation != null) {
            activity.moveTo(lastLocation);
        }
    }

    private void selectBuildingButtonClicked() {
        LocationInformation selectedItem = activity.getSelectedItem();
        if(selectedItem == null)
            return;;

        Intent intent = new Intent(activity, ShopListActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopListActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, selectedItem.building);
        activity.startActivity(intent);
    }

    /**
     * 다음 api 를 쓰면 동 까지 밖에 안 가져 온다.
     * 구글 api 를 쓰면 도로명/옛날주소 둘중에 하나로 섞여 온다.
     */
    private void findAddress() {
        SearchAddressFromLocationTask task = new SearchAddressFromLocationTask(activity);
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Address>() {
            @Override
            public void onTaskStart() {
                activity.setStatusText(R.string.searching_current_address);
            }

            @Override
            public void onTaskFinished(Address result) {
                if (result == null) {
                    return;
                }

//                String addressText = result.province + " " + result.county;
//                if (result.type == SimpleAddress.ADDRESS_TYPE_HOUSE_NUMBER) {
//                    addressText = addressText + " " + result.town + " " + result.houseNumber;
//                } else {
//                    addressText = addressText + " " + result.firstBuildingNumber;
//                    if (result.secondBuildingNumber != null)
//                        addressText = addressText + " " + result.secondBuildingNumber;
//                }
//                String text = activity.getString(R.string.current_location_colon, addressText);
//                activity.setCurrentLocationText(text);

                currentAddress = result;

                String message = "현재 위치: "+currentAddress.province+" "+currentAddress.county;

                startToFindAroundBuildings(lastLocation, currentAddress);
            }
        });
        task.execute(lastLocation);
    }

    private boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connect = false;
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting()) {  // wifi
                connect = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnectedOrConnecting()) { // 모바일 네트웍
                connect = true;
            } else {    // 네트워크 오프라인 상태.
                connect = false;
            }
        } else {    // 네트워크 null.. 모뎀이 없는 경우??
            connect = false;
        }

        return connect;
    }

    private class MJLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("junseo", "onLocationChanged: location");
            lastLocation = location;
            gpsStatus = GpsStatus.FOUND_LOCATION;

            activity.setCurrentMarkerLocation(location);

//            if(subLocationListener != null)
//                subLocationListener.onLocationChanged(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("junseo", "onStatusChanged: "+provider+" OUT_OF_SERVICE");
                    break;
                case LocationProvider.AVAILABLE:
                    Log.d("junseo", "onStatusChanged: "+provider+" AVAILABLE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("junseo", "onStatusChanged: "+provider+" TEMPORARILY_UNAVAILABLE");
                    break;
                default:
                    Log.d("junseo", "onStatusChanged: "+provider+" ??");
                    break;
            }

//            if(subLocationListener != null)
//                subLocationListener.onStatusChanged(provider, status, extras);
        }
        @Override
        public void onProviderEnabled(String provider) {
            Log.d("junseo", "onProviderEnabled: "+provider);
            gpsStatus = GpsStatus.SEARCHING;

//            if(subLocationListener != null)
//                subLocationListener.onProviderEnabled(provider);
        }
        @Override
        public void onProviderDisabled(String provider) {
            Log.d("junseo", "onProviderDisabled: "+provider);
            // GPS 가 꺼짐 ///////////////////
            lastLocation = null;
//            String text = activity.getString(R.string.gps_has_turned_off);
//            activity.setCurrentLocationText(text);
            gpsStatus = GpsStatus.OFF;

//            if(subLocationListener != null)
//                subLocationListener.onProviderDisabled(provider);
        }
    }

}
