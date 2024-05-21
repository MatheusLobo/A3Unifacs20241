package org.sample.LojaA3;

public class PriceInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PriceInvalidException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}