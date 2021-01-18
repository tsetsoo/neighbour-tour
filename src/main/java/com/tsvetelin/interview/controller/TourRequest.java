package com.tsvetelin.interview.controller;

import java.util.Objects;

public class TourRequest {
    private String startingCountry;
    private double budgetPerCountry;
    private double totalBudget;
    private String startingCurrency;

    public TourRequest(String startingCountry, double budgetPerCountry, double totalBudget, String startingCurrency) {
        this.startingCountry = startingCountry;
        this.budgetPerCountry = budgetPerCountry;
        this.totalBudget = totalBudget;
        this.startingCurrency = startingCurrency;
    }

    public String getStartingCountry() {
        return startingCountry;
    }

    public void setStartingCountry(String startingCountry) {
        this.startingCountry = startingCountry;
    }

    public double getBudgetPerCountry() {
        return budgetPerCountry;
    }

    public void setBudgetPerCountry(double budgetPerCountry) {
        this.budgetPerCountry = budgetPerCountry;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public String getStartingCurrency() {
        return startingCurrency;
    }

    public void setStartingCurrency(String startingCurrency) {
        this.startingCurrency = startingCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TourRequest that = (TourRequest) o;
        return Double.compare(that.budgetPerCountry, budgetPerCountry) == 0 && Double.compare(that.totalBudget, totalBudget) == 0 && startingCountry.equals(that.startingCountry) && startingCurrency.equals(that.startingCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingCountry, budgetPerCountry, totalBudget, startingCurrency);
    }
}
