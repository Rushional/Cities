package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.models.CityEntity;
import com.rushional.cities.repositories.CityRepository;
import com.rushional.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Value("${constants.image-host}")
    private String IMAGE_HOST;
    @Value("${constants.flags-bucket}")
    private String FLAGS_BUCKET;

    @Override
    public List<CityDto> getAllCities() {
        List<CityEntity> cityEntities = cityRepository.findAll();
        List<CityDto> cityDtos = new ArrayList<>();
        Iterator<CityEntity> cityIterator = cityEntities.iterator();
        while (cityIterator.hasNext()) {
            CityEntity currentCity = cityIterator.next();
            cityDtos.add(
                    new CityDto(
                            currentCity.getName(),
                            currentCity.getCountry().getName(),
                            getFlagUrl(currentCity.getCountry().getFlagPath())
                    )
            );
        }
        return cityDtos;
    }

    private String getFlagUrl(String flagPath) {
        if (Objects.isNull(flagPath))
            return null;
        return IMAGE_HOST + "/" + FLAGS_BUCKET + "/" + flagPath;
    }
}
