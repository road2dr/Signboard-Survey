package com.mjict.signboardsurvey.util;

/**
 * Created by Junseo on 2016-10-31.
 */
public class NoInputException extends Exception {
    private int fieldStringResId;

    public NoInputException(int id) {
        fieldStringResId = id;
    }

    public int getFieldStringResId() {
        return fieldStringResId;
    }
}
