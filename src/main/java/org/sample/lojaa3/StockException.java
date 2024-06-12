package org.sample.lojaa3;

public class StockException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StockException(String message) {
        super(message);
    }
}
