package com.tsvetelin.interview.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsvetelin.interview.service.CountryInfoProvider;
import com.tsvetelin.interview.service.CountryInfoProviderException;
import com.tsvetelin.interview.service.CountryNotFoundException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CountriesInfoApiClient implements CountryInfoProvider {

    private static final String BASE_URL = "https://restcountries.eu/rest/v2";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<String> getNeighboringCountriesOf(String startingCountry) {
        return retrieveCountryInfo(startingCountry).getBorders();
    }

    private CountriesInfoResponse retrieveCountryInfo(String countryCode) {
        OkHttpClient client = new OkHttpClient();

        var request = new Request.Builder()
                .url(BASE_URL + "/alpha/" + countryCode + "?fields=borders;currencies")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 404) {
                throw new CountryNotFoundException("Country with code [" + countryCode + "] does not exist");
            }

            if (response.code() != 200) {
                throw new CountryInfoProviderException("Unexpected error while retrieving country info");
            }

            return mapper.readValue(response.body().byteStream(), CountriesInfoResponse.class);
        } catch (IOException e) {
            throw new CountryInfoProviderException("Unexpected error while retrieving country info", e);
        }
    }

    @Override
    public String getCurrencyOf(String country) {
        return retrieveCountryInfo(country).getCurrencies().get(0).getCode();
    }
}
