package org.sample.lojaa3;

public class SkuInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SkuInvalidException(String message) {
        super(message);
    }
}