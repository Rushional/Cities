package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.dtos.UniqueCityNamesResponse;
import com.rushional.cities.services.CityService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.rushional.cities.utils.Constants.PER_PAGE_VALIDATION_ERROR_MESSAGE;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
@Validated
public class CityController {
    private final CityService cityService;

    @GetMapping()
    public CitiesResponse getCities(
            @RequestParam(value = "per_page", defaultValue = "25")
            @Min(value = -1, message = PER_PAGE_VALIDATION_ERROR_MESSAGE)
            int perPage,
            @RequestParam(value = "page") Optional<Integer> pageOptional,
            @RequestParam("city_name") Optional<String> cityNameOptional,
            @RequestParam("country_name") Optional<String> countryNameOptional
    ) {
        return cityService.getCities(perPage, pageOptional, cityNameOptional, countryNameOptional);
    }

    @GetMapping("/unique")
    public UniqueCityNamesResponse getUniqueCityNames() {
        return cityService.getUniqueCityNames();
    }

    @PatchMapping("/{id}")
    public CityDto editCity(@PathVariable Long id, @RequestParam("name") String name) {
        return cityService.editCity(id, name);
    }

    @PostMapping("{id}/upload-logo")
    public CityDto uploadLogo(
            @PathVariable Long id,
            @RequestParam("flag_image") MultipartFile flagImage
    ) {
        return cityService.uploadLogo(id, flagImage);
    }
}
