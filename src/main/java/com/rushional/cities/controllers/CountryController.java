package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.services.CountryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("{countryId}/upload-flag")
    public CountryDto uploadFlag(
            @PathVariable Long countryId,
            @RequestParam("flag_image") MultipartFile flagImage
    ) {
        return countryService.uploadFlag(countryId, flagImage);
    }
}
