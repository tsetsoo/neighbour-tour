package com.tsvetelin.interview.client;

import java.util.Map;

public class ExchangeRateApiResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;

    public ExchangeRateApiResponse() {
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
