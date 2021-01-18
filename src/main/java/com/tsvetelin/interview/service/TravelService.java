package com.tsvetelin.interview.service;

import com.tsvetelin.interview.controller.TravelRequest;
import com.tsvetelin.interview.controller.TravelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TravelService {

    private final CountryInfoProvider countryInfoProvider;
    private final ExchangeRateProvider exchangeRateProvider;

    @Autowired
    public TravelService(CountryInfoProvider countryInfoProvider, ExchangeRateProvider exchangeRateProvider) {
        this.countryInfoProvider = countryInfoProvider;
        this.exchangeRateProvider = exchangeRateProvider;
    }

    public TravelResponse calculateTours(TravelRequest travelRequest) {
        List<String> neighboringCountries = countryInfoProvider.getNeighboringCountriesOf(travelRequest.getStartingCountry());
        double budgetPerTour = neighboringCountries.size() * travelRequest.getBudgetPerCountry();
        var tours = ((int) (travelRequest.getTotalBudget() / budgetPerTour));
        var budgetLeft = tours > 0 ? travelRequest.getTotalBudget() - budgetPerTour * tours : travelRequest.getTotalBudget();
        var neighboringCountriesCurrenciesTotals = foreignCurrenciesTotals(neighboringCountries, travelRequest.getStartingCurrency(), travelRequest.getBudgetPerCountry(), tours);

        return new TravelResponse(tours, budgetLeft, neighboringCountriesCurrenciesTotals);
    }

    private Map<String, Double> foreignCurrenciesTotals(List<String> neighboringCountries, String startingCurrency, double budgetPerCountry, int tours) throws CountryInfoProviderException {
        var currenciesToExchangeToTimesToExchange = neighboringCountries
                .stream()
                .map(countryInfoProvider::getCurrencyOf)
                .filter(currency -> !currency.equals(startingCurrency))
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        var exchangeRates = exchangeRateProvider.ratesFromTo(startingCurrency, currenciesToExchangeToTimesToExchange.keySet());

        return exchangeRates.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue() * budgetPerCountry * tours * currenciesToExchangeToTimesToExchange.get(e.getKey()))
                );
    }
}
