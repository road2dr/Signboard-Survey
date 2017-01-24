package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-18.
 */
public class Item {
    private String name;
    private int code;
    private String value;

    public Item() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
