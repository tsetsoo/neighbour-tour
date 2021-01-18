package com.tsvetelin.interview.controller;

import java.util.Map;

public class TourResponse {
    private int tours;
    private double budgetLeft;
    private Map<String, Double> currenciesToPurchase;

    public TourResponse(int tours, double budgetLeft, Map<String, Double> currenciesToPurchase) {
        this.tours = tours;
        this.budgetLeft = budgetLeft;
        this.currenciesToPurchase = currenciesToPurchase;
    }

    public int getTours() {
        return tours;
    }

    public void setTours(int tours) {
        this.tours = tours;
    }

    public double getBudgetLeft() {
        return budgetLeft;
    }

    public void setBudgetLeft(double budgetLeft) {
        this.budgetLeft = budgetLeft;
    }

    public Map<String, Double> getCurrenciesToPurchase() {
        return currenciesToPurchase;
    }

    public void setCurrenciesToPurchase(Map<String, Double> currenciesToPurchase) {
        this.currenciesToPurchase = currenciesToPurchase;
    }

}
