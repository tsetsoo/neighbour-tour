package com.tsvetelin.interview.controller;

import com.tsvetelin.interview.service.CountryNotFoundException;
import com.tsvetelin.interview.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class TravelController {

    private final TravelService travelService;

    @Autowired
    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/travel")
    public TravelResponse index(@RequestBody TravelRequest travelRequest) {
        return travelService.calculateTours(travelRequest);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CountryNotFoundException.class})
    public @ResponseBody
    String countryNotFoundException(CountryNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public @ResponseBody
    String genericException(Exception exception) {
        return exception.getMessage();
    }

}