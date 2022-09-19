package com.carolinabaquero.test.core.exceptions;

public class InvalidLoginException extends Throwable {
    public InvalidLoginException(String message) {
        super("Invalid login form fields. "+message);
    }
}
