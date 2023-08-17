package com.rushional.cities.services;

import com.rushional.cities.dtos.CitiesResponse;

import java.util.List;
import java.util.Optional;

public interface CityService {

    List<CitiesResponse> getCitiesDtos(
            Optional<String> cityNameOptional,
            Optional<String> countryNameOptional
    );
}
