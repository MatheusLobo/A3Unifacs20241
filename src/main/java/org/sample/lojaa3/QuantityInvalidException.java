package org.sample.lojaa3;

public class QuantityInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public QuantityInvalidException(String message) {
        super(message);
    }
}