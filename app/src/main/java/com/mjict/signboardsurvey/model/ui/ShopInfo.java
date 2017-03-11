package com.mjict.signboardsurvey.model.ui;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopInfo {
    public String name;
    public String phone;
    public String category;
    public String representative;
    public boolean demolished;
    public boolean permitted;

    public ShopInfo(String n, String p, String c, String r, boolean d, boolean pe) {
        name = n;
        phone = p;
        category = c;
        demolished = d;
        representative = r;
        permitted = pe;
    }
}
