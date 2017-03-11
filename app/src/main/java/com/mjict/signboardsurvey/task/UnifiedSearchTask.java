package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.DetailBuildingBitmap;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.ShopWithBuilding;
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

        // 빌딩 상세 정보
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
                    BuildingPicture bp = pics.get(0);
                    String path = SyncConfiguration.getDirectoryForBuildingPicture(bp.isSynchronized()) + bp.getPath();
                    image = Utilities.loadImage(path, 8);
                }

                // 업소 및 간판 정보 로드
                List<Shop> shopsInBuilding = dmgr.findShopByBuildingId(b.getId());

                List<Sign> signsInBuilding = new ArrayList<>();
                for(int j=0; j<shopsInBuilding.size(); j++) {
                    Shop shop = shopsInBuilding.get(j);
                    if(shop.isDeleted() == true)
                        continue;

                    List<Sign> signs= dmgr.findSignsByShopId(shop.getId());

                    for(int k=0; k<signs.size(); k++) {
                        Sign s = signs.get(k);
                        signsInBuilding.add(s);
                    }
                }

                info = new DetailBuildingBitmap(image, b, shops, signsInBuilding, -1, -1);
                buildingResults.add(info);
            }
        }

        // 업소 상세 정보
        List<ShopWithBuilding> shopWithBuildings = new ArrayList<>();
        if(shops != null) {
            for(int i=0; i<shops.size(); i++) {
                Shop shop = shops.get(i);
                Building building = dmgr.getBuilding(shop.getBuildingId());
                shopWithBuildings.add(new ShopWithBuilding(building, shop));
            }
        }

        UnifiedSearchResult result = new UnifiedSearchResult(streetAddresses, buildingResults, shopWithBuildings);

        return result;
    }
}
