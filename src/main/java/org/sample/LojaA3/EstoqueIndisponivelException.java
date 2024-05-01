package org.sample.LojaA3;

public class EstoqueIndisponivelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EstoqueIndisponivelException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}