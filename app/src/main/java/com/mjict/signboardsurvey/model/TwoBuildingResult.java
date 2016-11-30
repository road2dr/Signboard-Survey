package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2016-11-14.
 */
public class TwoBuildingResult {
    public BuildingResult first;
    public BuildingResult second;

    public TwoBuildingResult(BuildingResult f, BuildingResult s) {
        first = f;
        second = s;
    }
}
