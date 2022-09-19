package com.carolinabaquero.test.core.exceptions;

public class MissingPropertiesConfigFile extends RuntimeException{
    public MissingPropertiesConfigFile(String propFile){

    super("Configuration file " + propFile + "is missing.Please, check that the file exists and has the correct values");
        }

}
