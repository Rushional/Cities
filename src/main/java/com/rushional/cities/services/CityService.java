package com.rushional.cities.services;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.dtos.UniqueCityNamesResponse;
import com.rushional.cities.models.City;
import com.rushional.cities.models.Country;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CityService {

    CitiesResponse getCities(
            int perPage,
            Optional<Integer> pageOptional,
            Optional<String> cityNameOptional,
            Optional<String> countryNameOptional
    );

    CityDto editCity(Long id, String name);

    UniqueCityNamesResponse getUniqueCityNames();

    CityDto uploadLogo(Long id, MultipartFile flagImage);

    String getFileNameWithoutExtension(City city);

    City getCityById(Long id);
}
