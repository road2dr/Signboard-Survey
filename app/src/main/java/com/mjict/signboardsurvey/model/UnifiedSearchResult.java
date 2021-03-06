package com.mjict.signboardsurvey.model;

import java.util.List;

/**
 * Created by Junseo on 2016-12-05.
 */
public class UnifiedSearchResult {
    public List<StreetAddress> addresses;
    public List<DetailBuildingBitmap> buildings;
    public List<ShopWithBuilding> shops;

    public UnifiedSearchResult(List<StreetAddress> addresses, List<DetailBuildingBitmap> buildings, List<ShopWithBuilding> shops) {
        this.addresses = addresses;
        this.buildings = buildings;
        this.shops = shops;
    }
}
