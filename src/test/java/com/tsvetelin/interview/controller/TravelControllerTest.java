package com.tsvetelin.interview.controller;

import com.tsvetelin.interview.service.TravelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class TravelControllerTest {

    private TravelController travelController;

    @Mock
    private TravelService travelService;

    @BeforeEach
    public void beforeEach() {
        travelController = new TravelController(travelService);
    }

    @Test
    public void testSuccessful() {
        var mockResponse = new TravelResponse(1, 100.0, Map.of(
                "USD", 50.0,
                "BGN", 41.4
        ));
        Mockito.doReturn(mockResponse).when(travelService).calculateTours(Mockito.any(TravelRequest.class));

        var actualResponse = travelController.index(new TravelRequest("BG", 100.0, 1000.0, "EUR"));
        Assertions.assertEquals(mockResponse, actualResponse);
    }
}
