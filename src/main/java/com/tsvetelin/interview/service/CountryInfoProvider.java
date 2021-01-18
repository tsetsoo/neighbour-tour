package com.tsvetelin.interview.service;

import java.util.List;

public interface CountryInfoProvider {
    List<String> getNeighboringCountriesOf(String startingCountry) throws CountryInfoProviderException;

    String getCurrencyOf(String country) throws CountryInfoProviderException;
}
