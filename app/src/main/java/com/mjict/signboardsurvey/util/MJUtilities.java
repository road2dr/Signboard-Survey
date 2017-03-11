package com.mjict.signboardsurvey.util;

import com.mjict.signboardsurvey.model.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-03-06.
 */
public class MJUtilities {

    public static final String[] FIXED_SIGN_TYPES = {   // 고정 광고물 코드
        "01", "02", "03", "16", "21", "23"
    };

    public static Setting[] filterFixedSignTypes(Setting[] settings) {

        if(settings == null)
            return null;

        List<Setting> filteredSettings = new ArrayList<>();

        for(int i=0; i<settings.length; i++) {
            Setting s = settings[i];
            if(isFixedSignType(s))
                filteredSettings.add(s);
        }

        Setting[] settingArray = new Setting[filteredSettings.size()];
        settingArray = filteredSettings.toArray(settingArray);

        return settingArray;
    }

    private static boolean isFixedSignType(Setting s) {
        boolean isFixedType = false;
        for(int i=0; i<FIXED_SIGN_TYPES.length; i++) {
            if(s.getCode().equals(FIXED_SIGN_TYPES[i])) {
                isFixedType = true;
                break;
            }
        }
        return isFixedType;
    }
}
