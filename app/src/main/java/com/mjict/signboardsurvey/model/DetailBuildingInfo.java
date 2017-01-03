package com.mjict.signboardsurvey.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Junseo on 2016-08-01.
 */
public class DetailBuildingInfo implements Serializable {
    public Bitmap image;
    public Building building;
    public List<Sign> signs;
    public double lat;
    public double lon;

    public DetailBuildingInfo(Bitmap i, Building b, List<Sign> s, double lat, double lon) {
        image = i;
        building = b;
        signs = s;
        this.lat = lat;
        this.lon = lon;
    }
}
