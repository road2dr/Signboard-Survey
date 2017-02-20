package com.mjict.signboardsurvey.model;

import java.util.List;

/**
 * Created by Junseo on 2016-08-03.
 */
public class ShopInformation {
    public Shop shop;
    public List<Sign> signs;

    public ShopInformation(Shop shop, List<Sign> signs) {
        this.shop = shop;
        this.signs = signs;
    }
}
