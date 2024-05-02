package org.sample.LojaA3;

public class SkuInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SkuInvalidoException(String message) {
        super(message);
    }

    
    public String getMessage() {
        return super.getMessage();
    }
}