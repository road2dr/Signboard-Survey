package com.mjict.signboardsurvey.model;

import java.util.List;

/**
 * Created by Junseo on 2016-12-25.
 */
public class ShopWithSign {
    public int index;
    public List<Shop> shops;
    public List<Sign> signs;

    public ShopWithSign(int index, List<Shop> shops, List<Sign> signs) {
        this.index = index;
        this.shops = shops;
        this.signs = signs;
    }
}
