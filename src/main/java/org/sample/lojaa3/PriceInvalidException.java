package org.sample.lojaa3;


public class PriceInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PriceInvalidException(String message) {
        super(message);
    }
}
