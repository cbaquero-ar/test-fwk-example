package com.carolinabaquero.test.utils.pojo;

import com.opencsv.bean.CsvBindByName;

public class LoginData {

    @CsvBindByName(column = "start ip")
    private String startIp;

    @CsvBindByName(column = "end ip")
    private String endIp;

    @CsvBindByName(column = "country code")
    private String countryCode;

    @CsvBindByName
    private String country;

    //  getters, setters, toString
}