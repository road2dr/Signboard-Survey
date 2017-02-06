package com.mjict.signboardsurvey.model.ui;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopInfo {
    public String name;
    public String phone;
    public String category;
    public boolean demolished;

    public ShopInfo(String n, String p, String c, boolean d) {
        name = n;
        phone = p;
        category = c;
        demolished = d;
    }
}
