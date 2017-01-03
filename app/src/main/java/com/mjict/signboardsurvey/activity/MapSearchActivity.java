package com.mjict.signboardsurvey.activity;

import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.MJMapAdapter;
import com.mjict.signboardsurvey.model.LocationInformation;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

/**
 * Created by Junseo on 2016-07-15.
 */
public class MapSearchActivity extends SABaseActivity {

    private static final int MY_LOCATION_TAG = 9433331;
//    private static final int MY_LOCATION_TITLE =""

//    private ViewGroup mapView;
    private ImageButton locationSearchButton;
    private ImageButton selectBuildingButton;
    private TextView statusTextView;
    private MapView mapView;

    MJMapAdapter adapter;

    private LocationInformation selectedItem;

    MapView.POIItemEventListener listener = new MapView.POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
            if(mapPOIItem instanceof LocationInformation) {
                LocationInformation linfo = (LocationInformation)mapPOIItem;
                selectedItem = linfo;
                selectBuildingButton.setVisibility(View.VISIBLE);
            } else {
                selectBuildingButton.setVisibility(View.INVISIBLE);
            }
        }
        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        }
        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        }
        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        }
    };

    MapView.MapViewEventListener mListener = new MapView.MapViewEventListener() {
        @Override
        public void onMapViewInitialized(MapView mapView) {
        }
        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        }
        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        }
        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
            // POItem disslected
            selectBuildingButton.setVisibility(View.INVISIBLE);
            selectedItem = null;
        }
        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        }
        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        }
        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        }
        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        }
        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_map_search;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.map_search);

        locationSearchButton = (ImageButton)this.findViewById(R.id.location_search_button);
        selectBuildingButton = (ImageButton)this.findViewById(R.id.select_building_button);

        statusTextView = (TextView)this.findViewById(R.id.status_text_view);

        mapView = new MapView(this);
        mapView.setDaumMapApiKey(MJConstants.DAUM_MAP_API_KEY);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setZoomLevel(1, false);

        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return false;
            }
        });

        //
        adapter = new MJMapAdapter(getApplicationContext());

//        // test
//        MapPOIItem marker = new MapPOIItem();
//        marker.setItemName("Default Marker");
//        MapPoint point = MapPoint.mapPointWithGeoCoord(37.566627, 126.978660);  // test
//        marker.setMapPoint(point);
//        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        marker.setMoveToCenterOnSelect(true);
//
//        MapPOIItem customMarker = new MapPOIItem();
//        customMarker.setItemName("내 위치");
//        MapPoint cpoint = MapPoint.mapPointWithGeoCoord(37.566984, 126.978390);  // test
//        customMarker.setMapPoint(cpoint);
//        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
//        customMarker.setCustomImageResourceId(R.drawable.ic_map_marker_person); // 마커 이미지.
//        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
//        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
//
//        CustomCalloutBalloonAdapter adapter = new CustomCalloutBalloonAdapter(this);
////
////
//        int[] images = {
//                R.drawable.building_1,R.drawable.building_2,R.drawable.building_3,
//                R.drawable.building_4,R.drawable.building_5,R.drawable.building_6
//        };
//        adapter.setData("서울특별시 중구 태평로1가 세종대로 110", "서울특별시청", "가로: 2, 세로형: 1", images);
////
//        mapView.setCalloutBalloonAdapter(adapter);
//        mapView.addPOIItem(customMarker);
//        mapView.addPOIItem(marker);

        mapView.setCalloutBalloonAdapter(adapter);


        mapView.setPOIItemEventListener(listener);

        mapView.setMapViewEventListener(mListener);
    }

    public LocationInformation getSelectedItem() {
        return selectedItem;
    }

    public void setCurrentMarkerLocation(Location location) {
        if(location == null)
            return;

        MapPOIItem item = mapView.findPOIItemByTag(MY_LOCATION_TAG);

        if(item != null)
            mapView.removePOIItem(item);

        MapPoint point = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());
        MapPOIItem myLoc = new MapPOIItem();
        String name = getString(R.string.my_location);
        myLoc.setItemName(name);
        myLoc.setMapPoint(point);
        myLoc.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        myLoc.setCustomImageResourceId(R.drawable.icon_my_marker); // 마커 이미지.
        myLoc.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        myLoc.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f)
        myLoc.setTag(MY_LOCATION_TAG);

        mapView.addPOIItem(myLoc);
    }

    public void addBuildingMarker(LocationInformation info) {
        if(info.lat == -1)
            return;

        //        MapPOIItem marker = new MapPOIItem();
//        marker.setItemName("Default Marker");
//        MapPoint point = MapPoint.mapPointWithGeoCoord(37.566627, 126.978660);  // test
//        marker.setMapPoint(point);
//        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        marker.setMoveToCenterOnSelect(true);

        MapPoint point = MapPoint.mapPointWithGeoCoord(info.lat, info.lon);
        info.setItemName("Default Marker");
        info.setMapPoint(point);
        info.setMarkerType(MapPOIItem.MarkerType.BluePin);
        info.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        info.setMoveToCenterOnSelect(true);
        mapView.addPOIItem(info);
    }

    public void clearAllBuildingMarkers() {
        MapPOIItem[] items = mapView.getPOIItems();
        if(items == null)
            return;

        for(int i=0; i<items.length; i++) {
            if(items[i].getTag() != MY_LOCATION_TAG)
                mapView.removePOIItem(items[i]);
        }
    }

    public void setStatusText(String text) {
        statusTextView.setText(text);
    }

    public void setStatusText(int resId) {
        statusTextView.setText(resId);
    }

    public void setSelectBuildingButtonOnClickListener(View.OnClickListener listener) {
        selectBuildingButton.setOnClickListener(listener);
    }

    public void setMyLocationButtonOnClickListener(View.OnClickListener listener) {
        locationSearchButton.setOnClickListener(listener);
    }

    public void moveTo(Location loc) {
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()), true);
    }
}
