package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2016-11-10.
 */
public class RecentSign {
    public int img;
    public String shopName;
    public String type;
    public String result;

    public RecentSign(int img, String shopName, String type, String result) {
        this.img = img;
        this.shopName = shopName;
        this.type = type;
        this.result = result;
    }
}
