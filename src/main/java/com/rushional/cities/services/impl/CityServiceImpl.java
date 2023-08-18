package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.models.CityEntity;
import com.rushional.cities.repositories.CityRepository;
import com.rushional.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CitiesResponse getCities(
            int perPage,
            Optional<Integer> pageOptional,
            Optional<String> cityNameOptional,
            Optional<String> countryNameOptional
    ) {
        Pageable paging;
        int page;
        if (perPage == -1) {
            page = 0;
            paging = PageRequest.of(page, Integer.MAX_VALUE);
        }
        else {
            page = pageOptional.get();
            paging = PageRequest.of(page, perPage);
        }
        Page<CityEntity> pageCities;

        if (cityNameOptional.isPresent()) {
            String cityName = cityNameOptional.get();
            if (countryNameOptional.isPresent()) {
                pageCities = cityRepository.findByCityNameAndCountryName(cityName, countryNameOptional.get(),paging);
            }
            else {
                pageCities = cityRepository.findByName(cityName, paging);
            }
        }
        else {
            if (countryNameOptional.isPresent()) {
                pageCities = cityRepository.findByCountryName(countryNameOptional.get(), paging);
            } else {
                pageCities = cityRepository.findAll(paging);
            }
        }
        Page<CityDto> pageCityDtos = pageCities.map(this::cityEntityToDto);
        List<CityDto> cityDtos = pageCityDtos.getContent();
        return new CitiesResponse(
                cityDtos, perPage, page, pageCities.getTotalPages(), pageCities.getTotalElements()
        );
    }

    private CityDto cityEntityToDto(CityEntity city) {
        return new CityDto(
                city.getId(),
                city.getName(),
                city.getCountry().getName(),
                city.getCountry().getFlagPath());
    }
}
