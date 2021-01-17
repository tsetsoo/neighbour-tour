package com.tsvetelin.interview.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsvetelin.interview.service.ExchangeRateProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ExchangeRateApiClient implements ExchangeRateProvider {

    private static final String EXCHANGE_RATE_API_URL_FORMAT = "https://api.exchangeratesapi.io/latest?base=%s&symbols=%s";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, Double> ratesFromTo(String startingCurrency, List<String> currenciesNeedingExchange) {
        OkHttpClient client = new OkHttpClient();

        var request = new Request.Builder()
                .url(String.format(EXCHANGE_RATE_API_URL_FORMAT, startingCurrency, StringUtils.collectionToCommaDelimitedString(currenciesNeedingExchange)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            var responseBody = mapper.readValue(response.body().byteStream(), ExchangeRateApiResponse.class);
            return responseBody.getRates();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
