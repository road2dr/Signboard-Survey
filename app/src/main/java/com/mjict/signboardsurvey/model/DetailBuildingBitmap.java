package com.mjict.signboardsurvey.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Junseo on 2016-08-01.
 */
public class DetailBuildingBitmap implements Serializable {
    public Bitmap image;
    public Building building;
    public List<Shop> shops;
    public List<Sign> signs;
    public double lat;
    public double lon;

    public DetailBuildingBitmap(Bitmap i, Building b, List<Shop> shops, List<Sign> signs, double lat, double lon) {
        image = i;
        building = b;
        this.shops = shops;
        this.signs = signs;
        this.lat = lat;
        this.lon = lon;
    }
}
