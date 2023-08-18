package com.rushional.cities.controllers;

import com.rushional.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping()
    public ResponseEntity<?> getCities(
            @RequestParam(value = "per_page", defaultValue = "25") int perPage,
            @RequestParam(value = "page", defaultValue = "0") Optional<Integer> pageOptional,
            @RequestParam("city_name") Optional<String> cityNameOptional,
            @RequestParam("country_name") Optional<String> countryNameOptional
    ) {
        return new ResponseEntity<>(
                cityService.getCities(perPage, pageOptional, cityNameOptional, countryNameOptional),
                HttpStatus.OK);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<?> editCity(@PathVariable Long id, @RequestParam("name") String name) {
        return new ResponseEntity(cityService.editCity(id, name), HttpStatus.OK);
    }
}
