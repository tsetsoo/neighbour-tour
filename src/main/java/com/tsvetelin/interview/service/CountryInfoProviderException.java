package com.tsvetelin.interview.service;

public class CountryInfoProviderException extends RuntimeException {
    public CountryInfoProviderException(String message) {
        super(message);
    }

    public CountryInfoProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
