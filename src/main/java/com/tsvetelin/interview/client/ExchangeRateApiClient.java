package com.tsvetelin.interview.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsvetelin.interview.service.ExchangeRateProvider;
import com.tsvetelin.interview.service.ExchangeRateProviderException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
public class ExchangeRateApiClient implements ExchangeRateProvider {

    private static final String EXCHANGE_RATE_API_URL_FORMAT = "https://api.exchangerate.host/latest?base=%s&symbols=%s";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, Double> ratesFromTo(String startingCurrency, Set<String> currenciesNeedingExchange) {
        OkHttpClient client = new OkHttpClient();

        var request = new Request.Builder()
                .url(String.format(EXCHANGE_RATE_API_URL_FORMAT, startingCurrency, StringUtils.collectionToCommaDelimitedString(currenciesNeedingExchange)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new ExchangeRateProviderException("Unexpected status code while retrieving exchange rates");
            }

            return mapper.readValue(response.body().byteStream(), ExchangeRateApiResponse.class).getRates();
        } catch (IOException e) {
            throw new ExchangeRateProviderException("Unexpected error while retrieving exchange rates", e);
        }
    }

}
