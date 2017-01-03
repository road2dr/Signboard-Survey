package com.mjict.signboardsurvey.model;

import android.graphics.Bitmap;

import net.daum.mf.map.api.MapPOIItem;

/**
 * Created by Junseo on 2016-07-27.
 */
public class LocationInformation extends MapPOIItem {
    public static final int MY_LOCATION = 8342341;
    public static final int BUILDING_LOCATIOM = 441231;

    public long id;
    public Bitmap image;
    public int type;
    public String address;
    public String name;
    public String information;
    public double lat;
    public double lon;
    public Building building;

    public LocationInformation(long id, Bitmap image, int type, String address, String name, String information,
                               double lat, double lon, Building building) {
        super();
        this.id = id;
        this.image = image;
        this.type = type;
        this.address = address;
        this.name = name;
        this.information = information;
        this.lat = lat;
        this.lon = lon;
        this.building = building;
    }
}
