package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2017-03-02.
 */
public class SignInformation {
    public Sign sign;
    public Shop shop;
    public Building building;

    public SignInformation(Sign s, Shop shop, Building b) {
        sign = s;
        this.shop = shop;
        building = b;
    }
}
