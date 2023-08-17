package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.models.CityEntity;
import com.rushional.cities.repositories.CityRepository;
import com.rushional.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Value("${constants.image-host}")
    private String IMAGE_HOST;
    @Value("${constants.flags-bucket}")
    private String FLAGS_BUCKET;

    public List<CitiesResponse> getCitiesDtos(
            Optional<String> cityNameOptional,
            Optional<String> countryNameOptional
    ) {
        if (cityNameOptional.isPresent()) {
            String cityName = cityNameOptional.get();
            return countryNameOptional.map(
                    s -> getAllCitiesByCityNameAndCountryName(cityName, s))
                    .orElseGet(() -> getAllCitiesByName(cityName));
        }
        else
            return countryNameOptional.map(
                    this::getAllCitiesByCountryName)
                    .orElseGet(this::getAllCities);
    }

    private List<CitiesResponse> getAllCities() {
        return cityEntitiesToDtos(cityRepository.findAll());
    }

    private List<CitiesResponse> getAllCitiesByName(String name) {
        return cityEntitiesToDtos(cityRepository.findByName(name));
    }

    private List<CitiesResponse> getAllCitiesByCountryName(String countryName) {
        return cityEntitiesToDtos(cityRepository.findByCountryName(countryName));
    }

    private List<CitiesResponse> getAllCitiesByCityNameAndCountryName(String cityName, String countryName) {
        return cityEntitiesToDtos(cityRepository.findByCityNameAndCountryName(cityName, countryName));
    }

    private String getFlagUrl(String flagPath) {
        if (Objects.isNull(flagPath))
            return null;
        return IMAGE_HOST + "/" + FLAGS_BUCKET + "/" + flagPath;
    }

    private List<CitiesResponse> cityEntitiesToDtos(List<CityEntity> cityEntities) {
        List<CitiesResponse> citiesResponses = new ArrayList<>();
        Iterator<CityEntity> cityIterator = cityEntities.iterator();
        while (cityIterator.hasNext()) {
            CityEntity currentCity = cityIterator.next();
            citiesResponses.add(
                    new CitiesResponse(
                            currentCity.getId(),
                            currentCity.getName(),
                            currentCity.getCountry().getName(),
                            getFlagUrl(currentCity.getCountry().getFlagPath())
                    )
            );
        }
        return citiesResponses;
    }
}
