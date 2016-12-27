package com.mjict.signboardsurvey.model;

import java.util.List;

/**
 * Created by Junseo on 2016-12-25.
 */
public class ShopAndSign {
    public int index;
    public List<Shop> shops;
    public List<Sign> signs;

    public ShopAndSign(int index, List<Shop> shops, List<Sign> signs) {
        this.index = index;
        this.shops = shops;
        this.signs = signs;
    }
}
