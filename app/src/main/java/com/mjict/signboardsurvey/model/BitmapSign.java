package com.mjict.signboardsurvey.model;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-11-30.
 */
public class BitmapSign {
    public Bitmap image;
    public Sign sign;

    public BitmapSign(Bitmap image, Sign sign) {
        this.image = image;
        this.sign = sign;
    }
}
