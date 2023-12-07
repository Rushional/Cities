package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.services.CityService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping()
    public CitiesResponse getCities(
            @RequestParam(value = "per_page", defaultValue = "25") @NotBlank int perPage,
            @RequestParam(value = "page", defaultValue = "0") Optional<Integer> pageOptional,
            @RequestParam("city_name") @Size(max = 100) Optional<String> cityNameOptional,
            @RequestParam("country_name") @Size(max = 100) Optional<String> countryNameOptional
    ) {
        return cityService.getCities(perPage, pageOptional, cityNameOptional, countryNameOptional);
    }

    @PatchMapping("/{id}")
    public CityDto editCity(@PathVariable Long id, @RequestParam("name") String name) {
        return cityService.editCity(id, name);
    }
}
