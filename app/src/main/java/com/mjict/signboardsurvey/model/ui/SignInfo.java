package com.mjict.signboardsurvey.model.ui;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-11-15.
 */
public class SignInfo {
    public Bitmap image;
    public String content;
    public String type;
    public String size;
    public String status;
    public String light;
    public String location;
    public String result;
    public int labelColor;
    public boolean labelVisible;
    public boolean permitted;

    public SignInfo(Bitmap image, String content, String type, String size, String status, String light, String location,
                    String result, int labelColor, boolean labelVisible, boolean permitted) {
        this.image = image;
        this.content = content;
        this.type = type;
        this.size = size;
        this.status = status;
        this.light = light;
        this.location = location;
        this.result = result;
        this.labelColor = labelColor;
        this.labelVisible = labelVisible;
        this.permitted = permitted;
    }
}
