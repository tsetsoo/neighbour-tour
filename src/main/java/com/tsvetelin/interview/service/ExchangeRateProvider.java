package com.tsvetelin.interview.service;

import java.util.List;
import java.util.Map;

public interface ExchangeRateProvider {
    Map<String, Double> ratesFromTo(String startingCurrency, List<String> currenciesNeedingExchange);
}
