package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.DetailBuildingBitmap;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.SignOwnership;
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
                String path = SyncConfiguration.getDirectoryForBuildingPicture() + pics.get(0).getPath();
                image = Utilities.loadImage(path, 8);
            }

            // 사진중에 null 이 아닌걸로 로드
//                if(pics != null) {
//                    for(int j=0; j<pics.size(); j++) {
//                        String path = SyncConfiguration.getDirectoryForBuildingPicture()+pics.get(j).getPath();
//                        // TODO 건물마다 무슨 사진이 이렇게 많나.. 이것만 아니면 빠르겠는데.. 데이터 맞는지 확인 해봐
//                        // 첫 번째 사진으로 하던지
//                        image = Utilities.loadImage(path, 8);
//                        if(image != null)
//                            break;
//                    }
//                }

            // 총 정보 로드
            List<Shop> shopsInBuilding = dmgr.findShopByBuildingId(b.getId());

            // TODO 나중에 SignOwnership 이 없어지면 바뀌겠지
            List<Sign> signs = new ArrayList<>();
            for (int j = 0; j < shopsInBuilding.size(); j++) {
                Shop shop = shopsInBuilding.get(j);
                if (shop.getIsDeleted() == true)
                    continue;

                List<SignOwnership> ownerships = dmgr.findSignOwnershipByShopId(shop.getId());

                for (int k = 0; k < ownerships.size(); k++) {
                    Sign s = dmgr.getSign(ownerships.get(k).getSignId());
                    if (s.isDeleted() == false)
                        signs.add(s);
                }
            }

            info = new DetailBuildingBitmap(image, b, shopsInBuilding, signs, -1, -1);

            publishProgress(info);
        }

        return true;
    }
}
