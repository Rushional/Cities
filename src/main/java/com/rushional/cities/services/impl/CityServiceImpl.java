package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CitiesResponse;
import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.dtos.UniqueCityNamesResponse;
import com.rushional.cities.exceptions.NotFoundException;
import com.rushional.cities.models.City;
import com.rushional.cities.models.Country;
import com.rushional.cities.repositories.CityRepository;
import com.rushional.cities.services.CityService;
import com.rushional.cities.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final FileUploadService fileUploadService;

    @Value("${constants.image-host}")
    private String IMAGE_HOST;
    @Value("${constants.cities-bucket}")
    private String CITIES_BUCKET;
    @Value("${constants.logos-path-in-bucket}")
    private String LOGOS_PATH_IN_BUCKET;

    public CitiesResponse getCities(
            int perPage,
            Optional<Integer> pageOptional,
            Optional<String> cityNameOptional,
            Optional<String> countryNameOptional
    ) {
        Pageable paging;
        int page;
        Sort sortByCountryName = Sort.by("country.name", "name");
        if (perPage == -1) {
            page = 0;
            paging = PageRequest.of(page, Integer.MAX_VALUE, sortByCountryName);
        }
        else {
            page = pageOptional.orElse(0);
            paging = PageRequest.of(page, perPage, sortByCountryName);
        }
        Page<City> pageCities;

        if (cityNameOptional.isPresent()) {
            String cityName = cityNameOptional.get();
            if (countryNameOptional.isPresent()) {
                pageCities = cityRepository.findByCityNameAndCountryName(cityName, countryNameOptional.get(), paging);
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
        City city = getCityById(id);
        city.setName(name);
        cityRepository.save(city);
        Country country = city.getCountry();
        return new CityDto(city.getId(), city.getName(),
                country.getName(), city.getLogoPath());
    }

    @Override
    public UniqueCityNamesResponse getUniqueCityNames() {
        return new UniqueCityNamesResponse(cityRepository.findDistinctNames());
    }

    @Override
    public CityDto uploadLogo(Long id, MultipartFile logoImage) {
        City city = getCityById(id);
        String fullFilePath = fileUploadService.uploadMultipartFile(
                LOGOS_PATH_IN_BUCKET,
                getFileNameWithoutExtension(city),
                logoImage,
                CITIES_BUCKET
        );
        city.setLogoPath(fullFilePath);
        cityRepository.save(city);
        return cityEntityToDto(city);
    }

    @Override
    public String getFileNameWithoutExtension(City city) {
        return city.getName() + "-" + city.getUuid() + ".";
    }

    private CityDto cityEntityToDto(City city) {
        return new CityDto(
                city.getId(),
                city.getName(),
                city.getCountry().getName(),
                getFlagUrl(city.getLogoPath()));
    }

    private String getFlagUrl(String flagPath) {
        if (Objects.isNull(flagPath))
            return null;
        return IMAGE_HOST + "/" + CITIES_BUCKET + "/" + flagPath;
    }

    @Override
    public City getCityById(Long id) {
        return cityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("City not found")
        );
    }
}
