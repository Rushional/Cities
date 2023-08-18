package com.rushional.cities.services;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.dtos.CityDto;

import java.util.Optional;

public interface CityService {

    CitiesResponse getCities(
            int perPage,
            Optional<Integer> pageOptional,
            Optional<String> cityNameOptional,
            Optional<String> countryNameOptional
    );

    CityDto editCity(Long id, String name);
}
