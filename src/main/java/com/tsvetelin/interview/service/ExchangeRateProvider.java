package com.tsvetelin.interview.service;

import java.util.Map;
import java.util.Set;

public interface ExchangeRateProvider {
    Map<String, Double> ratesFromTo(String startingCurrency, Set<String> currenciesNeedingExchange);
}
