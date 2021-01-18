package com.tsvetelin.interview.controller;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsvetelin.interview.service.CountryInfoProviderException;
import com.tsvetelin.interview.service.CountryNotFoundException;
import com.tsvetelin.interview.service.ExchangeRateProviderException;
import com.tsvetelin.interview.service.TravelService;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TravelController.class)
public class TravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelService travelService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void success() throws Exception {
        var request = new TravelRequest("DE", 100.0, 300.0, "EUR");
        var mockResponse = new TravelResponse(1, 100.0, Map.of(
                "USD", 50.0,
                "BGN", 41.4
        ));
        Mockito.doReturn(mockResponse).when(travelService).calculateTours(request);
        this.mockMvc.perform(post("/travel").content(mapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void countryNotFoundError() throws Exception {
        var request = new TravelRequest("DE", 100.0, 300.0, "EUR");
        var message = "Country does not exists";
        Mockito.doThrow(new CountryNotFoundException(message)).when(travelService).calculateTours(request);
        this.mockMvc.perform(post("/travel").content(mapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @Test
    public void countryInfoProviderError() throws Exception {
        var request = new TravelRequest("DE", 100.0, 300.0, "EUR");
        var message = "Country info provider error";
        Mockito.doThrow(new CountryInfoProviderException(message)).when(travelService).calculateTours(request);
        this.mockMvc.perform(post("/travel").content(mapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
                .andExpect(content().string(message));
    }

    @Test
    public void exchangeRateProviderError() throws Exception {
        var request = new TravelRequest("DE", 100.0, 300.0, "EUR");
        var message = "Exchange rate provider error";
        Mockito.doThrow(new ExchangeRateProviderException(message)).when(travelService).calculateTours(request);
        this.mockMvc.perform(post("/travel").content(mapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
                .andExpect(content().string(message));
    }
}