package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.services.CountryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping()
    public CountriesResponse getAllCountries(
            @RequestParam(value = "per_page", defaultValue = "25") @NotBlank int perPage,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return countryService.getAll(perPage, page);
    }
}
