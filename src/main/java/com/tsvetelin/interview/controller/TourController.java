package com.tsvetelin.interview.controller;

import com.tsvetelin.interview.service.CountryNotFoundException;
import com.tsvetelin.interview.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class TourController {

    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @PostMapping("/tour")
    public TourResponse index(@RequestBody TourRequest tourRequest) {
        return tourService.calculateTours(tourRequest);
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