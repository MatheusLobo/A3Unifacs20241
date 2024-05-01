package org.sample.LojaA3;

public class PrecoInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PrecoInvalidoException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}