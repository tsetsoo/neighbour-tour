package com.tsvetelin.interview.service;

import com.tsvetelin.interview.controller.TourRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    private static final String TEST_STARTING_COUNTRY = "BG";
    private static final String TEST_STARTING_CURRENCY = "BGN";
    private static final String NEIGHBOUR_ONE = "GER";
    private static final String NEIGHBOUR_TWO = "USA";
    private static final List<String> TEST_NEIGHBOURING_COUNTRIES = List.of(NEIGHBOUR_ONE, NEIGHBOUR_TWO);

    @Mock
    private CountryInfoProvider countryInfoProvider;

    @Mock
    private ExchangeRateProvider exchangeRateProvider;

    private TourService tourService;

    @BeforeEach
    public void beforeEach() {
        tourService = new TourService(countryInfoProvider, exchangeRateProvider);
    }

    @Test
    @DisplayName("2 neighbours, 2 currencies and budget for one tour")
    public void testSuccessOneTour() {
        mockTwoCountriesTwoCurrencies();

        var response = tourService.calculateTours(testTravelRequest(1000.0, 300.0));

        Assertions.assertEquals(1, response.getTours());
        Assertions.assertEquals(400.0, response.getBudgetLeft());
        Assertions.assertEquals(150.0, response.getCurrenciesToPurchase().get("EUR"));
        Assertions.assertEquals(180.0, response.getCurrenciesToPurchase().get("USD"));
    }

    @Test
    @DisplayName("2 neighbours, 2 currencies and budget for two tours, testing also that currencies to purchase grows proportionally with tours")
    public void testSuccessTwoTours() {
        mockTwoCountriesTwoCurrencies();

        var response = tourService.calculateTours(testTravelRequest(1300.0, 300.0));

        Assertions.assertEquals(2, response.getTours());
        Assertions.assertEquals(100.0, response.getBudgetLeft());
        Assertions.assertEquals(300.0, response.getCurrenciesToPurchase().get("EUR"));
        Assertions.assertEquals(360.0, response.getCurrenciesToPurchase().get("USD"));
    }

    @Test
    @DisplayName("2 neighbours, 2 currencies and budget for two tours, testing round with 2 decimal places")
    public void testSuccessTwoToursRoundTwoDecimals() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doReturn("EUR").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);
        Mockito.doReturn("USD").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_TWO);
        Mockito.doReturn(Map.of(
                "EUR", 0.554234,
                "USD", 0.603234031
        )).when(exchangeRateProvider).ratesFromTo(TEST_STARTING_CURRENCY, Set.of("EUR", "USD"));


        var response = tourService.calculateTours(testTravelRequest(1300.0, 300.0));

        Assertions.assertEquals(2, response.getTours());
        Assertions.assertEquals(100.0, response.getBudgetLeft());
        Assertions.assertEquals(332.54, response.getCurrenciesToPurchase().get("EUR"));
        Assertions.assertEquals(361.94, response.getCurrenciesToPurchase().get("USD"));
    }

    @Test
    @DisplayName("2 neighbours, 1 currency and budget for two tours, testing also that currencies to purchase grows proportionally with countries to use the currency for")
    public void testSuccessTwoToursOneCurrency() {
        mockTwoCountriesOneCurrency();

        var response = tourService.calculateTours(testTravelRequest(1300.0, 300.0));

        Assertions.assertEquals(2, response.getTours());
        Assertions.assertEquals(100.0, response.getBudgetLeft());
        Assertions.assertEquals(600.0, response.getCurrenciesToPurchase().get("EUR"));
    }

    @Test
    @DisplayName("2 neighbours, 1 currency and 1 identical to starting currency and budget for two tours, testing only foreign currency is bought")
    public void testSuccessTwoToursOneCurrencyIdenticalToStarting() {
        mockTwoCountriesTwoCurrenciesOneIsIdenticalToStarting();

        var response = tourService.calculateTours(testTravelRequest(1300.0, 300.0));

        Assertions.assertEquals(2, response.getTours());
        Assertions.assertEquals(100.0, response.getBudgetLeft());
        Assertions.assertEquals(300.0, response.getCurrenciesToPurchase().get("EUR"));
    }

    @Test
    @DisplayName("2 neighbours, 2 currencies and not enough budget")
    public void testNotEnoughMoneyForTour() {
        mockTwoCountriesTwoCurrencies();

        var response = tourService.calculateTours(testTravelRequest(500.0, 300.0));

        Assertions.assertEquals(0, response.getTours());
        Assertions.assertEquals(500.0, response.getBudgetLeft());
        Assertions.assertEquals(0.0, response.getCurrenciesToPurchase().get("EUR"));
        Assertions.assertEquals(0.0, response.getCurrenciesToPurchase().get("USD"));
    }

    @Test
    @DisplayName("Country info provider error neighbour")
    public void testCountryInfoProviderErrorNeighbour() {
        Mockito.doThrow(CountryInfoProviderException.class).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);

        Assertions.assertThrows(CountryInfoProviderException.class, () -> tourService.calculateTours(testTravelRequest(500.0, 300.0)));
    }

    @Test
    @DisplayName("Country info provider error currency")
    public void testCountryInfoProviderErrorCurrency() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doThrow(CountryInfoProviderException.class).when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);

        Assertions.assertThrows(CountryInfoProviderException.class, () -> tourService.calculateTours(testTravelRequest(500.0, 300.0)));
    }

    @Test
    @DisplayName("Country not found neighbour")
    public void testCountryNotFoundNeighbour() {
        Mockito.doThrow(CountryNotFoundException.class).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);

        Assertions.assertThrows(CountryNotFoundException.class, () -> tourService.calculateTours(testTravelRequest(500.0, 300.0)));
    }

    @Test
    @DisplayName("Country not found currency")
    public void testCountryNotFoundCurrency() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doThrow(CountryNotFoundException.class).when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);

        Assertions.assertThrows(CountryNotFoundException.class, () -> tourService.calculateTours(testTravelRequest(500.0, 300.0)));
    }


    @Test
    @DisplayName("Exchange rate provider error")
    public void testExchangeRateProviderError() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doReturn("EUR").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);
        Mockito.doReturn("USD").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_TWO);
        Mockito.doThrow(ExchangeRateProviderException.class).when(exchangeRateProvider).ratesFromTo(TEST_STARTING_CURRENCY, Set.of("EUR", "USD"));

        Assertions.assertThrows(ExchangeRateProviderException.class, () -> tourService.calculateTours(testTravelRequest(500.0, 300.0)));
    }

    private void mockTwoCountriesTwoCurrencies() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doReturn("EUR").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);
        Mockito.doReturn("USD").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_TWO);
        Mockito.doReturn(Map.of(
                "EUR", 0.5,
                "USD", 0.6
        )).when(exchangeRateProvider).ratesFromTo(TEST_STARTING_CURRENCY, Set.of("EUR", "USD"));
    }

    private void mockTwoCountriesTwoCurrenciesOneIsIdenticalToStarting() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doReturn("EUR").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);
        Mockito.doReturn(TEST_STARTING_CURRENCY).when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_TWO);
        Mockito.doReturn(Map.of(
                "EUR", 0.5
        )).when(exchangeRateProvider).ratesFromTo(TEST_STARTING_CURRENCY, Set.of("EUR"));
    }

    private void mockTwoCountriesOneCurrency() {
        Mockito.doReturn(TEST_NEIGHBOURING_COUNTRIES).when(countryInfoProvider).getNeighboringCountriesOf(TEST_STARTING_COUNTRY);
        Mockito.doReturn("EUR").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_ONE);
        Mockito.doReturn("EUR").when(countryInfoProvider).getCurrencyOf(NEIGHBOUR_TWO);
        Mockito.doReturn(Map.of(
                "EUR", 0.5
        )).when(exchangeRateProvider).ratesFromTo(TEST_STARTING_CURRENCY, Set.of("EUR"));
    }

    private TourRequest testTravelRequest(double totalBudget, double budgetPerCountry) {
        return new TourRequest(TEST_STARTING_COUNTRY, budgetPerCountry, totalBudget, TEST_STARTING_CURRENCY);
    }
}
