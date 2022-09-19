package com.carolinabaquero.test.core.exceptions;

/**
 * @author cbaquero
 */
public class WrongPropertiesFormatException extends RuntimeException {

    private static final long serialVersionUID = 5191948728342357242L;


    public WrongPropertiesFormatException(final String propFile, final String message) {

        super(propFile + " is not correctly defined. " + message);
    }
}
