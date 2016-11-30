package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingResult {
    public int image;
    public String streetAddress;
    public String houseAddress;
    public String buildingName;
    public String buildingNumber;

    public BuildingResult(int img, String streetAddress, String houseAddress, String buildingName, String buildingNumber) {
        image = img;
        this.streetAddress = streetAddress;
        this.houseAddress = houseAddress;
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
    }
}
