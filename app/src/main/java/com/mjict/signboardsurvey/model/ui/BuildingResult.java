package com.mjict.signboardsurvey.model.ui;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingResult {
    public Bitmap image;
    public String name;
    public String streetAddress;
    public String houseAddress;
    public String shopCountText;
    public String signCountText;

    public BuildingResult(Bitmap image, String name, String streetAddress, String houseAddress, String shopCount, String signCount) {
        this.image = image;
        this.name = name;
        this.streetAddress = streetAddress;
        this.houseAddress = houseAddress;
        this.shopCountText = shopCount;
        this.signCountText = signCount;
    }
}
