package com.mjict.signboardsurvey.util;

/**
 * Created by Junseo on 2016-09-27.
 */
public class WrongNumberScopeException extends Exception {

    int causeValue;

    public WrongNumberScopeException(int cause) {
        causeValue = cause;
    }

    public int getCauseValue() {
        return causeValue;
    }
}
