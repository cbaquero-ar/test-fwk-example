package com.carolinabaquero.test.core.exceptions;

public class ElementNotClickableException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public ElementNotClickableException() {

    }


    public ElementNotClickableException(final String element) {

        super("Could not click in element " + element + ", is not active.");
    }

}
