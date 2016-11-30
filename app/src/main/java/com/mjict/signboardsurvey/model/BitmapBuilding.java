package com.mjict.signboardsurvey.model;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-11-30.
 */
public class BitmapBuilding {
    public Bitmap image;
    public Building building;

    public BitmapBuilding(Bitmap img, Building b) {
        image = img;
        building = b;
    }
}
