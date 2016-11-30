package com.mjict.signboardsurvey.model;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-11-30.
 */
public class BitmapSign {
    public Bitmap image;
    public String name;
    public String type;
    public String result;
    public Sign sign;

    public BitmapSign(Bitmap image, String name, String type, String result, Sign sign) {
        this.image = image;
        this.name = name;
        this.type = type;
        this.result = result;
        this.sign = sign;
    }
}
