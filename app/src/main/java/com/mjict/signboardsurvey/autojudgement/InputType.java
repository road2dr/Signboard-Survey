package com.mjict.signboardsurvey.autojudgement;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Junseo on 2017-01-21.
 */
public enum InputType {
    SCOPE_INSTALL_FLOOR_COUNT("scope_install_floor_count"),
    SCOPE_HORIZONTAL_SIGN_COUNT("scope_horizontal_sign_count"),
    SCOPE_WIDTH("scope_width"),
    SCOPE_AREA("scope_area"),
    SCOPE_LENGTH("scope_length"),
    SCOPE_INSTALL_HEIGHT("scope_install_height"),
    SCOPE_PROJECTED_SIGN_COUNT("scope_projected_sign_count"),
    SCOPE_INSTALL_HEIGHT_TOP("scope_install_height_top"),
    SCOPE_PILLAR_SIGN_COUNT("scope_pillar_sign_count"),
    SCOPE_TOTAL_FLOOR_COUNT("scope_total_floor_count"),
    SCOPE_ROOFTOP_SIGN_COUNT("scope_rooftop_sign_count"),
    EQUATION_LIGHT("multi_equation_light"),
    EQUATION_TOTAL_FLOOR_COUNT("multi_equation_install_floor_count"),
    EQUATION_INSTALL_SIDE("multi_equation_install_side"),
    EQUATION_INTERSECTION("single_equation_intersection"),
    NONE("");

    private String name;

    private InputType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static final Map<String,InputType> enumMap;

    static {
        Map<String,InputType> map = new ConcurrentHashMap<String,InputType>();
        for (InputType instance : InputType.values()) {
            map.put(instance.getName(),instance);
        }
        enumMap = Collections.unmodifiableMap(map);
    }

    public static InputType get (String name) {
        return enumMap.get(name);
    }

//    public InputType toInputType(String typeName) {
//        int index = -1;
//
//        for(int i=0; i<name.length; i++) {
//            if(name[i].equalsIgnoreCase(typeName)) {
//                index = i;
//                break;
//            }
//        }
//
//        if(index == -1)
//            return NONE;
//
//        else
//            return InputType.va
//
//    }
}
