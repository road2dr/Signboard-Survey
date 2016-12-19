package com.mjict.signboardsurvey.model;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-12-06.
 */
public class IndexBitmap {

    public int index;
    public Bitmap image;

    public IndexBitmap(int idx, Bitmap img) {
        index = idx;
        image = img;
    }
}
