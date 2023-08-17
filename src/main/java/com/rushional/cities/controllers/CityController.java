package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.exceptions.NotFoundException;
import com.rushional.cities.models.CityEntity;
import com.rushional.cities.repositories.CityRepository;
import com.rushional.cities.services.CityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityRepository cityRepository;
    private final CityService cityService;

    @GetMapping()
    public List<CitiesResponse> getCities(
            @RequestParam("perPage") int perPage,
            @RequestParam("page") Optional<Integer> pageOptional,
            @RequestParam("cityName") Optional<String> cityNameOptional,
            @RequestParam("countryName") Optional<String> countryNameOptional,
            UriComponentsBuilder uriBuilder,
            HttpServletResponse response
    ) {
        return cityService.getCitiesDtos(cityNameOptional, countryNameOptional);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<?> editCity(@PathVariable Long id, @RequestParam("name") String name) {
        CityEntity city = getCity(id);
        city.setName(name);
        cityRepository.save(city);
        return ResponseEntity.ok().build();
    }

    private CityEntity getCity(Long id) {
        Optional<CityEntity> cityOptional = cityRepository.findById(id);
        if (cityOptional.isEmpty()) throw new NotFoundException("City not found");
        return cityOptional.get();
    }
}
