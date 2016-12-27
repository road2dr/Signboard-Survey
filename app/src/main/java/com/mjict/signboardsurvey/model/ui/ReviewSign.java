package com.mjict.signboardsurvey.model.ui;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-09-08.
 */
public class ReviewSign {

    public Bitmap signImage;
    public String status;
    public String type;
    public String content;
    public String lightType;
    public String result;
    public String size;
    public String location;
    public String date;

//    public Sign sign;
//    public Shop shop;

    public ReviewSign(Bitmap signImage, String status, String type, String content,
                      String lightType, String result, String size, String location, String date) {
        this.signImage = signImage;
        this.status = status;
        this.type = type;
        this.content = content;
        this.lightType = lightType;
        this.result = result;
        this.size = size;
        this.location = location;
        this.date = date;
    }
}
