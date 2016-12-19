package com.mjict.signboardsurvey.model;

import com.mjict.signboardsurvey.model.ui.BuildingResult;

import java.util.List;

/**
 * Created by Junseo on 2016-12-05.
 */
public class UnifiedSearchResult {
    public List<StreetAddress> addresses;
    public List<BuildingResult> buildings;
    public List<Shop> shops;

    public UnifiedSearchResult(List<StreetAddress> addresses, List<BuildingResult> buildings, List<Shop> shops) {
        this.addresses = addresses;
        this.buildings = buildings;
        this.shops = shops;
    }
}
