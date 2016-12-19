package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2016-12-06.
 */
public class Address {
    public String province;
    public String county;
    public String town;
    public String street;
    public String firstNumber;
    public String secondNumber;
    public String village;
    public String houseNumber;

    public Address(String province, String county, String town, String street, String firstNumber, String secondNumber, String village, String houseNumber) {
        this.province = province;
        this.county = county;
        this.town = town;
        this.street = street;
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.village = village;
        this.houseNumber = houseNumber;
    }

}
