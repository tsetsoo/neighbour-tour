package com.tsvetelin.interview.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountriesInfoResponse {

    private List<String> borders;
    private List<Currency> currencies;

    public CountriesInfoResponse() {
    }

    public List<String> getBorders() {
        return borders;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return "CountriesInfoResponse{" +
                "borders=" + borders +
                ", currencies=" + currencies +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Currency {
        private String code;

        public Currency(){
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "Currency{" +
                    "code='" + code + '\'' +
                    '}';
        }
    }
}


