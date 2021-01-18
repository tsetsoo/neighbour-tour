package com.tsvetelin.interview.service;

public class CountryNotFoundException extends CountryInfoProviderException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}
