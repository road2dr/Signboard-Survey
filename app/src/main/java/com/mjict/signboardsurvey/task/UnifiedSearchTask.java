package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.DetailBuildingBitmap;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.UnifiedSearchResult;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-12-02.
 */
public class UnifiedSearchTask extends DefaultAsyncTask<String, Integer, UnifiedSearchResult> {

    private Context context;
    private int maxAddressCount;
    private int maxBuildingsCount;
    private int maxShopCount;

    public UnifiedSearchTask(Context c) {
        context = c;
        maxAddressCount = 10;
        maxBuildingsCount = 10;
        maxShopCount = 10;
    }

    public void setMaxAddressResultCount(int count) {
        maxAddressCount = count;
    }

    public void setMaxBuildingsResultCount(int count) {
        maxBuildingsCount = count;
    }

    public void setMaxShopCount(int count) {
        maxShopCount = count;
    }

    @Override
    protected UnifiedSearchResult doInBackground(String... params) {
        if(params == null)
            return null;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        List<StreetAddress> streetAddresses = dmgr.findStreetAddressContain(params[0], maxAddressCount);
        List<Building> buildings = dmgr.findBuildingsContain(params[0], maxBuildingsCount);
        List<Shop> shops = dmgr.findShopsContain(params[0], maxShopCount);

        List<DetailBuildingBitmap> buildingResults = new ArrayList<>();
        if(buildings != null) {
            int n = buildings.size();
            for(int i=0; i<n; i++) {
                Building b = buildings.get(i);
                DetailBuildingBitmap info = null;

                // 사진 로드
                List<BuildingPicture> pics = dmgr.findBuildingPictureByBuildingId(b.getId());
                Bitmap image = null;
                if(pics != null && pics.size() > 0) {
                    String path = SyncConfiguration.getDirectoryForBuildingPicture(b.isSync()) + pics.get(0).getPath();
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

                List<Sign> signsInBuilding = new ArrayList<>();
                for(int j=0; j<shopsInBuilding.size(); j++) {
                    Shop shop = shopsInBuilding.get(j);
                    if(shop.isDeleted() == true)
                        continue;

                    List<Sign> signs= dmgr.findSignsByShopId(shop.getId());

                    for(int k=0; k<signs.size(); k++) {
                        Sign s = signs.get(k);
                        if(s.isDeleted() == false)
                            signsInBuilding.add(s);
                    }
                }

                info = new DetailBuildingBitmap(image, b, shops, signsInBuilding, -1, -1);
                buildingResults.add(info);
            }
        }

        UnifiedSearchResult result = new UnifiedSearchResult(streetAddresses, buildingResults, shops);

        return result;
    }
}
