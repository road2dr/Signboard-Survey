package com.mjict.signboardsurvey.model.ui;

import android.graphics.Bitmap;

import com.mjict.signboardsurvey.model.BitmapSign;
import com.mjict.signboardsurvey.model.Sign;

/**
 * Created by Junseo on 2016-12-01.
 */
public class RecentSign extends BitmapSign {
    public String content;
    public String type;
    public String result;

    public RecentSign(Bitmap image, Sign sign, String content, String type, String result) {
        super(image, sign);

        this.content = content;
        this.type = type;
        this.result = result;
    }
}
