package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/unique")
    public List<CountryDto> getUniqueCountries() {
        return countryService.uniqueCountries();
    }

}
