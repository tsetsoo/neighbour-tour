package com.tsvetelin.interview.controller;

public class TravelRequest {
    private String startingCountry;
    private double budgetPerCountry;
    private double totalBudget;
    private String startingCurrency;

    public TravelRequest(String startingCountry, double budgetPerCountry, double totalBudget, String startingCurrency) {
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
    public String toString() {
        return "TravelRequest{" +
                "startingCountry='" + startingCountry + '\'' +
                ", budgetPerCountry=" + budgetPerCountry +
                ", totalBudget=" + totalBudget +
                ", startingCurrency='" + startingCurrency + '\'' +
                '}';
    }
}
