package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.DetailBuildingInfo;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.SignOwnership;
import com.mjict.signboardsurvey.util.GeoPoint;
import com.mjict.signboardsurvey.util.GeoTrans;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-08-01.
 */
public class LoadBuildingDetailTask extends DefaultAsyncTask<List<Building>, DetailBuildingInfo, Boolean> {

    private Context context;

    public LoadBuildingDetailTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(List<Building>... params) {
        if(params == null)
            return false;

        if(isCancelled())
            return false;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<Building> buildings = params[0];
        int n = buildings.size();
        Log.d("junseo", "빌딩 상세 정보 로드: "+n);
        for(int i=0; i<n; i++) {
            Building b = buildings.get(i);
            Log.d("junseo", "building: "+i+" "+b.getPlcd());
            DetailBuildingInfo info = null;

            //
            if(isCancelled())
                break;

            // 사진 로드
            List<BuildingPicture> pics = dmgr.findBuildingPictureByBuildingId(b.getId());
            Bitmap image = null;
            if(pics != null) {
                for(int j=0; j<pics.size(); j++) {
                    String path = SyncConfiguration.getDirectoryForBuildingPicture()+pics.get(j).getPath();
                    File file = new File(path);
                    if(file.exists()) {
                        try {
//                            image = BitmapFactory.decodeFile(path);
                            BitmapFactory.Options opt = new BitmapFactory.Options();
                            opt.inSampleSize = 4;
                            image = BitmapFactory.decodeFile(path, opt);
                            break;
                        }catch(Exception e) {
                            e.printStackTrace();
                            image = null;
                        }
                    }
                }
            }

            // 총 정보 로드
            List<Shop> shops = dmgr.findShopByBuildingId(b.getId());

            // TODO 나중에 SignOwnership 이 없어지면 바뀌겠지
            List<Sign> signs = new ArrayList<>();
            for(int j=0; j<shops.size(); j++) {
                Shop shop = shops.get(j);
                if(shop.getIsDeleted() == true)
                    continue;

                List<SignOwnership> ownerships = dmgr.findSignOwnershipByShopId(shop.getId());

                for(int k=0; k<ownerships.size(); k++) {
                    Sign s = dmgr.getSign(ownerships.get(k).getSignId());
                    if(s.isDeleted() == false)
                        signs.add(s);
                }
            }

            double lat = -1;
            double lon = -1;

            GeoPoint utmkCurrent = new GeoPoint(b.getLongitude(), b.getLatitude());
            GeoPoint geoCurrent = GeoTrans.convert(GeoTrans.UTMK, GeoTrans.GEO, utmkCurrent);

            lat = geoCurrent.getY();    // 위도 Latitude
            lon = geoCurrent.getX();    // 경도 Longitude


//            String strAddress = b.getProvince()+" "+b.getCounty()+" "+b.getTown()+" "+b.getHouseNumber();
//            Geocoder geocoder = new Geocoder(context);
//            Address addr;
//            double lat = -1;
//            double lon = -1;
//            Location location = null;
//            try {
//                List<Address> listAddress = geocoder.getFromLocationName(strAddress, 1);
//                if (listAddress.size() > 0) { // 주소값이 존재 하면
//                    addr = listAddress.get(0); // Address형태로
//                    lat = addr.getLatitude();
//                    lon = addr.getLongitude();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            info = new DetailBuildingInfo(image, b, signs, lat, lon);
            publishProgress(info);

        }

        return true;
    }


}
