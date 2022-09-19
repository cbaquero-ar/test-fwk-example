package com.carolinabaquero.test.core.exceptions;

/**
 * @author cbaquero
 */
public class FailedLoginException extends Exception {
    public FailedLoginException(String message) {
        super("Login failed. " + message);
    }
}