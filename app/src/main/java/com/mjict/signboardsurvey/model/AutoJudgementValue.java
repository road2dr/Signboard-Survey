package com.mjict.signboardsurvey.model;

import com.mjict.signboardsurvey.autojudgement.InputType;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Junseo on 2017-01-22.
 */
public class AutoJudgementValue implements Serializable {

    private HashMap<InputType, Integer> values;

    public AutoJudgementValue() {
        values = new HashMap<>();
    }

    public void putValue(InputType type, int value) {
        values.put(type, value);
    }

    public Integer getValue(InputType type) {
        return values.get(type);
    }

//    public static String[] getInputValueNames() {
//        InputType[] types = InputType.values();
//        String[] names = new String[types.length];
//        for(int i=0; i<types.length; i++)
//            names[i] = types[i].getName();
//
//        return names;
//    }
}
