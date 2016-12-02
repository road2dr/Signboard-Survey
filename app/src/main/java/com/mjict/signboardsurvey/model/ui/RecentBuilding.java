package com.mjict.signboardsurvey.model.ui;

import android.graphics.Bitmap;

import com.mjict.signboardsurvey.model.BitmapBuilding;
import com.mjict.signboardsurvey.model.Building;

/**
 * Created by Junseo on 2016-12-01.
 */
public class RecentBuilding extends BitmapBuilding {

    public String name;
    public String address;

    public RecentBuilding(Bitmap image, Building building, String name, String address) {
        super(image, building);
        this.name = name;
        this.address = address;
    }
}
