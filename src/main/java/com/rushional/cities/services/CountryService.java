package com.rushional.cities.services;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.models.CountryEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CountryService {
    CountriesResponse getAll(int perPage, int page);

    CountryDto uploadFlag(Long countryId, MultipartFile flagImage);

    CountryEntity getCountryById(Long id);

    String getFileNameWithoutExtension(CountryEntity country);
}
