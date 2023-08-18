package com.rushional.cities.services;

import com.rushional.cities.dtos.CountriesResponse;

import java.util.Optional;

public interface CountryService {
    CountriesResponse getAll(int perPage, int page);
}
