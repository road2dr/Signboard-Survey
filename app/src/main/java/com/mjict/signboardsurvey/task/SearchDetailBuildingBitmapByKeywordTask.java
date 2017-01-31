package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.DetailBuildingBitmap;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-01-05.
 */
public class SearchDetailBuildingBitmapByKeywordTask extends DefaultAsyncTask<String, DetailBuildingBitmap, Boolean> {
    private Context context;

    public SearchDetailBuildingBitmapByKeywordTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params == null)
            return false;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<Building> buildings = dmgr.findBuildingsContain(params[0], -1);

        List<DetailBuildingBitmap> buildingResults = new ArrayList<>();
        if (buildings == null)
            return false;

        int n = buildings.size();
        for (int i = 0; i < n; i++) {
            Building b = buildings.get(i);
            DetailBuildingBitmap info = null;

            // 사진 로드
            List<BuildingPicture> pics = dmgr.findBuildingPictureByBuildingId(b.getId());
            Bitmap image = null;
            if (pics != null && pics.size() > 0) {
                String path = SyncConfiguration.getDirectoryForBuildingPicture(b.isSync()) + pics.get(0).getPath();
                image = Utilities.loadImage(path, 8);
            }

            // 사진중에 null 이 아닌걸로 로드
//                if(pics != null) {
//                    for(int j=0; j<pics.size(); j++) {
//                        String path = SyncConfiguration.getDirectoryForBuildingPicture()+pics.get(j).getPath();
//                        // 첫 번째 사진으로 하던지
//                        image = Utilities.loadImage(path, 8);
//                        if(image != null)
//                            break;
//                    }
//                }

            // 총 정보 로드
            List<Shop> shopsInBuilding = dmgr.findShopByBuildingId(b.getId());

            List<Sign> allSigns = new ArrayList<>();
            for (int j = 0; j < shopsInBuilding.size(); j++) {
                Shop shop = shopsInBuilding.get(j);
                if (shop.isDeleted() == true)
                    continue;

                List<Sign> signs = dmgr.findSignsByShopId(shop.getId());
                for(int k=0; k<signs.size(); k++) {
                Sign s = signs.get(k);
                        allSigns.add(s);
                }
            }

            info = new DetailBuildingBitmap(image, b, shopsInBuilding, allSigns, -1, -1);

            publishProgress(info);
        }

        return true;
    }
}
