package com.tsvetelin.interview.controller;

import com.tsvetelin.interview.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;

@RestController
public class TravelController {

    private final TravelService travelService;

    @Autowired
    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/travel") //TODO rename endpoint
    public TravelResponse index(@RequestBody TravelRequest travelRequest) {
        return travelService.calculateTours(travelRequest);
    }

}