package org.sample.lojaa3;

public class StockUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StockUnavailableException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}