package com.mjict.signboardsurvey.model.ui;

import android.graphics.Bitmap;

/**
 * Created by Junseo on 2016-12-26.
 */
public class DemolishedSign {
    public boolean labelVisible;
    public int labelColor;
    public String labelText;
    public SignStatus status;
    public String content;
    public String light;
    public String type;
    public String date;
    public String location;
    public String result;
    public String size;
    public Bitmap signImage;
    public Bitmap demolishedImage;

    public DemolishedSign(boolean labelVisible, int labelColor, String labelText, SignStatus status,
                          String content, String light, String type, String date,
                          String location, String result, String size, Bitmap signImage,
                          Bitmap demolishedImage) {
        this.labelVisible = labelVisible;
        this.labelColor = labelColor;
        this.labelText = labelText;
        this.status = status;
        this.content = content;
        this.light = light;
        this.type = type;
        this.date = date;
        this.location = location;
        this.result = result;
        this.size = size;
        this.signImage = signImage;
        this.demolishedImage = demolishedImage;
    }
}
