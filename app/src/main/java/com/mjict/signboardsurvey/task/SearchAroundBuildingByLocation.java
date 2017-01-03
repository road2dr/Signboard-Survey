package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Address;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.util.GeoPoint;
import com.mjict.signboardsurvey.util.GeoTrans;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-09-19.
 */
public class SearchAroundBuildingByLocation extends DefaultAsyncTask<Location, Integer, List<Building>> {

    private Context context;
    private Address targetAddress;

    public SearchAroundBuildingByLocation(Context c) {
        context = c;
    }

    public void setTargetAddress(Address address) {
        targetAddress = address;
    }

    @Override
    protected List<Building> doInBackground(Location... params) {
        Location loc = params[0];
        if(loc == null)
            return null;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        String province = SyncConfiguration.getProvinceForSync();

        List<Building> cb = null;
        List<Building> result = new ArrayList<>();
        if(targetAddress == null)
            cb = dmgr.findBuildingByProvince(province);
        else
            cb = dmgr.findBuilding(targetAddress.province, targetAddress.county, null,
                    null, null, null, null, null);

        int n = cb.size();

        // GEO 타입의 현재 위치를 grs80으로 변환
        GeoPoint geoCurrent = new GeoPoint(loc.getLongitude(), loc.getLatitude());
        GeoPoint utmkCurrent = GeoTrans.convert(GeoTrans.GEO, GeoTrans.UTMK, geoCurrent);
        Log.d("junseo", "current my location: "+utmkCurrent.getX()+" "+utmkCurrent.getY());

        for(int i=0; i<n; i++) {
            Building b = cb.get(i);

            if(b.getLatitude() == 0 )
                continue;

            GeoPoint utmkBuilding = new GeoPoint(b.getLongitude(), b.getLatitude());

            // 거리를 구한다
            double distance = GeoTrans.getDistancebyGrs80(utmkCurrent, utmkBuilding);
            Log.d("junseo", "building distance: "+distance);
            if(distance < 1)  // 1km 이내 건물
                result.add(b);
        }

        return result;
    }
}
