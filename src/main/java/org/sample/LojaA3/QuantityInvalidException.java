package org.sample.LojaA3;

public class QuantityInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QuantityInvalidException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}