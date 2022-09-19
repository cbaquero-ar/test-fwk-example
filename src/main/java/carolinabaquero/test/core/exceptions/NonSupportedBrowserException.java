package com.carolinabaquero.test.core.exceptions;

public class NonSupportedBrowserException extends Throwable {
    public NonSupportedBrowserException(String browser) {
        super("Browser '"+browser+"' is not supported by the test framework.");
    }
}
