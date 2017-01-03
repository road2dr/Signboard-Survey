package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2016-12-30.
 */
public class DetailSign {
    public Sign sign;
    public Shop shop;
    public Building building;

    public DetailSign(Sign sign, Shop shop, Building building) {
        this.sign = sign;
        this.shop = shop;
        this.building = building;
    }
}
