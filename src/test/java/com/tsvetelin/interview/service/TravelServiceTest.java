package com.tsvetelin.interview.service;

import com.tsvetelin.interview.controller.TravelRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TravelServiceTest {

    private static final String TEST_STARTING_COUNTRY = "BG";
    private static final String TEST_STARTING_CURRENCY = "BGN";
    private static final String NEIGHBOUR_ONE = "GER";
    private static final String NEIGHBOUR_TWO = "USA";

    private static final List<String> TEST_NEIGHBOURING_COUNTRIES = List.of(NEIGHBOUR_ONE, NEIGHBOUR_TWO);

    @Mock
    private CountryInfoProvider countryInfoProvider;

    @Mock
    private ExchangeRateProvider exchangeRateProvider;

    private TravelService travelService;

    @BeforeEach
    public void beforeEach() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doReturn("EUR").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);
        Mockito.doReturn("USD").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_TWO);
        Mockito.doReturn(Map.of(
                "EUR", 0.5,
                "USD", 0.6
        )).when(exchangeRateProvider).ratesFromTo(TEST_STARTING_CURRENCY, Set.of("EUR", "USD"));
        travelService = new TravelService(countryInfoProvider, exchangeRateProvider);
    }

    @Test
    public void testSuccess() {
        var response = travelService.calculateTours(testTravelRequest(1000.0, 300.0));

        Assertions.assertEquals(1, response.getTours());
        Assertions.assertEquals(400.0, response.getBudgetLeft());
        Assertions.assertEquals(150.0, response.getCurrenciesToPurchase().get("EUR"));
        Assertions.assertEquals(180.0, response.getCurrenciesToPurchase().get("USD"));
    }

    private TravelRequest testTravelRequest(double totalBudget, double budgetPerCountry) {
        return new TravelRequest(TEST_STARTING_COUNTRY, budgetPerCountry, totalBudget, TEST_STARTING_CURRENCY);
    }
}
