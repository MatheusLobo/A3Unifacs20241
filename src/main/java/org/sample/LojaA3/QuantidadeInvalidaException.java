package org.sample.LojaA3;

public class QuantidadeInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QuantidadeInvalidaException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}