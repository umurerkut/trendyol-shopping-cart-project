package com.trendyol.shoppingcart.core.exception;

public class InvalidValueException extends IllegalArgumentException {

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
