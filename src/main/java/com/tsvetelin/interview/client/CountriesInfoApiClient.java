package com.tsvetelin.interview.client;

import com.tsvetelin.interview.service.CountryInfoProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountriesInfoApiClient implements CountryInfoProvider {
    @Override
    public List<String> getNeighboringCountriesOf(String startingCountry) {
        return List.of("USA", "GER");
    }

    @Override
    public String getCurrencyOf(String country) {
        if(country.equals("USA")) {
            return "USD";
        }
        return "EUR";
    }
}
