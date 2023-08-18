package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.exceptions.NotFoundException;
import com.rushional.cities.models.CityEntity;
import com.rushional.cities.models.CountryEntity;
import com.rushional.cities.repositories.CityRepository;
import com.rushional.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Value("${constants.image-host}")
    private String IMAGE_HOST;
    @Value("${constants.flags-bucket}")
    private String FLAGS_BUCKET;

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

    @Override
    public CityDto editCity(Long id, String name) {
        CityEntity city = getCity(id);
        city.setName(name);
        cityRepository.save(city);
        CountryEntity country = city.getCountry();
        return new CityDto(city.getId(), city.getName(),
                country.getName(), country.getFlagPath());
    }

    private CityDto cityEntityToDto(CityEntity city) {
        return new CityDto(
                city.getId(),
                city.getName(),
                city.getCountry().getName(),
                getFlagUrl(city.getCountry().getFlagPath()));
    }

    private String getFlagUrl(String flagPath) {
        if (Objects.isNull(flagPath))
            return null;
        return IMAGE_HOST + "/" + FLAGS_BUCKET + "/" + flagPath;
    }

    private CityEntity getCity(Long id) {
        Optional<CityEntity> cityOptional = cityRepository.findById(id);
        if (cityOptional.isEmpty()) throw new NotFoundException("City not found");
        return cityOptional.get();
    }
}
