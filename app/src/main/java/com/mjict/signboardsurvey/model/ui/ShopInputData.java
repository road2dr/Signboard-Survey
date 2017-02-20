package com.mjict.signboardsurvey.model.ui;

/**
 * Created by Junseo on 2016-08-18.
 */
public class ShopInputData {
    public String name;
    public String phone;
    public String representative;
    public String category;
    public String stats;
//    public int floorCount;

    public ShopInputData(String name, String phone, String representative, String category, String status /*, int floorCount*/) {
        this.name = name;
        this.phone = phone;
        this.representative = representative;
        this.category = category;
        this.stats = status;
//        this.status = status;
//        this.floorCount = floorCount;
    }


}
