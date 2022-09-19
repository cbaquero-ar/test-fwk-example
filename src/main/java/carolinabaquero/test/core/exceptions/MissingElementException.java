package com.carolinabaquero.test.core.exceptions;


/**
 * @author cbaquero
 */
public class MissingElementException extends RuntimeException {

    private static final long serialVersionUID = -7142171900530805011L;


    /**
     * @param element the element being expected that was missing
     */
    public MissingElementException(final String element) {

        super(element + " is missing.");
    }


    /**
     * Sets a different message for the exception. Indicates just location and
     * exception particular message.
     *
     * @param elementLocation  the location or [page object + method] where the element is being expected
     * @param exceptionMessage an extra message to include on the exception
     */
    public MissingElementException(final String elementLocation, final String exceptionMessage) {

        super("Some element in " + elementLocation + " is missing. Exception was: \n"
                + exceptionMessage);
    }


    /**
     * Use this one for better reporting
     */
    public MissingElementException(final String elementLocation, final String exceptionMessage,
                                   final Throwable cause) {

        super("Some element in " + elementLocation + " is missing. Exception was: \n"
                + exceptionMessage, cause);
    }


    public MissingElementException(final String elementLocation, final Throwable cause) {

        super("Some element in " + elementLocation
                + " is missing. Check the log for exception stack trace.\n", cause);
    }

}
