package com.tsvetelin.interview.service;

import com.tsvetelin.interview.controller.TourRequest;
import com.tsvetelin.interview.controller.TourResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TourService {

    private final CountryInfoProvider countryInfoProvider;
    private final ExchangeRateProvider exchangeRateProvider;

    @Autowired
    public TourService(CountryInfoProvider countryInfoProvider, ExchangeRateProvider exchangeRateProvider) {
        this.countryInfoProvider = countryInfoProvider;
        this.exchangeRateProvider = exchangeRateProvider;
    }

    public TourResponse calculateTours(TourRequest tourRequest) {
        List<String> neighboringCountries = countryInfoProvider.getNeighboringCountriesOf(tourRequest.getStartingCountry());
        double budgetPerTour = neighboringCountries.size() * tourRequest.getBudgetPerCountry();
        var tours = ((int) (tourRequest.getTotalBudget() / budgetPerTour));
        var budgetLeft = simpleRoundTwoDecimalPlaces(tours > 0 ? tourRequest.getTotalBudget() - budgetPerTour * tours : tourRequest.getTotalBudget());
        var neighboringCountriesCurrenciesTotals = foreignCurrenciesTotals(neighboringCountries, tourRequest.getStartingCurrency(), tourRequest.getBudgetPerCountry(), tours);

        return new TourResponse(tours, budgetLeft, neighboringCountriesCurrenciesTotals);
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
                        e -> simpleRoundTwoDecimalPlaces(e.getValue() * budgetPerCountry * tours * currenciesToExchangeToTimesToExchange.get(e.getKey())))
                );
    }


    private double simpleRoundTwoDecimalPlaces(double toRound) {
        toRound = toRound * 100;
        toRound = Math.round(toRound);
        toRound = toRound / 100;
        return toRound;
    }

}
