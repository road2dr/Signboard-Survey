package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-18.
 */
public enum Category {
    SCOPE,
    MULTI_EQUATION,
    SINGLE_EQUATION,
    NONE;


    public static Category toCategory(String str) {
        Category category = NONE;

        if(str.equalsIgnoreCase("scope"))
            category = SCOPE;
        else if(str.equalsIgnoreCase("multi_equation"))
            category = MULTI_EQUATION;
        else if(str.equalsIgnoreCase("single_equation"))
            category = SINGLE_EQUATION;
        else
            category = NONE;

        return category;
    }
}
