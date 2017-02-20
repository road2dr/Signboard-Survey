package com.mjict.signboardsurvey.model.ui;

import java.util.List;

/**
 * Created by Junseo on 2017-02-07.
 */
public class ShopAndSign {
        public ShopInfo shop;
        public List<SignInfo> signs;

        public ShopAndSign(ShopInfo shop, List<SignInfo> signs) {
            this.shop = shop;
            this.signs = signs;
        }
}
