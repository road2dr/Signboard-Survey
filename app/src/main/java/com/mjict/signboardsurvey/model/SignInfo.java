package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2016-11-15.
 */
public class SignInfo {
    public int image;
    public String content;
    public String size;
    public String status;
    public int light;
    public String location;
    public String result;

    public SignInfo(int image, String content, String size, String status, int light, String location, String result) {
        this.image = image;
        this.content = content;
        this.size = size;
        this.status = status;
        this.light = light;
        this.location = location;
        this.result = result;
    }
}
