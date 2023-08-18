package com.rushional.cities.services;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.dtos.CountryDto;
import org.springframework.web.multipart.MultipartFile;

public interface CountryService {
    CountriesResponse getAll(int perPage, int page);

    CountryDto uploadFlag(Long id, MultipartFile flagImage);
}
